package com.example.birdaha.General;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {
    @SerializedName("homework_id")
    private int homeworkId;

    @SerializedName("announcement_id")
    private int announcementId;

    private int responseCode;

    public int getResponseCode() {
        return responseCode;
    }

    public int getHomeworkId() {
        return homeworkId;
    }

    public void setModel(NotificationModel model, int responseCode)
    {
        this.homeworkId = model.getHomeworkId();
        this.announcementId = model.getAnnouncementId();
        this.responseCode = responseCode;
    }

    public int getAnnouncementId() {
        return announcementId;
    }
}
