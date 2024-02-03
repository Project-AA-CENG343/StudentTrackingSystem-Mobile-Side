package com.example.birdaha.Utilities;

import android.view.View;

import com.example.birdaha.Users.Teacher;

/**
 * The {@code TeacherClassroomsRecyclerViewInterface} interface provides a contract for handling
 * item click events within a RecyclerView displaying teacher classrooms.
 */
public interface TeacherClassroomsRecyclerViewInterface {
    /**
     * Called when a teacher classroom item is clicked in the RecyclerView.
     *
     * @param position The position of the clicked item in the RecyclerView.
     * @param view     The View that was clicked.
     */
    void onTeacherClassroomsItemClick(int position, View view, Teacher teacher);
}
