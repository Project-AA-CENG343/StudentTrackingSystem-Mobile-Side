package com.example.birdaha.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdaha.Activities.ClassRoomAnnouncementScreen;
import com.example.birdaha.General.ClassAnnouncementModel;
import com.example.birdaha.General.UpdateRespond;
import com.example.birdaha.R;
import com.example.birdaha.Users.Teacher;
import com.example.birdaha.Utilities.ClassAnnouncementViewInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClassAnnouncementAdapter extends RecyclerView.Adapter<ClassAnnouncementAdapter.ClassAnnouncementViewHolder> implements Filterable {

    Context context;
    private final ClassAnnouncementViewInterface classAnnouncementViewInterface;
    public ArrayList<ClassAnnouncementModel> classAnnouncementModels;
    ArrayList<ClassAnnouncementModel> classAnnouncementModelsFiltered;
    private Teacher teacher;

    private boolean isTeacher;

    public ClassAnnouncementAdapter(Context context, ArrayList<ClassAnnouncementModel> classAnnouncementModels, ClassAnnouncementViewInterface classAnnouncementViewInterface, Teacher teacher, boolean isTeacher) {
        this.context = context;
        this.classAnnouncementModels = classAnnouncementModels;
        this.classAnnouncementModelsFiltered = classAnnouncementModels;
        this.classAnnouncementViewInterface = classAnnouncementViewInterface;
        this.teacher = teacher;
        this.isTeacher = isTeacher;
    }

    public ArrayList<ClassAnnouncementModel> getClassAnnouncementModels() {
        return classAnnouncementModels;
    }

    public void setClassAnnouncementModels(ArrayList<ClassAnnouncementModel> classAnnouncementModels) {
        this.classAnnouncementModels = classAnnouncementModels;
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ClassAnnouncementViewHolder that holds a View for class announcement items.
     */
    @NonNull
    @Override
    public ClassAnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_teacher_announcement, parent, false);
        return new ClassAnnouncementViewHolder(view, classAnnouncementViewInterface);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ClassAnnouncementViewHolder holder, int position) {
        ClassAnnouncementModel current = classAnnouncementModels.get(position);
        holder.textViewName.setText(current.getTitle());
        holder.bind(current, isTeacher);
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classAnnouncementViewInterface != null) {
                    int pos = classAnnouncementModels.indexOf(current);
                    classAnnouncementViewInterface.onClassAnnouncementItemClick(classAnnouncementModels.get(pos), v);
                }
            }
        });

        if (teacher != null) {
            if (current.getTeacher_id() == teacher.getTeacher_id()) {
                holder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (classAnnouncementViewInterface != null) {
                            int pos = classAnnouncementModels.indexOf(current);
                            if (pos != -1) {
                                classAnnouncementViewInterface.onClassAnnouncementEditClick(classAnnouncementModels.get(pos), v);
                            }
                        }
                    }
                });
            }
        }

        if (teacher != null) {
            if (current.getTeacher_id() == teacher.getTeacher_id()) {
                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Duyuruyu Sil");
                        builder.setMessage("Duyuruyu silmek istediğinize emin misiniz?");
                        builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://sinifdoktoruadmin.online/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                ClassRoomAnnouncementScreen.MakeAnnouncement deleteHomework = retrofit.create(ClassRoomAnnouncementScreen.MakeAnnouncement.class);
                                deleteHomework.deleteHomework(current.getAnnouncement_id()).enqueue(new Callback<UpdateRespond>() {
                                    @Override
                                    public void onResponse(Call<UpdateRespond> call, Response<UpdateRespond> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            Toast.makeText(context, "Duyuru başarıyla silindi!", Toast.LENGTH_SHORT).show();
                                            classAnnouncementModels.remove(current);
                                            notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(context, "Hata oluştu!" + response.code(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UpdateRespond> call, Throwable t) {
                                        //Log.d("Error", t.getMessage());
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                });
            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of class announcement items.
     */
    @Override
    public int getItemCount() {
        return classAnnouncementModels.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.values = classAnnouncementModelsFiltered;
                    filterResults.count = classAnnouncementModelsFiltered.size();
                } else {
                    String searchStr = constraint.toString().toLowerCase();
                    List<ClassAnnouncementModel> classAnnouncementModels1 = new ArrayList<>();
                    for (ClassAnnouncementModel model : classAnnouncementModelsFiltered) {
                        if (model.getTitle().toLowerCase().contains(searchStr)) {
                            classAnnouncementModels1.add(model);
                        }
                    }
                    filterResults.values = classAnnouncementModels1;
                    filterResults.count = classAnnouncementModels1.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                classAnnouncementModels = (ArrayList<ClassAnnouncementModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    /**
     * This class represents the ViewHolder for individual class announcement items.
     */
    public static class ClassAnnouncementViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        ImageButton editButton;
        ImageButton deleteButton;


        public ClassAnnouncementViewHolder(@NonNull View itemView, ClassAnnouncementViewInterface classAnnouncementViewInterface) {
            super(itemView);

            // Initialize the textViewTitle variable with the view from the layout with id textView
            textViewName = itemView.findViewById(R.id.announcement_title);
            editButton = itemView.findViewById(R.id.edit_icon_button);
            deleteButton = itemView.findViewById(R.id.delete_icon_button);

        }


        void bind(ClassAnnouncementModel model, boolean isTeacher) {
            if (isTeacher) {
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
        }


    }
}
