package com.example.birdaha.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.birdaha.Adapters.GeneralAnnouncementAdapter;
import com.example.birdaha.General.GeneralAnnouncement;
import com.example.birdaha.R;
import com.example.birdaha.Utilities.GeneralAnnouncementViewInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment displaying a list of general announcements using a vertical RecyclerView.
 * Implements the GeneralAnnouncementViewInterface to handle item click events.
 */
public class GeneralAnnouncementFragment extends Fragment implements GeneralAnnouncementViewInterface {

    // RecyclerView to display general announcements
    private RecyclerView generalAnnouncementrecyclerView;

    // List of general announcements
    private List<GeneralAnnouncement> generalAnnouncements = new ArrayList<>();

    /**
     * Empty constructor for the GeneralAnnouncementFragment.
     */
    public GeneralAnnouncementFragment(){
        // Required empty public constructor
    }

    public GeneralAnnouncementFragment(List<GeneralAnnouncement> generalAnnouncements){
        this.generalAnnouncements = generalAnnouncements;
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
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_announcement, container, false);

        generalAnnouncementrecyclerView = view.findViewById(R.id.general_announcement_recyclerView);
        generalAnnouncementrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

        /*generalAnnouncements.add(new GeneralAnnouncement("Duyuru 1", "Duyuru 1 içeriği"));
        generalAnnouncements.add(new GeneralAnnouncement("Duyuru 2", "Duyuru 2 içeriği"));
        generalAnnouncements.add(new GeneralAnnouncement("Duyuru 3", "Duyuru 3 içeriği"));
        generalAnnouncements.add(new GeneralAnnouncement("Duyuru 4", "Duyuru 4 içeriği"));
        generalAnnouncements.add(new GeneralAnnouncement("Duyuru 5", "Duyuru 5 içeriği"));
        generalAnnouncements.add(new GeneralAnnouncement("Duyuru 6", "Duyuru 6 içeriği"));
        generalAnnouncements.add(new GeneralAnnouncement("Duyuru 7", "Duyuru 7 içeriği"));
        generalAnnouncements.add(new GeneralAnnouncement("Duyuru 8", "Duyuru 8 içeriği"));*/

        GeneralAnnouncementAdapter adapter = new GeneralAnnouncementAdapter(getActivity(),generalAnnouncements,this);
        generalAnnouncementrecyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onGeneralAnnouncementItemClick(int position, View view) {
        GeneralAnnouncement current = generalAnnouncements.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        View overlayView = inflater.inflate(R.layout.announcement_detail_overlay,null);
        TextView title = overlayView.findViewById(R.id.announcement_detail_title);
        TextView detail = overlayView.findViewById(R.id.announcement_detail);

        title.setText(current.getTitle());
        detail.setText(current.getDetails());

        title.setEnabled(false);
        detail.setEnabled(false);

        builder.setView(overlayView);

        AlertDialog dialog = builder.create();
        dialog.show();
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
    }
}