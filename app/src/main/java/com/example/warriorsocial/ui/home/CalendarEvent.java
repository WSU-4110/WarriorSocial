package com.example.warriorsocial.ui.home;


import com.google.firebase.Timestamp;

public class CalendarEvent {
    private String organizationName;
    private String eventTitle;
    private int eventId;
    private String eventDescription;
    private String eventTimestamp;
    //private Timestamp eventTimestamp;

    public CalendarEvent() {}

    public CalendarEvent(String organizationName, String eventTitle, int id, String eventDescription, String eventTimestamp/*Timestamp eventTimestamp*/) {
        this.organizationName = organizationName;
        this.eventTitle = eventTitle;
        this.eventId = id;
        this.eventDescription = eventDescription;
        this.eventTimestamp = eventTimestamp;
    }

    public String getOrganizationName() { return organizationName; }
    public String getEventTitle() { return eventTitle; }
    public int getEventId() { return eventId; }
    public String getEventDescription() { return eventDescription; }
    //public Timestamp getEventTimestamp() { return eventTimestamp; }
    public String getEventTimestamp() { return eventTimestamp; }

    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }
    //public void setEventTimestamp(Timestamp eventTimestamp) { this.eventTimestamp = eventTimestamp; }
    public void setEventTimestamp(String eventTimestamp) {this.eventTimestamp = eventTimestamp;}
}
