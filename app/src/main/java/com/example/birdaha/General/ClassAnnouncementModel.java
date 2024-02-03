package com.example.birdaha.General;

import com.example.birdaha.Users.Teacher;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClassAnnouncementModel implements Serializable {
    @SerializedName("classroom_announcement_id")
    private int announcement_id;
    private Teacher teacher;
    private int teacher_id;
    private int classroom_id;

    private boolean isEditButtonVisible;
    private boolean isRemoveButtonVisible;

    @SerializedName("announcement_title")
    private String title;
    @SerializedName("announcement_content")
    private String details;


    public ClassAnnouncementModel(String title, String details, int classroom_id, int teacher_id) {
        this.title = title;
        this.details = details;
        this.classroom_id = classroom_id;
        this.teacher_id = teacher_id;
    }

    /**
     * Constructor for the ClassAnnouncementModel class with only a title.
     *
     * @param title The title of the class announcement.
     */
    public ClassAnnouncementModel(String title) {
        this.title = title;
    }

    /**
     * Get the title of the class announcement.
     *
     * @return The title of the class announcement.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the class announcement.
     *
     * @param title The title of the class announcement.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the details or content of the class announcement.
     *
     * @return Details or content of the class announcement.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Set the details or content of the class announcement.
     *
     * @param details Details or content of the class announcement.
     */
    public void setDetails(String details) {
        this.details = details;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getAnnouncement_id() {
        return announcement_id;
    }

    public void setAnnouncement_id(int announcement_id) {
        this.announcement_id = announcement_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }
}
