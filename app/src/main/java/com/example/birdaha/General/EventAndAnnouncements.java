package com.example.birdaha.General;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EventAndAnnouncements {
    private ArrayList<Event> events;
    @SerializedName("announcements")
    private ArrayList<GeneralAnnouncement> generalAnnouncements;

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<GeneralAnnouncement> getGeneralAnnouncements() {
        return generalAnnouncements;
    }

    public void setGeneralAnnouncements(ArrayList<GeneralAnnouncement> generalAnnouncements) {
        this.generalAnnouncements = generalAnnouncements;
    }
}
