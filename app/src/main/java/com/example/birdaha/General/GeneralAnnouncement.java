package com.example.birdaha.General;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents a general announcement that contains a title and details.
 */
public class GeneralAnnouncement {
    private int general_announcement_id;
    @SerializedName("announcement_title")
    private String title;   // The title of the general announcement
    @SerializedName("announcement_content")
    private String details; // The details or content of the general announcement

    /**
     * Constructor for the GeneralAnnouncement class.
     *
     * @param title   The title of the general announcement.
     * @param details The details or content of the general announcement.
     */
    public GeneralAnnouncement(String title, String details) {
        this.title = title;
        this.details = details;
    }

    public int getGeneral_announcement_id() {
        return general_announcement_id;
    }

    public void setGeneral_announcement_id(int general_announcement_id) {
        this.general_announcement_id = general_announcement_id;
    }

    /**
     * Get the title of the general announcement.
     *
     * @return The title of the general announcement.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the general announcement.
     *
     * @param title The title of the general announcement.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the details or content of the general announcement.
     *
     * @return The details or content of the general announcement.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Set the details or content of the general announcement.
     *
     * @param details The details or content of the general announcement.
     */
    public void setDetails(String details) {
        this.details = details;
    }
}
