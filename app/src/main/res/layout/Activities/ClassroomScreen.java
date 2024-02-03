package com.example.birdaha.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdaha.R;

public class ClassroomScreen extends AppCompatActivity {

    Button classroomAnnouncementsButton;
    Button classroomHomeworksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_screen);

        classroomAnnouncementsButton = findViewById(R.id.classroom_announcements);
        classroomHomeworksButton = findViewById(R.id.classroom_homeworks);

        classroomAnnouncementsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ClassroomScreen.this, ClassRoomAnnouncementScreen.class);
            startActivity(intent);
        });

        classroomHomeworksButton.setOnClickListener(v -> {
            Intent intent = new Intent(ClassroomScreen.this, ClassroomHomeworkScreen.class);
            startActivity(intent);
        });

    }
}