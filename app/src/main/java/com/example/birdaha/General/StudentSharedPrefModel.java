package com.example.birdaha.General;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class StudentSharedPrefModel {

    @NonNull
    @Override
    public String toString() {
        return lastHomeworkId + " - " + lastAnnouncementId + " - " + studentId;
    }

    private int lastHomeworkId = -1;
    private int lastAnnouncementId = -1;
    private int studentId = -1;



    private String classroomName = "";

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public int getLastHomeworkId() {
        return lastHomeworkId;
    }

    public void setLastHomeworkId(int lastHomeworkId) {
        this.lastHomeworkId = lastHomeworkId;
    }

    public int getLastAnnouncementId() {
        return lastAnnouncementId;
    }

    public void setLastAnnouncementId(int lastAnnouncementId) {
        this.lastAnnouncementId = lastAnnouncementId;
    }

    public StudentSharedPrefModel(int lastHomeworkId, int lastAnnouncementId, int studentId, String classroomName) {
        this.classroomName = classroomName;
        this.lastHomeworkId = lastHomeworkId;
        this.lastAnnouncementId = lastAnnouncementId;
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

        /*public StudentData deserializeStudent(String studentJson)
        {

        }*/
}

