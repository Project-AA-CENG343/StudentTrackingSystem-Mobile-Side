package com.example.birdaha.Users;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordStudent {
    private int student_id;
    @SerializedName("password")
    private String newPassword;

    public ChangePasswordStudent(int student_id, String newPassword) {
        this.student_id = student_id;
        this.newPassword = newPassword;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
