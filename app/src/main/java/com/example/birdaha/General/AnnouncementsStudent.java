package com.example.birdaha.General;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsStudent {
    @SerializedName("classroom_announcements")
    private ArrayList<ClassAnnouncementModel> classAnnouncements;

    public ArrayList<ClassAnnouncementModel> getClassAnnouncements() {
        return classAnnouncements;
    }

    public void setClassAnnouncements(ArrayList<ClassAnnouncementModel> classAnnouncements) {
        this.classAnnouncements = classAnnouncements;
    }
}
