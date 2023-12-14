package com.example.birdaha.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdaha.Adapters.HomeworkAdapter;
import com.example.birdaha.General.HwModel;
import com.example.birdaha.R;
import com.example.birdaha.Utilities.ClassroomHomeworkViewInterface;

import java.util.ArrayList;

/**
 * This activity displays a list of homework modules.
 */
public class HomeWorkScreen extends AppCompatActivity implements ClassroomHomeworkViewInterface {
    SearchView search;
    ArrayList<HwModel> hwModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work_screen);

        search = findViewById(R.id.searchView_homework);

        RecyclerView recyclerView = findViewById(R.id.hwRecyclerView);

        setHwModules();
        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(this, hwModels,this);
        recyclerView.setAdapter(homeworkAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        View baselineFilterView = findViewById(R.id.filterView);
        baselineFilterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverlay();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeworkAdapter.search(newText);
                return true;
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                homeworkAdapter.restoreOriginalList();
                return false;
            }
        });

    }

    private void setHwModules(){

        String[] titles = getResources().getStringArray(R.array.Homeworks);
        String[] infos = getResources().getStringArray(R.array.Homeworks_info);
        for (int i = 0; i < titles.length; i++) {
            hwModels.add(new HwModel(titles[i], infos[i]));
        }

    }


    // This method is called when the user clicks on the filter icon
    private void showOverlay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View overlayView = inflater.inflate(R.layout.filter_overlay, null);
        builder.setView(overlayView);

        AlertDialog dialog = builder.create();

        // Set the dialog window attributes to make it a small overlay
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER;
        dialog.getWindow().setAttributes(layoutParams);

        // Find the checkboxes in the overlay layout
        CheckBox checkBox1 = overlayView.findViewById(R.id.checkBox);
        CheckBox checkBox2 = overlayView.findViewById(R.id.checkBox2);

        // Add any additional customization or logic to the checkboxes here

        dialog.show();
    }

    @Override
    public void onClassroomHomeworkItemClick(int position, View view) {
        HwModel current = hwModels.get(position);
        // Create an AlertDialog.Builder object with the context of the itemView
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        // Create a LayoutInflater object from the itemView's context
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        // Inflate the overlay_layout.xml file into a View object
        View overlayView = inflater.inflate(R.layout.homework_overlay_layout, null);
        TextView title = overlayView.findViewById(R.id.homework_detail_name);
        TextView detail = overlayView.findViewById(R.id.homework_detail_info);
        title.setText(current.getTitle());
        detail.setText(current.getInfo());
        // Set the inflated view as the custom view for the AlertDialog
        builder.setView(overlayView);

        // Create an AlertDialog object from the builder
        AlertDialog dialog = builder.create();

        // Show the AlertDialog
        dialog.show();
    }
}