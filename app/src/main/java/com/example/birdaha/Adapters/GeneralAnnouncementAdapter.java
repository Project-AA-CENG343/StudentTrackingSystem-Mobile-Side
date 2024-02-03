package com.example.birdaha.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdaha.General.GeneralAnnouncement;
import com.example.birdaha.R;
import com.example.birdaha.Utilities.GeneralAnnouncementViewInterface;

import java.util.List;

/**
 * Adapter class for the RecyclerView that displays a list of general announcements.
 */
public class GeneralAnnouncementAdapter extends RecyclerView.Adapter<GeneralAnnouncementAdapter.GeneralAnnouncementViewHolder>{
    private final GeneralAnnouncementViewInterface generalAnnouncementViewInterface;
    private Context context;
    private List<GeneralAnnouncement> generalAnnouncements;

    /**
     * Constructor for the GeneralAnnouncementAdapter.
     *
     * @param context                       The context of the calling activity or fragment.
     * @param generalAnnouncements         The list of general announcements to be displayed in the RecyclerView.
     * @param generalAnnouncementViewInterface The interface to handle item click events.
     */
    public GeneralAnnouncementAdapter(Context context, List<GeneralAnnouncement> generalAnnouncements, GeneralAnnouncementViewInterface generalAnnouncementViewInterface){
        this.context = context;
        this.generalAnnouncements = generalAnnouncements;
        this.generalAnnouncementViewInterface = generalAnnouncementViewInterface;
    }

    /**
     * Inflates the layout for each item in the RecyclerView.
     *
     * @param parent   The parent view group.
     * @param viewType The type of view.
     * @return A new instance of the GeneralAnnouncementViewHolder.
     */
    @NonNull
    @Override
    public GeneralAnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GeneralAnnouncementViewHolder(LayoutInflater.from(context).inflate(R.layout.general_announcement_view,parent,false),generalAnnouncementViewInterface);
    }

    /**
     * Binds the data to the views in each ViewHolder.
     *
     * @param holder   The ViewHolder to bind the data to.
     * @param position The position of the item in the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull GeneralAnnouncementViewHolder holder, int position) {
        GeneralAnnouncement currentGeneralAnnouncement = generalAnnouncements.get(position);

        holder.title.setText(currentGeneralAnnouncement.getTitle());
    }

    /**
     * Returns the total number of items in the data set.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return generalAnnouncements.size();
    }


    /**
     * ViewHolder class representing each item in the RecyclerView.
     */
    public class GeneralAnnouncementViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        /**
         * Constructor for the GeneralAnnouncementViewHolder.
         *
         * @param itemView                       The view for each item in the RecyclerView.
         * @param generalAnnouncementViewInterface The interface to handle item click events.
         */
        public GeneralAnnouncementViewHolder(@NonNull View itemView, GeneralAnnouncementViewInterface generalAnnouncementViewInterface) {
            super(itemView);

            title = itemView.findViewById(R.id.general_announcement_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(generalAnnouncementViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            generalAnnouncementViewInterface.onGeneralAnnouncementItemClick(pos, itemView);
                        }
                    }
                }
            });
        }
    }
}
