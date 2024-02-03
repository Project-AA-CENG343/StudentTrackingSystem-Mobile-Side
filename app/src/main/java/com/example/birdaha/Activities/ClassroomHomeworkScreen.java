package com.example.birdaha.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.birdaha.Adapters.HomeworkAdapter;
import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.General.HomeworksTeacher;
import com.example.birdaha.General.HwModel;
import com.example.birdaha.General.StudentModel;
import com.example.birdaha.General.UpdateRespond;
import com.example.birdaha.Helper.LocalDataManager;
import com.example.birdaha.R;
import com.example.birdaha.Utilities.ClassroomHomeworkViewInterface;

import com.example.birdaha.Users.Teacher;
import com.example.birdaha.Utilities.HomeworkSerialize;
import com.google.gson.Gson;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

import retrofit2.http.Path;

public class ClassroomHomeworkScreen extends AppCompatActivity implements ClassroomHomeworkViewInterface {


    public interface AddHomework{
        @GET("/api/v1/teacher/homeworks/{classroomId}")
        Call<HomeworksTeacher> getHomeworks(@Path("classroomId") int classroomId);
        @POST("/api/v1/homework/add")
        Call<UpdateRespond> addHomework(@Body HwModel hwmodel);
        @POST("/api/v1/homework/update")
        Call<UpdateRespond> updateHomework(@Body HwModel hwModel);
        @GET("api/v1/homework/delete/{homeworkId}")
        Call<UpdateRespond> deleteHomework(@Path("homeworkId") int homeworkId);
    }

    SearchView search;

    ArrayList<HwModel> hwModels = new ArrayList<>();

    private ArrayList<HwModel> expiredHws;
    private ArrayList<HwModel> ongoingHws;
    private ArrayList<StudentModel> students;
    private RecyclerView recyclerView;
    private Context context;
    private ClassroomHomeworkViewInterface homeworkViewInterface;

    Button addingHomeworkButton;
    Button gradeButton;
    private ImageView homeworkImage;

    private String image;

    private HomeworkAdapter homeworkAdapter;

    private AlertDialog filterDialog = null;

    private Teacher teacher1 = null;



    private ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    Uri imageUri = result;
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        homeworkImage.setImageBitmap(bitmap);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_homework_screen);

        // Get the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the title
            actionBar.setTitle("");
        }

        recyclerView = findViewById(R.id.hwRecyclerView_classroom);

        expiredHws = new ArrayList<>();
        ongoingHws = new ArrayList<>();

        context = this;
        homeworkViewInterface = this;

        search = findViewById(R.id.searchView_homework);
        addingHomeworkButton = findViewById(R.id.adding_hw_btn);
        //gradeButton = findViewById(R.id.grade_btn);

        Classroom classroom = null;

        Intent intent = getIntent();
        if (intent != null) {
            classroom = (Classroom) intent.getSerializableExtra("classroom");

            hwModels = HomeworkSerialize.fromJson(LocalDataManager.getSharedPreference(context, "homework"+classroom.getName(), "")).arr;
            sortListByDate(hwModels);
            teacher1 = (Teacher) getIntent().getSerializableExtra("teacher");
            homeworkAdapter = new HomeworkAdapter(context, (ArrayList<HwModel>) hwModels, homeworkViewInterface, teacher1);
            recyclerView.setAdapter(homeworkAdapter);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sinifdoktoruadmin.online/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (classroom != null) {
            AddHomework getHomework = retrofit.create(AddHomework.class);
            getHomework.getHomeworks(classroom.getClassroom_id()).enqueue(new Callback<HomeworksTeacher>() {
                @Override
                public void onResponse(Call<HomeworksTeacher> call, Response<HomeworksTeacher> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        HomeworksTeacher models = response.body();
                        hwModels = models.getHomeworks();
                        if(hwModels.isEmpty()){
                            Toast.makeText(context, "Ödev yok!", Toast.LENGTH_SHORT).show();
                        }
                        students = models.getStudents();
                        for (HwModel o : hwModels) {

                            LocalDate today = LocalDate.now();
                            LocalDate localDate = LocalDate.parse(o.getDue_date());

                            if (localDate.isBefore(today))
                                expiredHws.add(o);
                            else
                                ongoingHws.add(o);
                        }

                        sortListByDate(hwModels);

                        teacher1 = (Teacher) getIntent().getSerializableExtra("teacher");
                        homeworkAdapter = new HomeworkAdapter(context, (ArrayList<HwModel>) hwModels, homeworkViewInterface, teacher1);
                        recyclerView.setAdapter(homeworkAdapter);
                    }
                }

                @Override
                public void onFailure(Call<HomeworksTeacher> call, Throwable t) {
                   // Log.d("Fail", t.getMessage());
                }
            });
        }

        // Set the layout manager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Find a view for filtering homework items and set a click listener
        View baselineFilterView = findViewById(R.id.filterView);
        baselineFilterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOverlay(); // Call the showOverlay() method when clicked
            }
        });


        addingHomeworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHwDialog();
            }
        });




        // Set a listener for the SearchView to handle query text changes
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

        // Set a listener for closing the SearchView
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        addingHomeworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHwDialog();
            }
        });

        /*gradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int homeworkId = 2280;
                Classroom classroom = (Classroom) intent.getSerializableExtra("classroom");
                ArrayList<StudentModel> students = (ArrayList<StudentModel>) intent.getSerializableExtra("students");
                Intent homeworkGradeIntent = new Intent(ClassroomHomeworkScreen.this, HomeworkStudentsScreen.class);

                homeworkGradeIntent.putExtra("students", (Serializable) students);
                homeworkGradeIntent.putExtra("classroom", classroom);
                homeworkGradeIntent.putExtra("homeworkId", homeworkId);
                startActivity(homeworkGradeIntent);

            }
        });*/


        // Set a listener for the SearchView to handle query text changes
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

        // Set a listener for closing the SearchView
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

    }


    public void showHwDialog()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeworkDialogFragment newFragment = new HomeworkDialogFragment(homeworkAdapter);


        Bundle args = new Bundle();
        args.putSerializable("teacher", getIntent().getSerializableExtra("teacher"));
        args.putSerializable("classroom", getIntent().getSerializableExtra("classroom"));
        newFragment.setArguments(args);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    /**
     * Displays an overlay dialog for filtering options.
     */
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

            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    hwModels.clear();

                    if (checkBox2.isChecked())
                        hwModels.addAll(expiredHws);

                    if (isChecked)
                        hwModels.addAll(ongoingHws);


                    if (!isChecked && !checkBox2.isChecked()) {
                        hwModels.addAll(expiredHws);
                        hwModels.addAll(ongoingHws);
                    }


                    sortListByDate(hwModels);
                    HomeworkAdapter homeworkAdapter = new HomeworkAdapter(context, hwModels, homeworkViewInterface, teacher1);
                    recyclerView.setAdapter(homeworkAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            });

            checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    hwModels.clear();

                    if (checkBox1.isChecked())
                        hwModels.addAll(ongoingHws);

                    if (isChecked)
                        hwModels.addAll(expiredHws);


                    if (!isChecked && !checkBox1.isChecked()) {
                        hwModels.addAll(expiredHws);
                        hwModels.addAll(ongoingHws);
                    }

                    sortListByDate(hwModels);
                    HomeworkAdapter homeworkAdapter = new HomeworkAdapter(context, hwModels, homeworkViewInterface, teacher1);
                    recyclerView.setAdapter(homeworkAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            });

        }

        // Add any additional customization or logic to the checkboxes here

        filterDialog.show();
    }

    @Override
    public void onClassroomHomeworkItemClick(HwModel clickedItem, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        View overlayView = inflater.inflate(R.layout.dialog_hw_detail, null);
        EditText courseName = overlayView.findViewById(R.id.homework_detail_course_name);
        EditText title = overlayView.findViewById(R.id.homework_detail_title);
        EditText dueDate = overlayView.findViewById(R.id.homework_detail_duedate);
        EditText content = overlayView.findViewById(R.id.homework_detail_content);

        if(teacher1 != null)
            overlayView.findViewById(R.id.linearLayout2).setVisibility(View.INVISIBLE);

        ImageView imageView = overlayView.findViewById(R.id.homework_detail_image);
        Button gradeButton = overlayView.findViewById(R.id.give_grade_button);

        courseName.setEnabled(false);
        title.setEnabled(false);
        dueDate.setEnabled(false);
        content.setEnabled(false);

        courseName.setText(clickedItem.getCourse_name());
        title.setText(clickedItem.getTitle());
        dueDate.setText(clickedItem.getDue_date());
        content.setText(clickedItem.getInfo());



        gradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeworkGradeIntent = new Intent(ClassroomHomeworkScreen.this, HomeworkStudentsScreen.class);

                homeworkGradeIntent.putExtra("students", (Serializable) students);
                homeworkGradeIntent.putExtra("classroom", teacher1.getClassroom());
                homeworkGradeIntent.putExtra("homeworkId", clickedItem.getHomework_id());
                startActivity(homeworkGradeIntent);

            }
        });

        // If the clickedItem has no image, do not open the full screen view
        if (clickedItem.getGetImage() != null) {
            byte[] imageBytes = Base64.decode(clickedItem.getGetImage(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            Glide.with(ClassroomHomeworkScreen.this)
                    .load(decodedImage)
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(ClassroomHomeworkScreen.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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

        builder.setView(overlayView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClassroomHomeworkEditClick(HwModel clickedItem, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        View overlayView = inflater.inflate(R.layout.full_screen_hw_adding_dialog, null);
        EditText hw_title = overlayView.findViewById(R.id.hw_title);
        EditText hw_content = overlayView.findViewById(R.id.hw_content);
        EditText hw_due_date = overlayView.findViewById(R.id.dateEditText);
        homeworkImage = overlayView.findViewById(R.id.homework_image);
        Button hw_adding_image = overlayView.findViewById(R.id.hw_adding_image);
        TextView hw_dialog_title = overlayView.findViewById(R.id.fullscreen_hw_title);

        hw_dialog_title.setText("Ödevi Düzenle");

        hw_adding_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });

        hw_title.setText(clickedItem.getTitle());
        hw_content.setText(clickedItem.getInfo());
        hw_due_date.setText(clickedItem.getDue_date());
        if (clickedItem.getGetImage() != null) {
            byte[] imageBytes = Base64.decode(clickedItem.getGetImage(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            Glide.with(ClassroomHomeworkScreen.this)
                    .load(decodedImage)
                    .into(homeworkImage);
        }
        hw_due_date.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, // Use getActivity() as the context
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Format the date in your preferred format (yyyy-MM-dd)
                        String date = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        hw_due_date.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

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

        //Classroom classroom = (Classroom) getIntent().getSerializableExtra("classroom");
        //Teacher teacher = (Teacher) getIntent().getSerializableExtra("teacher");

        TextView actionButton = overlayView.findViewById(R.id.fullscreen_dialog_action);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = hw_title.getText().toString();
                String content = hw_content.getText().toString();
                String due_date = hw_due_date.getText().toString();
                clickedItem.setTitle(title);
                clickedItem.setInfo(content);
                clickedItem.setDue_date(due_date);
                if(image != null){
                    clickedItem.setImage(image);
                    clickedItem.setGetImage(image);
                }


                //HwModel hwModel = new HwModel(classroom.getClassroom_id(), teacher.getTeacher_id(), teacher.getCourse().getName(), due_date, title, content, image);
                //hwModel.setGetImage(image);
                // Retrofit call
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://sinifdoktoruadmin.online/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                AddHomework updateHomework = retrofit.create(AddHomework.class);
                updateHomework.updateHomework(clickedItem).enqueue(new Callback<UpdateRespond>() {
                    @Override
                    public void onResponse(Call<UpdateRespond> call, Response<UpdateRespond> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Handle successful response
                            Toast.makeText(ClassroomHomeworkScreen.this, "Ödev başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle unsuccessful response
                            Log.d("ResponseError", new Gson().toJson(response.body()) + response.code());
                            Toast.makeText(ClassroomHomeworkScreen.this, "Hata oluştu " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateRespond> call, Throwable t) {
                        // Handle failure
                        //Log.d("Error", t.getMessage());
                    }
                });
                dialog.dismiss();
            }
        });
    }

    private void sortListByDate(ArrayList<HwModel> list) {
        ZoneId turkeyZone = ZoneId.of("Europe/Istanbul");
        LocalDate today = LocalDate.now(turkeyZone);

        Comparator<HwModel> dateComparator = (date1, date2) -> {
            LocalDate localDate1 = LocalDate.parse(date1.getDue_date());
            LocalDate localDate2 = LocalDate.parse(date2.getDue_date());

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