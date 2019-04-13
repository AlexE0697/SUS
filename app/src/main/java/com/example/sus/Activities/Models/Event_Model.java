package com.example.sus.Activities.Models;

public class Event_Model {

    private String event_title;
    private String event_description;
    private String event_timestamp;
    private String event_by;
    private String event_price;

    public Event_Model() {
    }

    public String getevent_title() {
        return event_title;
    }

    public void setevent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getevent_description() {
        return event_description;
    }

    public void setevent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getevent_timestamp() {
        return event_timestamp;
    }

    public void setevent_timestamp(String event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    public String getevent_by() {
        return event_by;
    }

    public void setevent_by(String event_by) {
        this.event_by = event_by;
    }

    public String getevent_price() { return event_price; }

    public void setevent_price(String event_price) {

        this.event_price = event_price;
    }
}
