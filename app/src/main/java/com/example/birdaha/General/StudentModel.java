package com.example.birdaha.General;

import java.io.Serializable;

public class StudentModel implements Serializable, Comparable<StudentModel> {

    private int student_id;
    private String name;
    private int student_no;
    private String classroom_id;


    public StudentModel(int student_id, String name, int student_no, String classroom_id) {
        this.student_id = student_id;
        this.name = name;
        this.student_no = student_no;
        this.classroom_id = classroom_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudent_no() {
        return student_no;
    }

    public void setStudent_no(int student_no) {
        this.student_no = student_no;
    }

    public String getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(String classroom_id) {
        this.classroom_id = classroom_id;
    }


    @Override
    public int compareTo(StudentModel o) {
        return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
    }

}
