package com.example.birdaha.Utilities;

import android.view.View;

/**
 * Interface for handling item click events in the General Announcement RecyclerView.
 */
public interface GeneralAnnouncementViewInterface {
    /**
     * Called when an item in the General Announcement RecyclerView is clicked.
     *
     * @param position The position of the clicked item in the data set.
     * @param view     The View representing the clicked item.
     */
    void onGeneralAnnouncementItemClick(int position, View view);
}
