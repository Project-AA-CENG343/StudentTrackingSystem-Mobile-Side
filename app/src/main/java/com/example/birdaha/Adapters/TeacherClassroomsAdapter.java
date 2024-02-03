package com.example.birdaha.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.R;
import com.example.birdaha.Users.Teacher;
import com.example.birdaha.Utilities.TeacherClassroomsRecyclerViewInterface;

import java.util.List;

/**
 * The `TeacherClassroomsAdapter` class is a RecyclerView adapter designed for displaying
 * a list of teacher classrooms. It binds the data of `Classroom` objects to the corresponding
 * views in the RecyclerView.
 *
 * The adapter also provides a click interface through `TeacherClassroomsRecyclerViewInterface`
 * for handling item clicks on the teacher classrooms.
 */
public class TeacherClassroomsAdapter extends RecyclerView.Adapter<TeacherClassroomsAdapter.TeacherClassroomsViewHolder>{
    private final TeacherClassroomsRecyclerViewInterface teacherClassroomsRecyclerViewInterface;
    private Context context;
    private List<Classroom> teacherClassrooms;
    private Teacher teacher;

    /**
     * Constructs a new `TeacherClassroomsAdapter`.
     *
     * @param context                           The context in which the adapter is used.
     * @param teacherClassrooms                 The list of teacher classrooms to be displayed.
     * @param teacherClassroomsRecyclerViewInterface The interface for handling item clicks.
     */
    public TeacherClassroomsAdapter(Context context, List<Classroom> teacherClassrooms, TeacherClassroomsRecyclerViewInterface teacherClassroomsRecyclerViewInterface, Teacher teacher){
        this.context = context;
        this.teacherClassrooms = teacherClassrooms;
        this.teacherClassroomsRecyclerViewInterface = teacherClassroomsRecyclerViewInterface;
        this.teacher = teacher;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new `TeacherClassroomsViewHolder` that holds the View for each teacher classroom item.
     */
    @NonNull
    @Override
    public TeacherClassroomsAdapter.TeacherClassroomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeacherClassroomsAdapter.TeacherClassroomsViewHolder(LayoutInflater.from(context).inflate(R.layout.teacherclassrooms_view,parent,false),teacherClassroomsRecyclerViewInterface,teacher);

    }

    /**
     * Called to display the data at the specified position.
     *
     * @param holder   The ViewHolder that should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TeacherClassroomsAdapter.TeacherClassroomsViewHolder holder, int position) {
        Classroom current = teacherClassrooms.get(position);

        holder.name.setText(current.getName());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of teacher classrooms.
     */
    @Override
    public int getItemCount() {
        return teacherClassrooms.size();
    }

    /**
     * The `TeacherClassroomsViewHolder` class represents the ViewHolder for teacher classrooms.
     * It holds references to the views within the item layout and handles item click events.
     */
    public class TeacherClassroomsViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        /**
         * Constructs a new `TeacherClassroomsViewHolder`.
         *
         * @param itemView                           The View representing the teacher classroom item.
         * @param teacherClassroomsRecyclerViewInterface The interface for handling item clicks.
         */
        public TeacherClassroomsViewHolder(@NonNull View itemView, TeacherClassroomsRecyclerViewInterface teacherClassroomsRecyclerViewInterface, Teacher teacher) {
            super(itemView);

            name = itemView.findViewById(R.id.teacherClassrooms_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(teacherClassroomsRecyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            teacherClassroomsRecyclerViewInterface.onTeacherClassroomsItemClick(pos,itemView,teacher);
                        }
                    }
                }
            });
        }
    }
}
