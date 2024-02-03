package com.example.birdaha.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdaha.General.Event;
import com.example.birdaha.R;
import com.example.birdaha.Utilities.EventRecyclerViewInterface;

import java.util.List;

/**
 * Adapter class for the RecyclerView that displays a list of events.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final EventRecyclerViewInterface eventRecyclerViewInterface;
    private Context context;
    private List<Event> events;

    /**
     * Constructor for the EventAdapter.
     *
     * @param context                   The context of the calling activity or fragment.
     * @param events                    The list of events to be displayed in the RecyclerView.
     * @param eventRecyclerViewInterface The interface to handle item click events.
     */
    public EventAdapter(Context context, List<Event> events, EventRecyclerViewInterface eventRecyclerViewInterface){
        this.context = context;
        this.events = events;
        this.eventRecyclerViewInterface = eventRecyclerViewInterface;
    }

    /**
     * Inflates the layout for each item in the RecyclerView.
     *
     * @param parent   The parent view group.
     * @param viewType The type of view.
     * @return A new instance of the EventViewHolder.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.events_view,parent,false),eventRecyclerViewInterface);
    }

    /**
     * Binds the data to the views in each ViewHolder.
     *
     * @param holder   The ViewHolder to bind the data to.
     * @param position The position of the item in the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent =events.get(position);

        holder.image.setImageResource(currentEvent.getImageResource());
    }

    /**
     * Returns the total number of items in the data set.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }


    /**
     * ViewHolder class representing each item in the RecyclerView.
     */
    public class EventViewHolder extends RecyclerView.ViewHolder{
        ImageView image;

        /**
         * Constructor for the EventViewHolder.
         *
         * @param itemView                     The view for each item in the RecyclerView.
         * @param eventRecyclerViewInterface   The interface to handle item click events.
         */
        public EventViewHolder(@NonNull View itemView, EventRecyclerViewInterface eventRecyclerViewInterface) {
            super(itemView);

            image = itemView.findViewById(R.id.event_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(eventRecyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            eventRecyclerViewInterface.onEventItemClick(pos, itemView);
                        }
                    }
                }
            });
        }
    }
}
