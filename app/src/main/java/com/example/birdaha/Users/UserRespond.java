package com.example.birdaha.Users;

import com.example.birdaha.Classrooms.Classroom;

public class UserRespond {
    private int respondCode;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRespondCode() {
        return respondCode;
    }

    public void setRespondCode(int respondCode) {
        this.respondCode = respondCode;
    }
}
