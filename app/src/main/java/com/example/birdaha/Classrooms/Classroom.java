package com.example.birdaha.Classrooms;

import com.example.birdaha.General.HwModel;
import com.example.birdaha.Users.Student;
import com.example.birdaha.Users.Teacher;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Classroom implements Serializable {
    private int classroom_id;
    @SerializedName("classroom_name")
    private String name;
    //private Teacher classroomTeacher;
    private List<Student> students;
    private List<HwModel> homeworks;
    private List<ClassAnnouncement> classAnnouncements;

    public Classroom(String name, int classroom_id){

        this.classroom_id = classroom_id;
        this.name = name;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public Teacher getClassroomTeacher() {
        return classroomTeacher;
    }

    public void setClassroomTeacher(Teacher classroomTeacher) {
        this.classroomTeacher = classroomTeacher;
    }*/

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<HwModel> getHomeworks() {
        return homeworks;
    }

    public void setHomeworks(List<HwModel> homeworks) {
        this.homeworks = homeworks;
    }

    public List<ClassAnnouncement> getClassAnnouncements() {
        return classAnnouncements;
    }

    public void setClassAnnouncements(List<ClassAnnouncement> classAnnouncements) {
        this.classAnnouncements = classAnnouncements;
    }
}
