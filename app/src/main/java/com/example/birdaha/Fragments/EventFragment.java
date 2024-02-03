package com.example.birdaha.Fragments;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birdaha.Adapters.EventAdapter;
import com.example.birdaha.General.Event;
import com.example.birdaha.R;
import com.example.birdaha.Utilities.EventRecyclerViewInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment displaying a list of events using a horizontal RecyclerView.
 * Implements the EventRecyclerViewInterface to handle item click events.
 */
public class EventFragment extends Fragment implements EventRecyclerViewInterface {
    // RecyclerView to display events
    private RecyclerView eventRecyclerView;

    // List of events
    private List<Event> events = new ArrayList<>();

    /**
     * Empty constructor for the EventFragment.
     */
    public EventFragment(){
        // Required public empty constructor
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
        View view = inflater.inflate(R.layout.fragment_event,container,false);

        eventRecyclerView = view.findViewById(R.id.event_recyclerView);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        /*events.add(new Event("Etkinlik 1",R.drawable.img,"Etkinlik 1 içeriği"));
        events.add(new Event("Etkinlik 2", R.drawable.img_1,"Etkinlik 2 içeriği"));*/

        EventAdapter adapter = new EventAdapter(getActivity(),events,this);
        eventRecyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Callback method invoked when an event item is clicked in the RecyclerView.
     * Displays an AlertDialog with detailed information about the clicked event.
     *
     * @param position The position of the clicked item in the RecyclerView.
     * @param view     The View that was clicked.
     */
    @Override
    public void onEventItemClick(int position, View view) {
        Event currentEvent = events.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        View overlayView = inflater.inflate(R.layout.event_detail_overlay, null);
        ImageView imageView = overlayView.findViewById(R.id.event_detail_image);
        TextView detail = overlayView.findViewById(R.id.event_detail_detail);
        EditText title = overlayView.findViewById(R.id.event_detail_title);
        imageView.setImageResource(currentEvent.getImageResource());
        detail.setText(currentEvent.getDetails());
        title.setText(currentEvent.getTitle());

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