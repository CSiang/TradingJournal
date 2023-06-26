package tfip_project.financial_analysis.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.calendar.model.Event;

import tfip_project.financial_analysis.Models.EventDetail;
import tfip_project.financial_analysis.Payload.MsgResponse;
import tfip_project.financial_analysis.Security.Models.AppUser;
import tfip_project.financial_analysis.Services.CalendarService;
import tfip_project.financial_analysis.Services.UserService;

@RestController
@RequestMapping(path = "/calendar")
public class CalendarEventController {
    
    @Autowired
    CalendarService calSvc;

    @Autowired
    UserService userSvc;

    @PostMapping(path = "/addEvent")
    // Pay attention that to use Map, MultiValueMap is for x-www-form-urlencoded.
    public ResponseEntity<MsgResponse> addCalEvent(@RequestBody Map<String, String> eventData){

        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = userSvc.getUser(username);
        System.out.println("User: " + user);
        String title = eventData.get("title");
        String body = eventData.get("body");
        String startDateTime = eventData.get("startDateTime");
        String endDateTime = eventData.get("endDateTime");
        // System.out.printf("\nThe form received is:\n%s\n", eventData);
        // System.out.printf("\nTitle: %s, body: %s, startDateTime: %s, endDateTime: %s\n",
        //         title, body, startDateTime, endDateTime);

        Event newEvent = calSvc.newEvent(title, body, startDateTime, endDateTime);
        Boolean outcome = calSvc.insertEvent(user.getCalendarId(), newEvent);

        // outcome == true
        if(outcome){
            return ResponseEntity.ok().body(new MsgResponse("Event has been successfully created."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MsgResponse("Event creation is failed...please check with administrator."));
        }
    }

    @GetMapping(path="/eventList")
    // Format of date string to be dd/MM/yyyy. endDate must be later than startDate
    public ResponseEntity<List<EventDetail>> getEventsByPeriod(
        @RequestParam(value = "start") String startDateTime,
        @RequestParam(value = "end") String endDateTime) {

        // System.out.println("startDateTime: " + startDateTime);
        // System.out.println("endDateTime: " + endDateTime);

        List<EventDetail> eventDetailList = new ArrayList<>();
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = userSvc.getUser(username);
        // System.out.println("User: " + user);

        try{
            Optional<List<Event>> optEventList = calSvc.getEvents(user.getCalendarId(), startDateTime, endDateTime);
            if(optEventList.isPresent()){
                List<Event> events = optEventList.get();
                for(Event event: events){
                    eventDetailList.add(EventDetail.getEventDetail(event));
                }
            }
            return ResponseEntity.ok(eventDetailList);
        } catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(eventDetailList);
        }
    }

    @DeleteMapping(path ="delete/{eventId}")
    public ResponseEntity<MsgResponse> deleteEvent(@PathVariable String eventId){

        // System.out.println("eventId received: "+ eventId);
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser user = userSvc.getUser(username);

        if(calSvc.deleteEvent(user.getCalendarId(), eventId)){
            return ResponseEntity.ok(new MsgResponse("Calendar event is removed."));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new MsgResponse("Calendar event removal unsuccessful, please contact administrator.")
                    );
        }

    }

    

}
