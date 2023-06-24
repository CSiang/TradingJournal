package tfip_project.financial_analysis;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Acl;
import com.google.api.services.calendar.model.AclRule;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.AclRule.Scope;

import jakarta.transaction.Transactional;
import tfip_project.financial_analysis.Models.TradingRecord;
import tfip_project.financial_analysis.Repositories.TradeRecordRepository;
import tfip_project.financial_analysis.Security.Models.AppUser;
import tfip_project.financial_analysis.Security.Models.Role;
import tfip_project.financial_analysis.Services.CalendarService;
import tfip_project.financial_analysis.Services.EmailService;
import tfip_project.financial_analysis.Services.GoogleCalendarService;
import tfip_project.financial_analysis.Services.TradeRecordService;
import tfip_project.financial_analysis.Services.UserService;
import tfip_project.financial_analysis.Services.yhFinanceService;
import yahoofinance.Stock;

@SpringBootApplication
public class FinancialAnalysisApplication implements CommandLineRunner {

	@Autowired
	CalendarService calSvc;

	@Autowired
    Calendar gCalSvc;

	@Autowired
	yhFinanceService yhSvc;

	@Autowired
	UserService userSvc;

	@Autowired
	TradeRecordRepository trRepo;

	@Autowired
	TradeRecordService trSvc;

	@Autowired
	private EmailService emailSvc;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public static void main(String[] args) {
		SpringApplication.run(FinancialAnalysisApplication.class, args);
	}
	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		
		// SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// Date parsedDate = dateFormat.parse("01/06/2023");
		// String calendarId = "cscorpio87@gmail.com";
		// // String projectCalendarId = "72db9d51f7p95lapc16gip8j88@group.calendar.google.com";
		// String projectCalendarId = userSvc.getUser("BL").getCalendarId();

		// Long value = Long.valueOf(1200*60*60*24*100) ;
		// Long dateNow = new Date().getTime();
		// Long adjusted = dateNow - value;
		// String startDate = "01/06/2023";
		// String endDate = "25/06/2023";

		// System.out.println("calendarId is: "+ projectCalendarId);

		// Optional<List<CalendarListEntry>> optCalList =  calSvc.getCalendarList();
		// if(optCalList.isPresent()){
		// 	List<CalendarListEntry> calList = optCalList.get();

		// 	for(CalendarListEntry entry: calList){
		// 		System.out.printf("\nCalendar ID: %s, Calendar Summary: %s\n",
        //                     entry.getId(),entry.getSummary());
		// 	}
		// }

		// Below is getting events from my own primary calendar.
		// Optional<List<Event>> optEvents =  calSvc.getEvents(calendarId,startDate, endDate);
		// if(optEvents.isPresent()){
		// 	System.out.println("Before adding event...");
		// 	List<Event> events = optEvents.get();
		// 	for(Event event: events){
		// 		System.out.printf("\nEvent summary: %s, Event start: %s, Event end: %s\n",
		// 		event.getSummary(), event.getStart(), event.getEnd());
		// 	}
		// }

		// String title="Testing";
		// String body="Just to test to ensure insert event work.";
		// String startDateTime = "2023-06-24T10:50:00+08:00";
		// Date parsedStartDate = dateFormat.parse("25/06/2023");
		// Event newEvent = calSvc.newEvent(calendarId, startDateTime, title, body);
		// calSvc.insertEvent(projectCalendarId, newEvent);

		// Optional<List<Event>> optEvents2 =  calSvc.getEvents(projectCalendarId, startDate, endDate);
		// if(optEvents2.isPresent()){
		// System.out.println("After adding event...");
		// List<Event> events = optEvents2.get();
		// if( !(events.size() >0) ){
		// 	System.out.println("There is no event....");
		// } else {
		// 	for(Event event: events){
		// 		System.out.println("Event: "+ event);
		// 		System.out.printf("\nEvent ID: %s, Event summary: %s, Event start: %s, Event end: %s\n",
		// 		event.getId(), event.getSummary(), event.getStart(), event.getEnd().getDateTime());

		// 		System.out.println("Date is java date format: "+new Date(event.getStart().getDateTime().getValue())) ;
		// 	}
		// }
		// }

		


	}

}
