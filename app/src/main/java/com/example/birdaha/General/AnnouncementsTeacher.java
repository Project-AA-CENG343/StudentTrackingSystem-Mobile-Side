package com.example.birdaha.General;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsTeacher {
    @SerializedName("classroom-announcements")
    ArrayList<ClassAnnouncementModel> classroomAnnouncements;

    public ArrayList<ClassAnnouncementModel> getClassroomAnnouncements() {
        return classroomAnnouncements;
    }

    public void setClassroomAnnouncements(ArrayList<ClassAnnouncementModel> classroomAnnouncements) {
        this.classroomAnnouncements = classroomAnnouncements;
    }
}
