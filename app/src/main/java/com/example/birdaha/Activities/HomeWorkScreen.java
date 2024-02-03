package com.example.birdaha.Activities;

import android.app.AlertDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.birdaha.Adapters.HomeworkAdapter;
import com.example.birdaha.Adapters.StudentHomeworkAdapter;
import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.General.HomeworkResult;
import com.example.birdaha.General.HomeworkResultModel;
import com.example.birdaha.General.HomeworksStudent;
import com.example.birdaha.General.HwModel;
import com.example.birdaha.General.NotificationDataModel;
import com.example.birdaha.General.NotificationModel;
import com.example.birdaha.General.StudentModel;
import com.example.birdaha.General.StudentSharedPrefModel;
import com.example.birdaha.Helper.LocalDataManager;
import com.example.birdaha.R;
import com.example.birdaha.Users.Student;
import com.example.birdaha.Utilities.ClassroomHomeworkViewInterface;
import com.example.birdaha.Utilities.HomeworkSerialize;
import com.example.birdaha.Utilities.NotificationService.NotificationJobService;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * This activity displays a list of homework modules.
 */
public class HomeWorkScreen extends AppCompatActivity implements ClassroomHomeworkViewInterface {

    interface GetHomework{
        @GET("api/v1/homeworks/{classroomId}/{studentId}")
        Call<HomeworksStudent> getHomeworks(@Path("classroomId") int classroomId,
                                            @Path("studentId") int studentId);
    }

    interface Result {
        @GET("/api/v1/homework/result/{homeworkId}/{studentId}")
        Call<HomeworkResultModel> getResult(@Path("homeworkId") int homeworkId, @Path("studentId") int studentId);

    }


    SearchView search;
    ArrayList<HwModel> hwModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private StudentHomeworkAdapter homeworkAdapter;
    private Context context;
    private ClassroomHomeworkViewInterface homeworkViewInterface;

    private ArrayList<HwModel> expiredHws;
    private ArrayList<HwModel> ongoingHws;

    private AlertDialog filterDialog = null;
    private Student student = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work_screen);

        // Get the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the title
            actionBar.setTitle("");
        }

        search = findViewById(R.id.searchView_homework);

        recyclerView = findViewById(R.id.hwRecyclerView);
        expiredHws = new ArrayList<>();
        ongoingHws = new ArrayList<>();

        context = this;
        homeworkViewInterface = this;

        Classroom classroom = null;

        Intent intent = getIntent();
        if (intent != null) {
            student = (Student) intent.getSerializableExtra("student");

            classroom = (Classroom) intent.getSerializableExtra("classroom");


            NotificationModel notificationModel = NotificationJobService.fetchNotification(student.getStudent_id());



            String studentsArrayJson = LocalDataManager.getSharedPreference(context, "studentsArray", NotificationDataModel.getDefaultJson());
            NotificationDataModel notificationDataModel = NotificationDataModel.fromJson(studentsArrayJson);
            StudentSharedPrefModel studentSharedPref = notificationDataModel.getOrDefault(student.getStudent_id(), student.getClassroom().getName());

            studentSharedPref.setLastHomeworkId(notificationModel.getHomeworkId());

            notificationDataModel.addOrUpdateStudents(studentSharedPref);

            LocalDataManager.setSharedPreference(context, "studentsArray", notificationDataModel.toJson());

            hwModels = HomeworkSerialize.fromJson(LocalDataManager.getSharedPreference(context, "homework"+classroom.getName(), "")).arr;

            sortListByDate(hwModels);
            homeworkAdapter = new StudentHomeworkAdapter(context, hwModels, homeworkViewInterface);
            recyclerView.setAdapter(homeworkAdapter);

        }

        Log.d("classid",String.valueOf(classroom.getClassroom_id()));
        Log.d("studentid",String.valueOf(student.getStudent_id()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sinifdoktoruadmin.online/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetHomework getHomework = retrofit.create(GetHomework.class);
        getHomework.getHomeworks(classroom.getClassroom_id(),student.getStudent_id()).enqueue(new Callback<HomeworksStudent>() {
            @Override
            public void onResponse(Call<HomeworksStudent> call, Response<HomeworksStudent> response) {
                if(response.isSuccessful() && response.body() != null){
                    HomeworksStudent models = response.body();
                    Log.d("Response",new Gson().toJson(response.body()));

                    ZoneId turkeyZone = ZoneId.of("Europe/Istanbul");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    hwModels = models.getHomeworks();
                    if(hwModels.isEmpty()){
                        Toast.makeText(context, "Ödev yok!", Toast.LENGTH_SHORT).show();
                    }
                    for(HwModel o : hwModels)
                    {
//                        HomeworkResult newResult = new HomeworkResult();
//                        int homeworkId = o.getHomework_id();
//
//                        newResult.setHomework_id(homeworkId);
//                        newResult.setStudent_id(student.getStudent_id());
//
//                        Retrofit retrofit2 = new Retrofit.Builder()
//                                .baseUrl("http://sinifdoktoruadmin.online/")
//                                .addConverterFactory(GsonConverterFactory.create())
//                                .build();
//
//                        HomeworkStudentsScreen.Result resultRequest = retrofit2.create(HomeworkStudentsScreen.Result.class);
//
//                        System.out.println(homeworkId);
//                        System.out.println(student.getStudent_id());
//                        resultRequest.getResult(homeworkId, student.getStudent_id()).enqueue(new Callback<HomeworkResultModel>(){
//
//                            @Override
//                            public void onResponse(Call<HomeworkResultModel> call, Response<HomeworkResultModel> response) {
//                                if(response.isSuccessful() && response.body() != null)
//                                {
//                                    HomeworkResultModel model = response.body();
//                                    HomeworkResult result = model.getResult();
//                                    System.out.println(result);
//                                    o.setResult(result);
//                                    System.out.println(new Gson().toJson(model.getResult()));
//                                    if(result != null)
//                                    {
//                                        newResult.setGradedBefore(true);
//                                        newResult.setHomework_result_id(result.getHomework_result_id());
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<HomeworkResultModel> call, Throwable t) {
//
//                            }
//                        });

                        //System.out.println(o.getResult());


                        LocalDate today = LocalDate.now(turkeyZone);
                        LocalDate localDate = LocalDate.parse(o.getDue_date(), formatter);

                        if(localDate.isBefore(today))
                            expiredHws.add(o);
                        else
                            ongoingHws.add(o);
                    }

                    sortListByDate(hwModels);
                    homeworkAdapter = new StudentHomeworkAdapter(context, hwModels, homeworkViewInterface);
                    recyclerView.setAdapter(homeworkAdapter);

                }
                else{
                }
            }
            @Override
            public void onFailure(Call<HomeworksStudent> call, Throwable t) {
            }
        });




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
                homeworkAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }



    
    // This method is called when the user clicks on the filter icon
    private void showOverlay() {
        if (filterDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View overlayView = inflater.inflate(R.layout.filter_overlay, null);
            builder.setView(overlayView);

            filterDialog = builder.create();


            // Set the dialog window attributes to make it a small overlay
            WindowManager.LayoutParams layoutParams = filterDialog.getWindow().getAttributes();

            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.TOP | Gravity.CENTER;
            filterDialog.getWindow().setAttributes(layoutParams);

            // Find the checkboxes in the overlay layout
            CheckBox checkBox1 = overlayView.findViewById(R.id.checkBox);
            CheckBox checkBox2 = overlayView.findViewById(R.id.checkBox2);

            // Add any additional customization or logic to the checkboxes here


            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    updateHomeworkList(isChecked, checkBox2.isChecked());
                }
            });

            checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    updateHomeworkList(checkBox1.isChecked(), isChecked);
                }
            });

            // Add any additional customization or logic to the checkboxes here
        }
        filterDialog.show();
    }

    private void updateHomeworkList(boolean includeOngoing, boolean includeExpired) {
        ArrayList<HwModel> filteredList = new ArrayList<>();

        if (includeOngoing) filteredList.addAll(ongoingHws);
        else if (includeExpired) filteredList.addAll(expiredHws);
        else filteredList.addAll(hwModels);

        sortListByDate(filteredList);

        homeworkAdapter = new StudentHomeworkAdapter(context, filteredList, homeworkViewInterface);
        recyclerView.setAdapter(homeworkAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onClassroomHomeworkItemClick(HwModel clickedItem, View view) {
        // Create an AlertDialog.Builder object with the context of the itemView
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        // Create a LayoutInflater object from the itemView's context
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        // Inflate the overlay_layout.xml file into a View object

        View overlayView = inflater.inflate(R.layout.dialog_hw_detail, null);
        EditText courseName = overlayView.findViewById(R.id.homework_detail_course_name);
        EditText title = overlayView.findViewById(R.id.homework_detail_title);
        EditText dueDate = overlayView.findViewById(R.id.homework_detail_duedate);
        EditText content = overlayView.findViewById(R.id.homework_detail_content);
        ImageView imageView = overlayView.findViewById(R.id.homework_detail_image);
        Button gradeButton = overlayView.findViewById(R.id.give_grade_button);

        EditText parentNote = overlayView.findViewById(R.id.hw_parent_note_edittext);
        EditText studentNote = overlayView.findViewById(R.id.hw_student_note_edittext);


        if(LocalDataManager.getSharedPreference(getApplicationContext(), "USER", "unknown").equals("STUDENT"))
        {
            TextInputLayout pn =overlayView.findViewById(R.id.hw_parent_note);
            pn.setVisibility(View.GONE);
        }

        if(clickedItem.getResult() != null)
        {
            parentNote.setText(clickedItem.getResult().getNote_for_parent());
            studentNote.setText(""+clickedItem.getResult().getGrade());
        }


        gradeButton.setVisibility(View.GONE);

        courseName.setEnabled(false);
        title.setEnabled(false);
        dueDate.setEnabled(false);
        content.setEnabled(false);
        parentNote.setEnabled(false);
        studentNote.setEnabled(false);

        courseName.setText(clickedItem.getCourse_name());
        title.setText(clickedItem.getTitle());
        dueDate.setText(clickedItem.getDue_date());
        content.setText(clickedItem.getInfo());





        // If the clickedItem has no image, do not open the full screen view
        if(clickedItem.getGetImage() != null){
            byte[] imageBytes = Base64.decode(clickedItem.getGetImage(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
            Glide.with(HomeWorkScreen.this)
                    .load(decodedImage)
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(HomeWorkScreen.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                    dialog.setContentView(R.layout.dialog_full_screen_image);

                    ImageView fullScreenImage = dialog.findViewById(R.id.fullScreenImageView);
                    fullScreenImage.setImageBitmap(decodedImage);
                    fullScreenImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }

        // Set the inflated view as the custom view for the AlertDialog
        builder.setView(overlayView);

        // Create an AlertDialog object from the builder
        AlertDialog dialog = builder.create();

        // Show the AlertDialog
        dialog.show();
    }

    @Override
    public void onClassroomHomeworkEditClick(HwModel clickedItem, View view) {

    }

    private void sortListByDate(ArrayList<HwModel> list){
        ZoneId turkeyZone = ZoneId.of("Europe/Istanbul");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now(turkeyZone);

        Comparator<HwModel> dateComparator = (date1, date2) -> {
            LocalDate localDate1 = LocalDate.parse(date1.getDue_date(), formatter);
            LocalDate localDate2 = LocalDate.parse(date2.getDue_date(), formatter);

            if (localDate1.isEqual(today)) {
                return -1; // Bugünkü tarihleri en önce sırala
            } else if (localDate2.isEqual(today)) {
                return 1; // Bugünkü tarihleri en önce sırala
            } else if (localDate1.isBefore(today) && localDate2.isBefore(today)) {
                return localDate2.compareTo(localDate1); // Geçmiş tarihleri büyükten küçüğe sırala
            } else if (localDate1.isAfter(today) && localDate2.isAfter(today)) {
                return localDate1.compareTo(localDate2); // Gelecek tarihleri küçükten büyüğe sırala
            } else if (localDate1.isBefore(today) && localDate2.isAfter(today)) {
                return 1; // Geçmiş tarihleri gelecek tarihlerden sonra sırala
            } else {
                return -1; // Gelecek tarihleri geçmiş tarihlerden önce sırala
            }
        };

        Collections.sort(list, dateComparator);
    }

}