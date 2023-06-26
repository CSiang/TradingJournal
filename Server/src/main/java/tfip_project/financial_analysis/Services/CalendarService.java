package tfip_project.financial_analysis.Services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Acl;
import com.google.api.services.calendar.model.AclRule;
import com.google.api.services.calendar.model.AclRule.Scope;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

@Service
public class CalendarService {
    
    // This is from the GCalendarConfig.java
    @Autowired
    Calendar gCalSvc;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    

    public String createNewCalendar(Long AppUserId) throws IOException{

        String summary = "Trading journal-user#" + String.valueOf(AppUserId);
        String newCalId = "";

        com.google.api.services.calendar.model.Calendar newCal = new com.google.api.services.calendar.model.Calendar();
        newCal.setSummary(summary);
        com.google.api.services.calendar.model.Calendar insertedCal = gCalSvc.calendars().insert(newCal).execute();
        newCalId = insertedCal.getId();

        // Below is to add me into the calendar, so i can see and manipulate the calendar if necessary.
        AclRule rules = new AclRule();
        Scope scope = new Scope();
        scope.setValue("cscorpio87@gmail.com");
        scope.setType("user");
        rules.setScope(scope);
        rules.setRole("owner");
        gCalSvc.acl().insert(newCalId, rules).execute();

        return newCalId;
    }

    // Format of date string to be dd/MM/yyyy. endDate must be later than startDate
    public Optional<List<Event>> getEvents(String calendarId, String startDate, String endDate) throws ParseException{
		
		Date parsedStartDate = dateFormat.parse(startDate);
        Date parsedEndDate = dateFormat.parse(endDate);
        // Use the newEndDate so the search result will include the date of endDate. Per the API, the resulted events are endDate exclusive.
        Date newEndDate = new Date(parsedEndDate.getTime() + 24*60*60*1000); 
        
        // System.out.println("parsedStartDate: "+ parsedStartDate);
        // System.out.println("parsedEndDate: "+ parsedEndDate);
        // System.out.println("newEndDate: "+ newEndDate);
        // System.out.println("DateTime of parsedStartDate: "+ new DateTime(parsedStartDate));
        // System.out.println("DateTime of parsedEndDate: "+ new DateTime(parsedEndDate));

        try{
            // timeMax must be later than timeMin.
            Events events = gCalSvc.events().list(calendarId)
                                .setTimeMin(new DateTime(parsedStartDate))
                                .setTimeMax(new DateTime(newEndDate))
                                // setTimeZone is to set the timezone of the event you get from this API. if not it will be per calendar default, for this service account, it is UTC.
                                .setTimeZone("Asia/Singapore")
                                .execute();
            System.out.println("Calendar events as follow: ");
    
            List<Event> eventList = events.getItems();
    
            // for(Event event: eventList){
            //     System.out.println("Event: " + event);
            // }
            return Optional.of(eventList);

        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Error from calendar events().list() method. Message: " + ex.getMessage());
        }
        
        return Optional.empty();
    }

    public Optional<List<CalendarListEntry>> getCalendarList(){
        try{
            CalendarList calendarList =  gCalSvc.calendarList().list().execute();
            List<CalendarListEntry> items = calendarList.getItems();
    
            // System.out.println("Below is the list of calendars from gCalSvc:");
            // for (CalendarListEntry calendarListEntry : items) {
            //     System.out.printf("\nCalendar ID: %s, Calendar Summary: %s\n",
            //                 calendarListEntry.getId(),calendarListEntry.getSummary());
            // }
            return Optional.of(items);

        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("Error from calendar calendarList().list() method. Message: " + ex.getMessage());
        }
        return Optional.empty();
    }

    public Boolean deleteCalendar(String calendarId){
        try{
            gCalSvc.calendars().delete(calendarId).execute();
            return true;
        } catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Issue when deleting calendar with ID: "+ calendarId);
            return false;
        }
    }

    public Optional<List<AclRule>> getRules(String calendarId){
        try{
            Acl acl = gCalSvc.acl().list(calendarId).execute();
            List<AclRule> rules =  acl.getItems();
            return Optional.of(rules);
        } catch(Exception ex){
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    /*
     * ruleId is something similar to "user:cscorpio87@gmail.com" at "id" section.
     * Role is selected from "none", "freeBusyRead", "reader", "writer", & "owner". Only "owner" can delete the calendar.
     * See https://developers.google.com/calendar/api/concepts/sharing
     */
    public Boolean amendRole(String calendarId, String ruleId, String newRole){
        try{
            // Retrieve the access rule from the API
            AclRule rule = gCalSvc.acl().get(calendarId, "user:cscorpio87@gmail.com").execute();
            // Make a change
            rule.setRole(newRole);
            // Update the access rule
            AclRule updatedRule = gCalSvc.acl().update(calendarId, rule.getId(), rule).execute();
            System.out.println(updatedRule.getEtag());
            return true;
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    // Need to test to add event into calendar, send up the reminder properly.
    // 1 way of thought is to send up the reminder as email and add the email of the user into attendee, hence email reminder will be sent to the email of user.
    // Sample format for startDateTime & endDateTime: "2023-06-24T11:00:00+08:00".
    public Event newEvent(String title, String body, String startDateTime, String endDateTime ) {
        // Create new event.
        Event event = new Event();
        event.setSummary(title);

        DateTime end = new DateTime(endDateTime);
        System.out.println("The datetime in newEvent method is: " + end);

        // Date endDate = new Date(startDate.getTime() + 3600000);
        // DateTime start = new DateTime(startDate, TimeZone.getTimeZone("Asia/Singapore"));
        DateTime start = new DateTime(startDateTime);
        System.out.println("The start datetime in newEvent method is: " + start);

        event.setStart(new EventDateTime().setDateTime(start).setTimeZone("Asia/Singapore"));
        // DateTime end = new DateTime(endDate, TimeZone.getTimeZone("Asia/Singapore"));

        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone("Asia/Singapore"));

        // Can't set EventAttendee without Domain-wide delegation.
        // String organizerEmail
        // List<String> attendeeEmails
        // List<EventAttendee> eventAttendees = new ArrayList<>();
        // EventAttendee organizerAttendee = new EventAttendee();
        // organizerAttendee.setEmail(organizerEmail);
        // organizerAttendee.setOrganizer(true);
        // eventAttendees.add(organizerAttendee);
        // for (String attendee: attendeeEmails) {
        //     EventAttendee eventAttendee = new EventAttendee();
        //     eventAttendee.setEmail(attendee);
        //     eventAttendees.add(eventAttendee);
        // }
        // event.setAttendees(eventAttendees);


        event.setDescription(body);

        // Reminder is not used. Not able to set reminder for attendee or other person than this service email, hence it is pointless to set.
        // EventReminder[] reminderOverrides = new EventReminder[] {
        //     new EventReminder().setMethod("email").setMinutes(24 * 60),
        //     new EventReminder().setMethod("popup").setMinutes(5),
        //     new EventReminder().setMethod("email").setMinutes(5),
        //     new EventReminder().setMethod("email").setMinutes(830),
        // };
        // Event.Reminders reminders = new Event.Reminders()
        //     .setUseDefault(false)
        //     .setOverrides(Arrays.asList(reminderOverrides));
        // event.setReminders(reminders);

        return event;
    }

    public Boolean insertEvent(String calendarId, Event event){
        try{
            gCalSvc.events().insert(calendarId, event).execute();
            return true;
        } catch(Exception ex){
            ex.printStackTrace();
            System.out.println("Error when inserting event: " + ex.getMessage());
            return false;
        }
    }

    public Boolean deleteEvent(String calendarId, String eventId){
        try{
            gCalSvc.events().delete(calendarId,eventId).execute();
            return true;
        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

}
