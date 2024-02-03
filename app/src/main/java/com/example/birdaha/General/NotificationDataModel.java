package com.example.birdaha.General;

import com.google.gson.Gson;

import java.util.ArrayList;

public class NotificationDataModel {

    private ArrayList<StudentSharedPrefModel> students = new ArrayList<>();

    public ArrayList<StudentSharedPrefModel> getStudents() {
        return students;
    }

    public void addOrUpdateStudents(StudentSharedPrefModel student)
    {
        StudentSharedPrefModel wantedStudent = getStudent(student.getStudentId());

        if(wantedStudent == null)
            students.add(student);
        else
        {
            wantedStudent.setStudentId(student.getStudentId());
            wantedStudent.setLastHomeworkId(student.getLastHomeworkId());
            wantedStudent.setLastAnnouncementId(student.getLastAnnouncementId());
        }
    }

    public StudentSharedPrefModel getOrDefault(int studentId, String classroomName)
    {
        StudentSharedPrefModel student = getStudent(studentId);

        if(student == null)
            return new StudentSharedPrefModel(-1, -1, studentId, classroomName);

        return student;
    }

    private StudentSharedPrefModel getStudent(int studentId)
    {
        for(StudentSharedPrefModel student : students)
            if(student.getStudentId() == studentId)
                return student;

        return null;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static String getDefaultJson() {
        NotificationDataModel defaultData = new NotificationDataModel();
        return defaultData.toJson();
    }

    public static NotificationDataModel fromJson(String notificationDataModelJson)
    {
        return new Gson().fromJson(notificationDataModelJson, NotificationDataModel.class);
    }


}
