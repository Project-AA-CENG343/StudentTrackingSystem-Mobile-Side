package com.example.birdaha.Classrooms;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Lecture implements Serializable {
    private int course_id;
    @SerializedName("course_name")
    private String name;

    public Lecture(int course_id, String name) {
        this.course_id = course_id;
        this.name = name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}