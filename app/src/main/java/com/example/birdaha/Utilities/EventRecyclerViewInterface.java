package com.example.birdaha.Utilities;

import android.view.View;

/**
 * Interface for handling item click events in the Event RecyclerView.
 */
public interface EventRecyclerViewInterface {
    /**
     * Called when an item in the Event RecyclerView is clicked.
     *
     * @param position The position of the clicked item in the data set.
     * @param view     The View representing the clicked item.
     */
    void onEventItemClick(int position, View view);
}
