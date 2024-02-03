package com.example.birdaha.General;

import android.graphics.Bitmap;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class represents an event with a title, image resource, and details.
 */
public class Event {
    private int event_id;
    @SerializedName("event_image")
    private String image;
    private ScaleTypes scaleTypes;

    @SerializedName("event_title")
    private String title;          // The title of the event
    private int imageResource;// The resource ID of the event's image
    @SerializedName("event_content")
    private String details;        // Details or description of the event

    /**
     * Constructor for the Event class.
     *
     * @param title         The title of the event.
     * @param imageResource The resource ID of the event's image.
     * @param details       Details or description of the event.
     */
    public Event(String title, int imageResource, String details) {
        this.title = title;
        this.imageResource = imageResource;
        this.details = details;
    }

    public Event(int event_id, int image, ScaleTypes scaleTypes, String title, String details) {
        this.event_id = event_id;
        this.imageResource = image;
        this.scaleTypes = scaleTypes;
        this.title = title;
        this.details = details;
    }

    /**
     * Get the resource ID of the event's image.
     *
     * @return The resource ID of the event's image.
     */
    public int getImageResource() {
        return imageResource;
    }

    /**
     * Set the resource ID of the event's image.
     *
     * @param imageResource The resource ID of the event's image.
     */
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    /**
     * Get the title of the event.
     *
     * @return The title of the event.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the event.
     *
     * @param title The title of the event.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the details or description of the event.
     *
     * @return Details or description of the event.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Set the details or description of the event.
     *
     * @param details Details or description of the event.
     */
    public void setDetails(String details) {
        this.details = details;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
