package com.example.guidance.realm.model.onecallWeather;

/**
 * Created by Conor K on 05/03/2021.
 */
public class alerts {

    String sender_name;
    String event;
    int start;
    int end;
    String description;

    public alerts(String sender_name, String event, int start, int end, String description) {
        this.sender_name = sender_name;
        this.event = event;
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getEvent() {
        return event;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getDescription() {
        return description;
    }
}
