package com.example.birdaha.General;

import com.google.gson.annotations.SerializedName;

public class SendProfilePictureTeacher {
    @SerializedName("image_teacher")
    private String image;
    @SerializedName("teacherId")
    private int teacher_id;

    public SendProfilePictureTeacher(String image, int teacher_id) {
        this.image = image;
        this.teacher_id = teacher_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }
}
