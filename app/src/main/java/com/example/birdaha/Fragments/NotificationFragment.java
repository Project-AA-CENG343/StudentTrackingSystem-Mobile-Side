package com.example.birdaha.Fragments;

import static androidx.fragment.app.DialogFragment.STYLE_NORMAL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.birdaha.R;

/**
 * The NotificationFragment class represents a fragment displaying notifications.
 * This fragment inflates a layout to display notifications and handles any necessary view setup.
 */
public class NotificationFragment extends Fragment {

    /**
     * Key to retrieve the title content.
     */
    private static final String KEY_TITLE="Content";

    /**
     * Empty constructor for the NotificationFragment.
     */
    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of the NotificationFragment.
     *
     * @param param1 Title content to be displayed.
     * @return A new instance of NotificationFragment.
     */
    public static NotificationFragment newInstance(String param1) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is created.
     *
     * @param savedInstanceState A Bundle containing the saved state of the fragment,
     *                            or null if there is no saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the saved state of the fragment,
     *                           or null if there is no saved state.
     * @return The root View of the fragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Set the custom full-screen dialog theme
        return inflater.inflate(R.layout.preferences, container, false);
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored
     * in to the view. This gives subclasses a chance to initialize themselves once they know their view
     * hierarchy has been completely created.
     *
     * @param view               The View returned by onCreateView().
     * @param savedInstanceState A Bundle containing the saved state of the fragment,
     *                           or null if there is no saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*String title = getArguments().getString(KEY_TITLE);
        ((TextView)view.findViewById(R.id.title)).setText(title);*/


    }
}