package com.example.birdaha.General;

import java.io.Serializable;

public class HomeworkResult implements Serializable{

    transient boolean isGradedBefore;
    public boolean isGradedBefore() {
        return isGradedBefore;
    }

    public void setGradedBefore(boolean gradedBefore) {
        isGradedBefore = gradedBefore;
    }

    @Override
    public String toString() {
        return "HomeworkResult{" +
                "isGradedBefore=" + isGradedBefore +
                ", homework_result_id=" + homework_result_id +
                ", homework_id=" + homework_id +
                ", student_id=" + student_id +
                ", note_for_parent='" + note_for_parent + '\'' +
                ", grade=" + grade +
                '}';
    }

    int homework_result_id;
    int homework_id;
    int student_id;
    String note_for_parent;
    int grade;

    public int getHomework_id() {
        return homework_id;
    }

    public void setHomework_id(int homework_id) {
        this.homework_id = homework_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getNote_for_parent() {
        return note_for_parent;
    }

    public void setNote_for_parent(String note_for_parent) {
        this.note_for_parent = note_for_parent;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getHomework_result_id() {
        return homework_result_id;
    }

    public void setHomework_result_id(int homework_result_id) {
        this.homework_result_id = homework_result_id;
    }
}
