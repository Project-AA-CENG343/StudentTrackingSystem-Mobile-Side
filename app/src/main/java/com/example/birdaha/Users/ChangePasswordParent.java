package com.example.birdaha.Users;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordParent {
    private int parent_id;
    @SerializedName("password")
    private String newPassword;

    public ChangePasswordParent(int parent_id, String newPassword) {
        this.parent_id = parent_id;
        this.newPassword = newPassword;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
