package com.example.birdaha.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdaha.General.HwModel;
import com.example.birdaha.R;
import com.example.birdaha.Utilities.ClassroomHomeworkViewInterface;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StudentHomeworkAdapter extends RecyclerView.Adapter<StudentHomeworkAdapter.HomeworkViewHolder> implements Filterable{
    private final ClassroomHomeworkViewInterface homeworkViewInterface;
    Context context;
    ArrayList<HwModel> hwModels;
    ArrayList<HwModel> hwModelsFiltered;

    public StudentHomeworkAdapter(Context context, ArrayList<HwModel> hwModels, ClassroomHomeworkViewInterface homeworkViewInterface){
        this.context = context;
        this.hwModels = hwModels;
        this.hwModelsFiltered = hwModels;
        this.homeworkViewInterface = homeworkViewInterface;
    }

    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_student_homework, parent, false);
        return new HomeworkViewHolder(view, homeworkViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkViewHolder holder, int position) {
        ZoneId turkeyZone = ZoneId.of("Europe/Istanbul");
        LocalDate localDate = LocalDate.now(turkeyZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = localDate.format(formatter);

        HwModel current = hwModels.get(position);
        if(current.getResult() != null)
            holder.grade.setText(""+current.getResult().getGrade());
        else
            holder.grade.setText("Not: /");

        holder.textViewname.setText(current.getTitle());
        holder.textViewname.setBackground((current.getDue_date().compareTo(formattedDateTime) < 0) ? context.getDrawable(R.drawable.darkgray_round_background) : context.getDrawable(R.drawable.blue_round_background));

        holder.textViewname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeworkViewInterface != null) {
                    int pos = hwModels.indexOf(current);
                    if (pos != -1) {
                        homeworkViewInterface.onClassroomHomeworkItemClick(hwModels.get(pos), v);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return hwModels.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.values = hwModelsFiltered;
                    filterResults.count = hwModelsFiltered.size();
                }
                else{
                    String searchStr = constraint.toString().toLowerCase();
                    List<HwModel> hwmodel = new ArrayList<>();
                    for(HwModel model : hwModelsFiltered){
                        if(model.getTitle().toLowerCase().contains(searchStr)){
                            hwmodel.add(model);
                        }
                    }
                    filterResults.values = hwmodel;
                    filterResults.count = hwmodel.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                hwModels = (ArrayList<HwModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    public static class HomeworkViewHolder extends RecyclerView.ViewHolder{

        TextView textViewname;
        TextView grade;

        /**
         * Constructor for HomeworkViewHolder.
         *
         * @param itemView              The view item for the homework item.
         * @param homeworkViewInterface The interface to handle homework item clicks.
         */

        public HomeworkViewHolder(@NonNull View itemView, ClassroomHomeworkViewInterface homeworkViewInterface) {
            super(itemView);

            // Initialize the textViewTitle variable with the view from the layout with id textView
            //textViewTitle = itemView.findViewById(R.id.studentName);

            // Initialize the cardView variable with the view from the layout with id cardView
            textViewname = itemView.findViewById(R.id.student_homework_title);
            grade = itemView.findViewById(R.id.student_hw_grade);

        }
    }
}