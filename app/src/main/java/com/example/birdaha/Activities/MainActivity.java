package com.example.birdaha.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.Classrooms.Lecture;
import com.example.birdaha.Helper.LocalDataManager;
import com.example.birdaha.R;
import com.example.birdaha.Users.ChangePasswordParent;
import com.example.birdaha.Users.ChangePasswordStudent;
import com.example.birdaha.Users.ChangePasswordTeacher;
import com.example.birdaha.Users.LoginRequest;
import com.example.birdaha.Users.Parent;
import com.example.birdaha.Users.Student;
import com.example.birdaha.Users.Teacher;
import com.example.birdaha.Users.User;
import com.example.birdaha.Users.UserRespond;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * This class represents the main login screen of the application.
 * It allows users to enter their username and password and login as either a Student, Teacher, or Parent.
 */
public class MainActivity extends AppCompatActivity {

    public interface RequestUser {
        @POST("api/v1/login")
        Call<UserRespond> postUser(@Body LoginRequest loginRequest);

        @POST("api/v1/teacher/password")
        Call<UserRespond> changePasswordTeacher(@Body ChangePasswordTeacher teacher);

        @POST("api/v1/student/password")
        Call<UserRespond> changePasswordStudent(@Body ChangePasswordStudent student);

        @POST("api/v1/parent/password")
        Call<UserRespond> changePasswordParent(@Body ChangePasswordParent parent);
    }

    EditText username;
    EditText password;
    CheckBox checkBox;
    MaterialButton loginButton;

    /**
     * Called when the activity is created. Initializes the user interface and sets up event handlers
     * for the login button.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the content view to the main login layout

        // Get the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the title
            actionBar.setTitle("");
        }
        // Find and initialize UI elements
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        checkBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        loginButton = (MaterialButton) findViewById(R.id.loginButton);

        Boolean isRemember = LocalDataManager.getSharedPreference(getApplicationContext(), "isRemember", "noValue").equals("true");
        checkBox.setChecked(isRemember);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LocalDataManager.setSharedPreference(getApplicationContext(), "isRemember", checkBox.isChecked() ? "true" : "false");
            }
        });

        // Set a click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDataManager.removeSharedPreference(getApplicationContext(), "username");
                LocalDataManager.removeSharedPreference(getApplicationContext(), "password");
                login(username.getText().toString(), password.getText().toString());
            }
        });


        if(isRemember)
        {
            String username = LocalDataManager.getSharedPreference(getApplicationContext(), "username", "noValue");
            String password = LocalDataManager.getSharedPreference(getApplicationContext(), "password", "noValue");
            if(!(username.equals("noValue") || password.equals("noValue")))
                login(username, password);
        }

    }

    /**
     * Checks whether the edit texts for username and password are empty or not.
     *
     * @return True if both fields are not empty; otherwise, false.
     */
    public boolean checkEditText() {
        String name = username.getText().toString();
        String pass = password.getText().toString();
        return !name.isEmpty() && !pass.isEmpty();
    }

    private void login(String username, String password)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sinifdoktoruadmin.online/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestUser requestUser = retrofit.create(RequestUser.class);
        LoginRequest loginRequest = new LoginRequest(username, password);

        requestUser.postUser(loginRequest).enqueue(new Callback<UserRespond>() {
            @Override
            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LocalDataManager.setSharedPreference(getApplicationContext(), "username", username);
                    LocalDataManager.setSharedPreference(getApplicationContext(), "password", password);

                    UserRespond respond = response.body();
                    User user = respond.getUser();
                    Log.d("Respond", new Gson().toJson(respond.getUser()).toString());
                    switch (response.code()) {
                        case 201:

                            LocalDataManager.setSharedPreference(getApplicationContext(), "USER", "TEACHER");

                            List<Classroom> classrooms = respond.getUser().getClassrooms();
                            Lecture course = respond.getUser().getCourse();
                            Log.d("Course", course.getName());
                            Log.d("Course", String.valueOf(course.getCourse_id()));
                            Teacher teacher = new Teacher(user.getName(), user.getTeacher_id(), course, classrooms);
                            if((username + "123").equals(password)){
                                Dialog dialog = new Dialog(MainActivity.this);
                                dialog.setContentView(R.layout.change_password);
                                TextInputEditText newPassword = dialog.findViewById(R.id.newPassword);
                                TextInputEditText confirmPassword = dialog.findViewById(R.id.confirmPassword);
                                ImageView closeDialog = dialog.findViewById(R.id.closeChangePassword);
                                closeDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(MainActivity.this, TeacherMainActivity.class);
                                        intent.putExtra("user", teacher);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                Button button = dialog.findViewById(R.id.set_new_password_btn);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(TextUtils.isEmpty(newPassword.getText()) || TextUtils.isEmpty(confirmPassword.getText())){
                                            Toast.makeText(MainActivity.this, "Lütfen gerekli yerleri doldurun", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                                            Toast.makeText(MainActivity.this, "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if(newPassword.getText().toString().equals(password)){
                                            Toast.makeText(MainActivity.this, "Yeni şifreniz eskisi ile aynı olamaz", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl("http://sinifdoktoruadmin.online/")
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        ChangePasswordTeacher teacherChange = new ChangePasswordTeacher(user.getTeacher_id(),newPassword.getText().toString());
                                        RequestUser changePassword = retrofit.create(RequestUser.class);
                                        changePassword.changePasswordTeacher(teacherChange).enqueue(new Callback<UserRespond>() {
                                            @Override
                                            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                                                if(response.isSuccessful() && response.body() != null){
                                                    Toast.makeText(MainActivity.this, "Şifreniz başarıyla değiştirildi", Toast.LENGTH_SHORT).show();
                                                    LocalDataManager.setSharedPreference(getApplicationContext(), "password", newPassword.getText().toString());
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(MainActivity.this, TeacherMainActivity.class);
                                                    intent.putExtra("user", teacher);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    Toast.makeText(MainActivity.this, "Hata oluştu " + response.code(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<UserRespond> call, Throwable t) {
                                                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                dialog.show();
                            }
                            else{
                                Intent intent = new Intent(MainActivity.this, TeacherMainActivity.class);
                                intent.putExtra("user", teacher);
                                startActivity(intent);
                                finish();
                            }
                            break;
                        case 202:

                            LocalDataManager.setSharedPreference(getApplicationContext(), "USER", "STUDENT");

                            Classroom classroom = respond.getUser().getClassroom();
                            Log.d("Classroom", classroom.getName());
                            Student student = new Student(user.getName(), user.getStudent_id(), classroom, user.getSchool_no());
                            if((username + "123").equals(password)){
                                Dialog dialog = new Dialog(MainActivity.this);
                                dialog.setContentView(R.layout.change_password);
                                TextInputEditText newPassword = dialog.findViewById(R.id.newPassword);
                                TextInputEditText confirmPassword = dialog.findViewById(R.id.confirmPassword);
                                ImageView closeDialog = dialog.findViewById(R.id.closeChangePassword);
                                closeDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(MainActivity.this, StudentMainActivity.class);
                                        intent.putExtra("user", student);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                Button button = dialog.findViewById(R.id.set_new_password_btn);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(TextUtils.isEmpty(newPassword.getText()) || TextUtils.isEmpty(confirmPassword.getText())){
                                            Toast.makeText(MainActivity.this, "Lütfen gerekli yerleri doldurun", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                                            Toast.makeText(MainActivity.this, "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if(newPassword.getText().toString().equals(password)){
                                            Toast.makeText(MainActivity.this, "Yeni şifreniz eskisi ile aynı olamaz", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl("http://sinifdoktoruadmin.online/")
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        ChangePasswordStudent studentChange = new ChangePasswordStudent(user.getStudent_id(),newPassword.getText().toString());
                                        RequestUser changePassword = retrofit.create(RequestUser.class);
                                        changePassword.changePasswordStudent(studentChange).enqueue(new Callback<UserRespond>() {
                                            @Override
                                            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                                                if(response.isSuccessful() && response.body() != null){
                                                    Toast.makeText(MainActivity.this, "Şifreniz başarıyla değiştirildi", Toast.LENGTH_SHORT).show();
                                                    LocalDataManager.setSharedPreference(getApplicationContext(), "password", newPassword.getText().toString());
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(MainActivity.this, StudentMainActivity.class);
                                                    intent.putExtra("user", student);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    Toast.makeText(MainActivity.this, "Hata oluştu " + response.code(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<UserRespond> call, Throwable t) {
                                                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                dialog.show();
                            }
                            else{
                                Intent intent = new Intent(MainActivity.this, StudentMainActivity.class);
                                intent.putExtra("user", student);
                                startActivity(intent);
                                finish();
                            }
                            break;
                        case 203:

                            LocalDataManager.setSharedPreference(getApplicationContext(), "USER", "PARENT");

                            Parent parent = new Parent(user.getName(), user.getParent_id(), user.getStudents());
                            SharedPreferences preferences = getSharedPreferences("ParentPrefs",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            for(Student currentStudent : user.getStudents()){
                                String combinedData = currentStudent.getStudent_id() + "|" + currentStudent.getGetStudentImage();
                                String key = "parent_student_data_" + currentStudent.getStudent_id();
                                editor.putString(key,combinedData);
                                editor.apply();
                            }
                            if((username + "123").equals(password)){
                                Dialog dialog = new Dialog(MainActivity.this);
                                dialog.setContentView(R.layout.change_password);
                                TextInputEditText newPassword = dialog.findViewById(R.id.newPassword);
                                TextInputEditText confirmPassword = dialog.findViewById(R.id.confirmPassword);
                                ImageView closeDialog = dialog.findViewById(R.id.closeChangePassword);
                                closeDialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(MainActivity.this, ParentMainActivity.class);
                                        intent.putExtra("user", parent);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                Button button = dialog.findViewById(R.id.set_new_password_btn);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(TextUtils.isEmpty(newPassword.getText()) || TextUtils.isEmpty(confirmPassword.getText())){
                                            Toast.makeText(MainActivity.this, "Lütfen gerekli yerleri doldurun", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                                            Toast.makeText(MainActivity.this, "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if(newPassword.getText().toString().equals(password)){
                                            Toast.makeText(MainActivity.this, "Yeni şifreniz eskisi ile aynı olamaz", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl("http://sinifdoktoruadmin.online/")
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();
                                        ChangePasswordParent parentChange = new ChangePasswordParent(user.getParent_id(),newPassword.getText().toString());
                                        RequestUser changePassword = retrofit.create(RequestUser.class);
                                        changePassword.changePasswordParent(parentChange).enqueue(new Callback<UserRespond>() {
                                            @Override
                                            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                                                if(response.isSuccessful() && response.body() != null){
                                                    Toast.makeText(MainActivity.this, "Şifreniz başarıyla değiştirildi", Toast.LENGTH_SHORT).show();
                                                    LocalDataManager.setSharedPreference(getApplicationContext(), "password", newPassword.getText().toString());
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(MainActivity.this, ParentMainActivity.class);
                                                    intent.putExtra("user", parent);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    Toast.makeText(MainActivity.this, "Hata oluştu " + response.code(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<UserRespond> call, Throwable t) {
                                                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                dialog.show();
                            }
                            else{
                                Intent intent = new Intent(MainActivity.this, ParentMainActivity.class);
                                intent.putExtra("user", parent);
                                startActivity(intent);
                                finish();
                            }
                            break;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserRespond> call, Throwable t) {
                //Log.d("ERROR", t.getMessage());
            }
        });
    }
}
