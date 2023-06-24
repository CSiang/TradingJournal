package tfip_project.financial_analysis.Models;

import java.util.Date;

import com.google.api.services.calendar.model.Event;

public class EventDetail {
   
    String eventId;
    // summary is similar to its title.
    String summary;
    String description;
    Date startDate;
    Date endDate;
    Long startDateLong;
    Long endDateLong;
    
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        this.startDateLong = startDate.getTime();
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        this.endDateLong = endDate.getTime();
    }
    public Long getStartDateLong() {
        return startDateLong;
    }
    public void setStartDateLong(Long startDateLong) {
        this.startDateLong = startDateLong;
        this.startDate = new Date(startDateLong);
    }
    public Long getEndDateLong() {
        return endDateLong;
    }
    public void setEndDateLong(Long endDateLong) {
        this.endDateLong = endDateLong;
        this.endDate = new Date(endDateLong);
    }
    @Override
    public String toString() {
        return "EventDetail [eventId=" + eventId + ", summary=" + summary + ", description=" + description
                + ", startDate=" + startDate + ", endDate=" + endDate + ", startDateLong=" + startDateLong
                + ", endDateLong=" + endDateLong + "]";
    }

    public static EventDetail getEventDetail(Event event){

        EventDetail ed = new EventDetail();
        ed.setEventId(event.getId());
        ed.setSummary(event.getSummary());
        ed.setDescription(event.getDescription());
        ed.setStartDateLong(event.getStart().getDateTime().getValue());
        ed.setEndDateLong(event.getEnd().getDateTime().getValue());

        return ed;
    }

    
    



}
