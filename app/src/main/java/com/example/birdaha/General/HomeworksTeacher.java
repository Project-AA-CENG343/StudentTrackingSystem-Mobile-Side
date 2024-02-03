package com.example.birdaha.General;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HomeworksTeacher {
    ArrayList<HwModel> homeworks;
    ArrayList<StudentModel> students;

    public ArrayList<StudentModel> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<StudentModel> students) {
        this.students = students;
    }

    public ArrayList<HwModel> getHomeworks() {
        return homeworks;
    }

    public void setHomeworks(ArrayList<HwModel> homeworks) {
        this.homeworks = homeworks;
    }

}
