package com.example.birdaha.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdaha.General.HwModel;
import com.example.birdaha.General.StudentModel;
import com.example.birdaha.R;
import com.example.birdaha.Users.Student;
import com.example.birdaha.Utilities.HomeworkStudentsViewInterface;

import java.util.ArrayList;
import java.util.List;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    Context context;


    ArrayList<StudentModel> students;
    ArrayList<StudentModel> studentsFiltered;


    private final HomeworkStudentsViewInterface studentViewInterface;


    public StudentAdapter(Context context, ArrayList<StudentModel> students, HomeworkStudentsViewInterface studentViewInterface) {
        this.context = context;
        this.students = students;
        this.studentsFiltered = students;
        this.studentViewInterface = studentViewInterface;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row3, parent, false);
        return new StudentViewHolder(view, studentViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentModel currentStudent = students.get(position);
        holder.studentNameTextView.setText(currentStudent.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentViewInterface != null)
                {
                    int pos = students.indexOf(currentStudent);
                    if(pos != -1) {
                        try {
                            studentViewInterface.onHomeworkStudentItemClick(students.get(pos), v);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.values = studentsFiltered;
                    filterResults.count = studentsFiltered.size();
                }
                else{
                    String searchStr = constraint.toString().toLowerCase();
                    List<StudentModel> studentModel = new ArrayList<>();
                    for(StudentModel model : studentsFiltered){
                        if(model.getName().toLowerCase().contains(searchStr)){
                            studentModel.add(model);
                        }
                    }
                    filterResults.values = studentModel;
                    filterResults.count = studentModel.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                students = (ArrayList<StudentModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }




    /**
     * This class represents the ViewHolder for individual class announcement items.
     */
    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView studentNameTextView;
        CardView cardView;


        public StudentViewHolder(@NonNull View itemView, HomeworkStudentsViewInterface studentViewInterface) {
            super(itemView);

            // Initialize the textViewTitle variable with the view from the layout with id homework_detail_name
            studentNameTextView = itemView.findViewById(R.id.studentName);

            // Initialize the cardView variable with the view from the layout with id cardView2
            cardView = itemView.findViewById(R.id.cardView3);

            // Set a click listener on the cardView
        }
    }

}
