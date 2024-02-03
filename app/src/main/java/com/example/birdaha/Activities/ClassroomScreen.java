package com.example.birdaha.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.R;
import com.example.birdaha.Users.Teacher;

public class ClassroomScreen extends AppCompatActivity {

    Button classroomAnnouncementsButton;
    Button classroomHomeworksButton;
    TextView classroomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_screen);

        // Get the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the title
            actionBar.setTitle("");
        }

        Intent intent2 = getIntent();
        if(intent2 != null){
            Classroom classroom = (Classroom) intent2.getSerializableExtra("classroom");
            classroomName = findViewById(R.id.classroom_info);
            classroomName.setText(classroom.getName());
            classroomAnnouncementsButton = findViewById(R.id.classroom_announcements);

            classroomAnnouncementsButton.setOnClickListener(v -> {
                        Intent getIntent = getIntent();
                        if (getIntent != null) {
                            Teacher teacher = (Teacher) getIntent.getSerializableExtra("teacher");
                            Intent intent = new Intent(ClassroomScreen.this, ClassRoomAnnouncementScreen.class);
                            intent.putExtra("teacher", teacher);
                            intent.putExtra("classroom", classroom);
                            startActivity(intent);
                        }
            });
                classroomHomeworksButton = findViewById(R.id.classroom_homeworks);
                classroomHomeworksButton.setOnClickListener(v -> {
                    Intent getIntent2 = getIntent();
                    if(getIntent2 != null){
                        Teacher teacher = (Teacher) getIntent2.getSerializableExtra("teacher");
                        Intent intent = new Intent(ClassroomScreen.this, ClassroomHomeworkScreen.class);
                        intent.putExtra("teacher",teacher);
                        intent.putExtra("classroom",classroom);
                        startActivity(intent);
                    }
                });
        }
    }
}