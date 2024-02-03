package com.example.birdaha.Utilities;

import android.view.View;
import com.example.birdaha.General.ClassAnnouncementModel;

/**
 * Interface for handling item click events in the General Announcement RecyclerView.
 */
public interface ClassAnnouncementViewInterface {
    /**
     * Called when an item in the General Announcement RecyclerView is clicked.
     *
     * @param position The position of the clicked item in the data set.
     * @param view     The View representing the clicked item.
     */
    void onClassAnnouncementItemClick(ClassAnnouncementModel clickedItem, View view);
    void onClassAnnouncementEditClick(ClassAnnouncementModel clickedItem, View view);
}
