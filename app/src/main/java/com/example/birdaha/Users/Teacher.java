package com.example.birdaha.Users;

import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.Classrooms.Lecture;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User{

    public Teacher(String name,int teacher_id, Lecture course, List<Classroom> classrooms){
        this.name = name;
        this.teacher_id = teacher_id;
        this.course = course;
        this.classrooms = classrooms;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }
}
