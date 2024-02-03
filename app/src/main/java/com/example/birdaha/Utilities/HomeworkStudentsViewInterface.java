package com.example.birdaha.Utilities;

import android.view.View;

import com.example.birdaha.General.StudentModel;

public interface HomeworkStudentsViewInterface {
    void onHomeworkStudentItemClick(StudentModel clickedItem, View view) throws InterruptedException;
}
