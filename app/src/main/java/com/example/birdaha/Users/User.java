package com.example.birdaha.Users;

import android.graphics.Bitmap;

import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.Classrooms.Lecture;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    protected String username;
    protected String password;

    protected String name;
    //@SerializedName("phone")
    protected String phone;
    private int parent_id;
    protected int teacher_id;
    protected int course_id;
    protected int student_id;
    private int classroom_id;
    @SerializedName("student_no")
    protected long school_no;
    protected Classroom classroom;
    protected List<Classroom> classrooms;
    protected Lecture course;
    private List<Student> students;
    @SerializedName("image_student")
    protected String sendStudentPicture;
    @SerializedName("image_teacher")
    protected String sendTeacherPicture;
    @SerializedName("student_image")
    protected String getStudentImage;
    @SerializedName("teacher_image")
    protected String getTeacherImage;



    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public Lecture getCourse() {
        return course;
    }

    public void setCourse(Lecture course) {
        this.course = course;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
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

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public void setClassroom_id(int classroom_id) {
        this.classroom_id = classroom_id;
    }

    public long getSchool_no() {
        return school_no;
    }

    public void setSchool_no(long school_no) {
        this.school_no = school_no;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public User(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSendStudentPicture() {
        return sendStudentPicture;
    }

    public void setSendStudentPicture(String sendStudentPicture) {
        this.sendStudentPicture = sendStudentPicture;
    }

    public String getSendTeacherPicture() {
        return sendTeacherPicture;
    }

    public void setSendTeacherPicture(String sendTeacherPicture) {
        this.sendTeacherPicture = sendTeacherPicture;
    }

    public String getGetStudentImage() {
        return getStudentImage;
    }

    public void setGetStudentImage(String getStudentImage) {
        this.getStudentImage = getStudentImage;
    }

    public String getGetTeacherImage() {
        return getTeacherImage;
    }

    public void setGetTeacherImage(String getTeacherImage) {
        this.getTeacherImage = getTeacherImage;
    }
}
