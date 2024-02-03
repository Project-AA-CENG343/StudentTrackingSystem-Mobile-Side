package com.example.birdaha.General;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class HwModel implements Serializable, Comparable<HwModel> {
    private int homework_id;
    private int teacher_id;
    private int classroom_id;
    private String course_name;
    private String due_date;
    @SerializedName("hw_image")
    private String image;
    private String title;

    @SerializedName("image")
    private String getImage;

    @SerializedName("content")
    private String info;

    private ArrayList<HomeworkResult> results;

    public HomeworkResult getResult() {
        if(!results.isEmpty())
            return results.get(0);
        else
            return null;
    }



    public HwModel(int classroom_id, int teacher_id, String course_name, String dueDate, String title, String info, String image){
        this.classroom_id = classroom_id;
        this.teacher_id = teacher_id;
        this.course_name = course_name;
        this.due_date = dueDate;
        this.title = title;
        this.info = info;
        this.image = image;
    }

    public HwModel(int classroom_id, int teacher_id, String title, String info, String course_name,String due_date){
        this.classroom_id = classroom_id;
        this.teacher_id = teacher_id;
        this.title = title;
        this.info = info;
        this.course_name = course_name;
        this.due_date = due_date;

    }
    public int getHomework_id() {
        return homework_id;
    }

    public void setHomework_id(int homework_id) {
        this.homework_id = homework_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the homework.
     *
     * @param title The title of the homework.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int compareTo(HwModel o) {
        return this.title.toLowerCase().compareTo(o.getTitle().toLowerCase());
    }

    public String getGetImage() {
        return getImage;
    }

    public void setGetImage(String getImage) {
        this.getImage = getImage;
    }

}

