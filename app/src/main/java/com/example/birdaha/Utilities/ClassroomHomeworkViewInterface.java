package com.example.birdaha.Utilities;

import android.view.View;

import com.example.birdaha.General.HwModel;

public interface ClassroomHomeworkViewInterface {
    void onClassroomHomeworkItemClick(HwModel clickedItem, View view);
    void onClassroomHomeworkEditClick(HwModel clickedItem, View view);
}
