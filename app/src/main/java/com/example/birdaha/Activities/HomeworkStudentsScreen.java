package com.example.birdaha.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.example.birdaha.Adapters.StudentAdapter;
import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.General.ClassAnnouncementModel;
import com.example.birdaha.General.HomeworkResult;
import com.example.birdaha.General.HomeworkResultModel;
import com.example.birdaha.General.StudentModel;
import com.example.birdaha.General.UpdateRespond;
import com.example.birdaha.R;
import com.example.birdaha.Users.Student;
import com.example.birdaha.Utilities.HomeworkStudentsViewInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import android.view.View;
import android.widget.EditText;

import com.example.birdaha.Adapters.StudentAdapter;
import com.example.birdaha.General.ClassAnnouncementModel;
import com.example.birdaha.R;
import com.example.birdaha.Users.Student;
import com.example.birdaha.Utilities.HomeworkStudentsViewInterface;

import java.util.ArrayList;

/**
 * This class represents the HomeworkStudentsScreen activity.
 * It implements the HomeworkStudentsViewInterface.
 */
public class HomeworkStudentsScreen extends AppCompatActivity implements HomeworkStudentsViewInterface {

    interface Result{
        @GET("/api/v1/homework/result/{homeworkId}/{studentId}")
        Call<HomeworkResultModel> getResult(@Path("homeworkId") int homeworkId, @Path("studentId") int studentId);

        @POST("/api/v1/homework/result/update")
        Call<UpdateRespond> updateHomeworkGrade(@Body HomeworkResult hwResult);

        @POST("/api/v1/homework/result/add")
        Call<UpdateRespond> addHomeworkGrade(@Body HomeworkResult hwResult);


    }

    // List of students
    ArrayList<StudentModel> students;

    // Adapter for the student list
    private StudentAdapter studentAdapter;
    private int homeworkId;
    private Retrofit retrofit;

    SearchView search;

    /**
     * This method is called when the activity is starting.
     * It sets the content view, initializes the RecyclerView and sets the students.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_students_screen);
        RecyclerView recyclerView = findViewById(R.id.RecyclerView_students);
        search = findViewById(R.id.searchView_students);

        // Get the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the title
            actionBar.setTitle("");
        }

        Classroom classroom = (Classroom) getIntent().getSerializableExtra("classroom");
        students = (ArrayList<StudentModel>) getIntent().getSerializableExtra("students");
        students.sort(new Comparator<StudentModel>() {
            @Override
            public int compare(StudentModel o1, StudentModel o2) {
                return o1.compareTo(o2);
            }
        });

        homeworkId = getIntent().getIntExtra("homeworkId", -1);

        //setStudents();
        studentAdapter = new StudentAdapter(this, students,this);
        recyclerView.setAdapter(studentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Set a listener for the SearchView to handle query text changes
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studentAdapter.getFilter().filter(newText);
                return true;
            }
        });

        // Set a listener for closing the SearchView
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }

    /**
     * This method is called when a student item in the RecyclerView is clicked.
     * It inflates a dialog for entering grade and note, and shows the dialog.
     * @param position The position of the clicked item.
     * @param view The view within the RecyclerView that was clicked.
     */
    @Override
    public void onHomeworkStudentItemClick(StudentModel student, View view) throws InterruptedException {
        // Inflate the dialog_grade_entry.xml layout
        View dialogView = getLayoutInflater().inflate(R.layout.overlay_student_grade, null);
        final EditText editTextGrade = dialogView.findViewById(R.id.editTextGrade);
        final EditText editTextNote = dialogView.findViewById(R.id.editTextNote);

        HomeworkResult newResult = new HomeworkResult();
        newResult.setHomework_id(homeworkId);
        newResult.setStudent_id(student.getStudent_id());

        retrofit = new Retrofit.Builder()
                .baseUrl("http://sinifdoktoruadmin.online/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Result resultRequest = retrofit.create(Result.class);
        resultRequest.getResult(homeworkId, student.getStudent_id()).enqueue(new Callback<HomeworkResultModel>(){

            @Override
            public void onResponse(Call<HomeworkResultModel> call, Response<HomeworkResultModel> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    HomeworkResultModel model = response.body();
                    HomeworkResult result = model.getResult();
                    if(result != null)
                    {
                        editTextGrade.setText(Integer.toString(result.getGrade()));
                        editTextNote.setText(result.getNote_for_parent());

                        newResult.setGradedBefore(true);
                        newResult.setHomework_result_id(result.getHomework_result_id());
                    }
                }
            }

            @Override
            public void onFailure(Call<HomeworkResultModel> call, Throwable t) {

            }
        });

        Thread.sleep(100);

        // Create and show the AlertDialog
        new AlertDialog.Builder(this,R.style.CustomAlertDialog)
                .setTitle("Not ve Puan Giriniz")
                .setView(dialogView)
                .setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String grade = editTextGrade.getText().toString().trim();

                        if(!isGradeValid(grade))
                        {
                            showAlertDialog();
                            return;
                        }

                        int gradeInt = Integer.valueOf(grade);
                        String note = editTextNote.getText().toString().trim();

                        newResult.setGrade(gradeInt);
                        newResult.setNote_for_parent(note);

                        retrofit = new Retrofit.Builder()
                                .baseUrl("http://sinifdoktoruadmin.online/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        if(newResult.isGradedBefore())
                        {

                            Result updateGrade = retrofit.create(Result.class);
                            updateGrade.updateHomeworkGrade(newResult).enqueue(new Callback<UpdateRespond>() {
                                @Override
                                public void onResponse(Call<UpdateRespond> call, Response<UpdateRespond> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        Log.d("Response",new Gson().toJson(response.body()));
                                    }
                                    else{
                                        Log.d("ResponseError",new Gson().toJson(response.body()));
                                        Log.d("Response",String.valueOf(response.code()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<UpdateRespond> call, Throwable t) {
                                    Log.d("Error",t.getMessage());
                                }
                            });

                        }
                        else
                        {
                            Result addGrade = retrofit.create(Result.class);
                            addGrade.addHomeworkGrade(newResult).enqueue(new Callback<UpdateRespond>() {
                                @Override
                                public void onResponse(Call<UpdateRespond> call, Response<UpdateRespond> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        Log.d("Response",new Gson().toJson(response.body()));
                                    }
                                    else{
                                        Log.d("ResponseError",new Gson().toJson(response.body()));
                                        Log.d("Response",String.valueOf(response.code()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<UpdateRespond> call, Throwable t) {
                                    Log.d("Error",t.getMessage());
                                }
                            });

                        }

                    }
                })
                .show();
    }


    private boolean isGradeValid(String grade)
    {
        int gradeInt;
        try {
            gradeInt = Integer.valueOf(grade);

        }catch (NumberFormatException e)
        {
            return false;
        }

        return gradeInt >= 0 && gradeInt <= 100;

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.CustomAlertDialog);

        // Set the dialog title and message
        builder.setTitle("Hata")
                .setMessage("Girişleri kontrol ediniz.");

        // Add a button to the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the positive button click event here
                dialog.dismiss(); // Close the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}