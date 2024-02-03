package com.example.birdaha.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdaha.Adapters.ClassAnnouncementAdapter;
import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.General.AnnouncementsTeacher;
import com.example.birdaha.General.ClassAnnouncementModel;
import com.example.birdaha.General.UpdateRespond;
import com.example.birdaha.Helper.LocalDataManager;
import com.example.birdaha.R;
import com.example.birdaha.Users.Teacher;
import com.example.birdaha.Utilities.AnnouncementSerialize;
import com.example.birdaha.Utilities.ClassAnnouncementViewInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ClassRoomAnnouncementScreen extends AppCompatActivity implements ClassAnnouncementViewInterface {

    public interface MakeAnnouncement{

        @GET("api/v1/teacher/announcements/{classroomId}")
        Call<AnnouncementsTeacher> getAnnouncements(@Path("classroomId") int classroomId);

        @POST("/api/v1/announcement/add")
        Call<UpdateRespond> makeAnnouncement(@Body ClassAnnouncementModel classAnnouncementModel);

        @POST("api/v1/announcements/update")
        Call<UpdateRespond> updateAnnouncement(@Body ClassAnnouncementModel classAnnouncementModel);

        @GET("api/v1/announcement/delete/{announcementId}")
        Call<UpdateRespond> deleteHomework(@Path("announcementId") int announcementId);
    }

    SearchView search;

    ArrayList<ClassAnnouncementModel> classAnnouncementModels;

    Button addingAnnouncementButton;

    ClassAnnouncementAdapter classAnnouncementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_announcement_screen);
        // Get the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the title
            actionBar.setTitle("");
        }
        RecyclerView recyclerView = findViewById(R.id.caRecyclerView_classroom);

        search = findViewById(R.id.searchView_students);
        addingAnnouncementButton = findViewById(R.id.adding_announcement_button);

        Classroom classroom = null;

        Intent intent = getIntent();
        if(intent != null){
            classroom = (Classroom) intent.getSerializableExtra("classroom");

            classAnnouncementModels = AnnouncementSerialize.fromJson(LocalDataManager.getSharedPreference(getApplicationContext(), "announcement"+classroom.getName(), "")).arr;

            Teacher teacher = (Teacher) getIntent().getSerializableExtra("teacher");
            classAnnouncementAdapter = new ClassAnnouncementAdapter(ClassRoomAnnouncementScreen.this, classAnnouncementModels, ClassRoomAnnouncementScreen.this, teacher, true);
            recyclerView.setAdapter(classAnnouncementAdapter);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sinifdoktoruadmin.online/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(classroom != null){
            MakeAnnouncement getAnnouncement = retrofit.create(MakeAnnouncement.class);
            getAnnouncement.getAnnouncements(classroom.getClassroom_id()).enqueue(new Callback<AnnouncementsTeacher>() {
                @Override
                public void onResponse(Call<AnnouncementsTeacher> call, Response<AnnouncementsTeacher> response) {
                    if(response.isSuccessful() && response.body() != null){
                        AnnouncementsTeacher models = response.body();
                        classAnnouncementModels = (ArrayList<ClassAnnouncementModel>) models.getClassroomAnnouncements();
                        if(classAnnouncementModels.isEmpty()){
                        }

                        Teacher teacher = (Teacher) getIntent().getSerializableExtra("teacher");
                        classAnnouncementAdapter = new ClassAnnouncementAdapter(ClassRoomAnnouncementScreen.this, classAnnouncementModels, ClassRoomAnnouncementScreen.this,teacher,true);
                        recyclerView.setAdapter(classAnnouncementAdapter);
                    }
                    else{
                    }
                }

                @Override
                public void onFailure(Call<AnnouncementsTeacher> call, Throwable t) {
                }
            });
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                classAnnouncementAdapter.getFilter().filter(newText);
                return true;
            }
        });

        addingAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnnouncementDialog();
            }
        });

    }
    public void showAnnouncementDialog()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AnnouncementDialogFragment newFragment = new AnnouncementDialogFragment(classAnnouncementAdapter);

        Bundle args = new Bundle();
        args.putSerializable("teacher", getIntent().getSerializableExtra("teacher"));
        args.putSerializable("classroom", getIntent().getSerializableExtra("classroom"));
        newFragment.setArguments(args);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    public void onClassAnnouncementItemClick(ClassAnnouncementModel clickedItem, View view) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        View overlayView = inflater.inflate(R.layout.dialog_ann_detail, null);



        EditText title = overlayView.findViewById(R.id.announcement_detail_name);
        EditText details = overlayView.findViewById(R.id.announcement_detail_content);
        EditText teacherName = overlayView.findViewById(R.id.announcement_detail_teacher);


        title.setText(clickedItem.getTitle());
        details.setText(clickedItem.getDetails());
        teacherName.setText(clickedItem.getTeacher().getName());

        title.setEnabled(false);
        details.setEnabled(false);
        teacherName.setEnabled(false);


        builder.setView(overlayView);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClassAnnouncementEditClick(ClassAnnouncementModel clickedItem, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        View overlayView = inflater.inflate(R.layout.full_screen_announcement_adding_dialog,null);
        TextInputEditText title = overlayView.findViewById(R.id.lectureNameEditText);
        TextInputEditText details = overlayView.findViewById(R.id.add_announcement_content);

        TextView hw_dialog_title = overlayView.findViewById(R.id.fullscreen_ann_title);

        hw_dialog_title.setText("Duyuruyu Düzenle");

        title.setText(clickedItem.getTitle());
        details.setText(clickedItem.getDetails());

        builder.setView(overlayView);
        AlertDialog dialog = builder.create();
        dialog.show();

        ImageButton closeButton = overlayView.findViewById(R.id.fullscreen_dialog_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView actionButton = overlayView.findViewById(R.id.fullscreen_dialog_action);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedTitle = title.getText().toString();
                String updatedDetails = details.getText().toString();
                clickedItem.setTitle(updatedTitle);
                clickedItem.setDetails(updatedDetails);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://sinifdoktoruadmin.online/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MakeAnnouncement updateAnnouncement = retrofit.create(MakeAnnouncement.class);
                updateAnnouncement.updateAnnouncement(clickedItem).enqueue(new Callback<UpdateRespond>() {
                    @Override
                    public void onResponse(Call<UpdateRespond> call, Response<UpdateRespond> response) {
                        if(response.isSuccessful() && response.body() != null){
                            classAnnouncementAdapter.notifyDataSetChanged();
                            Intent intent = new Intent(ClassRoomAnnouncementScreen.this,ClassRoomAnnouncementScreen.class);
                            startActivity(intent);
                        }
                        else{
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateRespond> call, Throwable t) {
                    }
                });
                dialog.dismiss();
            }
        });
    }
}