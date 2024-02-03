package com.example.birdaha.Users;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordTeacher {
    private int teacher_id;
    @SerializedName("password")
    private String newPassword;

    public ChangePasswordTeacher(int teacher_id,String newPassword){
        this.teacher_id = teacher_id;
        this.newPassword = newPassword;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
