package com.example.birdaha.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdaha.Fragments.EventFragment;
import com.example.birdaha.Fragments.GeneralAnnouncementFragment;
import com.example.birdaha.R;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_home_page);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.event_container, new EventFragment())
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.announcement_container, new GeneralAnnouncementFragment())
                .commit();
    }
}