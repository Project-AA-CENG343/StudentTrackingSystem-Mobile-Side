package com.example.birdaha.Interface;

import androidx.fragment.app.Fragment;

/**
 * Defines a contract for managing fragment navigation within an app.
 */
public interface NavigationManager {

    /**
     * Displays the provided fragment within the app's UI.
     *
     * @param fragment The fragment to be displayed.
     * @param addToBackStack True if the fragment transaction should be added to the back stack, otherwise false.
     */
    void showFragment(Fragment fragment, boolean b);
}
