package com.example.birdaha.General;

import com.google.gson.annotations.SerializedName;

public class SendProfilePictureStudent {
    @SerializedName("image_student")
    private String student_image;
    @SerializedName("studentId")
    private int student_id;

    public SendProfilePictureStudent(String student_image, int student_id) {
        this.student_image = student_image;
        this.student_id = student_id;
    }

    public String getStudent_image() {
        return student_image;
    }
    public void setStudent_image(String student_image) {
        this.student_image = student_image;
    }

    public int getStudent_id() {
        return student_id;
    }
    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }
}
