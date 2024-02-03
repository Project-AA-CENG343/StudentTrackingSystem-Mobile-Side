package com.example.birdaha.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.birdaha.Fragments.HomePageFragment;
import com.example.birdaha.Fragments.NotificationFragment;
import com.example.birdaha.Fragments.StudentProfileFragment;
import com.example.birdaha.Helper.FragmentNavigationManager;
import com.example.birdaha.Helper.LocalDataManager;
import com.example.birdaha.Helper.ProfilePictureChangeEvent;
import com.example.birdaha.Interface.NavigationManager;
import com.example.birdaha.R;
import com.example.birdaha.Users.Student;
import com.example.birdaha.Users.Teacher;
import com.example.birdaha.Utilities.NotificationService.NotificationJobService;
import com.example.birdaha.Utilities.NotificationService.Service;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * The StudentMainActivity class represents the main activity for student users within the application.
 * This activity manages a navigation drawer using a DrawerLayout and handles fragment transactions
 * based on user interaction with the navigation drawer items.
 * It extends AppCompatActivity to ensure compatibility across various Android versions.
 */
public class StudentMainActivity extends AppCompatActivity {


    /***
     * DrawerLayout for menu usage
     */
    private DrawerLayout drawerLayout;

    /**
     * Toggle for open/close DrawerLayout
     */
    private ActionBarDrawerToggle drawerToggle;

    /**
     * NavigationManager for switch between fragments
     */
    private NavigationManager navigationManager;

    private ImageView studentPhoto;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState A Bundle containing the saved state of the activity,
     *                            or null if there is no saved state.
     *
     * Initializes the activity's layout, sets up the navigation drawer, and handles
     * fragment transactions based on the selected item in the drawer menu. Also sets up
     * click listeners for various TextViews in the navigation drawer to show respective fragments.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        EventBus.getDefault().register(this);

        // Get the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the title
            actionBar.setTitle("");
        }

        Service.start(NotificationJobService.class, this, 102, "notification");


        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout_window_field);
        TextView nameSurname = drawerLayout.findViewById(R.id.TextView_student_name_surname);
        studentPhoto = drawerLayout.findViewById(R.id.ImageView_person_photo);

        Intent intent = getIntent();
        if(intent != null){
            Student student = (Student) intent.getSerializableExtra("user");
            nameSurname.setText(student.getName());
            SharedPreferences preferences = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);
            String key = "profile_data_" + student.getStudent_id();
            String combinedData = preferences.getString(key,"");
            String[] dataParts = combinedData.split("\\|");
            if(dataParts.length == 2){
                int studentId = Integer.parseInt(dataParts[0]);
                String encodedImage = dataParts[1];
                if(student.getStudent_id() == studentId){
                    byte[] byteArray = Base64.decode(encodedImage,Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
                    Glide.with(this)
                            .load(decodedImage)
                            .circleCrop()
                            .into(studentPhoto);
                }
            }
        }
        navigationManager = FragmentNavigationManager.getmInstance(this);


        setupDrawer();

        if(savedInstanceState == null)
            selectFirstItemAsDefault();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        TextView TextView_profile = findViewById(R.id.TextView_profile);
        TextView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if(intent != null){
                    Student student = (Student) intent.getSerializableExtra("user");

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment f = fragmentManager.findFragmentById(R.id.FrameLayout_container);

                    if(!(f instanceof StudentProfileFragment))
                        navigationManager.showFragment(StudentProfileFragment.newInstance(student,true), false);

                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        TextView TextView_home_page = findViewById(R.id.TextView_home_page);
        TextView_home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment f = fragmentManager.findFragmentById(R.id.FrameLayout_container);
                Student student = (Student) intent.getSerializableExtra("user");


                if(!(f instanceof HomePageFragment))
                    navigationManager.showFragment(HomePageFragment.newInstance("userId", student), false);

                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        TextView TextView_notifications = findViewById(R.id.TextView_notifications);
        TextView_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment f = fragmentManager.findFragmentById(R.id.FrameLayout_container);

                Student student = null;
                if(getIntent() != null){
                    student = (Student) getIntent().getSerializableExtra("user");
                }

                if(!(f instanceof NotificationFragment))
                    navigationManager.showFragment(NotificationFragment.newInstance("userId",student.getName(), student.getStudent_id(),"student",student.getSchool_no()), false);

                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        /*
        TextView TextView_logout = findViewById(R.id.TextView_logout);
        TextView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDataManager.clearSharedPreference(getApplicationContext());
                Intent intent = new Intent(StudentMainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        */

    }

    /**
     * Called after the activity's `onCreate()` method has returned, indicating that
     * the activity's state is restored and it's ready to be interacted with.
     * Synchronizes the state of the ActionBarDrawerToggle with the DrawerLayout,
     * updating the ActionBar's navigation icon or drawer indicator to reflect the current DrawerLayout state.
     *
     * @param savedInstanceState A Bundle containing the saved state of the activity,
     *                            or null if there is no saved state.
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * Called when the device's configuration changes.
     * Allows the ActionBarDrawerToggle to respond to configuration changes,
     * ensuring proper display of the ActionBar's navigation icon or drawer indicator
     * corresponding to the new configuration of the DrawerLayout.
     *
     * @param newConfig The new Configuration object representing the device's new configuration.
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Sets the first item in the navigation drawer as the default item.
     * If the navigation manager is not null, it displays the Home Page Fragment
     * as the default fragment without adding it to the back stack.
     */
    private void selectFirstItemAsDefault() {
        Student student = (Student) getIntent().getSerializableExtra("user");

        if(navigationManager != null)
            navigationManager.showFragment(HomePageFragment.newInstance("", student), false);

    }

    /**
     * Sets up the Navigation Drawer with an ActionBarDrawerToggle for handling
     * opening and closing events of the drawer. It also updates the options menu
     * when the drawer state changes.
     *
     * This method initializes the ActionBarDrawerToggle, sets its open/close strings,
     * and overrides its onDrawerOpened and onDrawerClosed methods to update the
     * options menu accordingly when the drawer is opened or closed.
     */
    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    /**
     * Initialize the contents of the Activity's options menu.
     *
     * @param menu The options menu in which items are placed.
     * @return Returns true for the menu to be displayed; if you return false,
     *         the menu will not be shown.
     *
     * This method is called during the creation of the options menu for the Activity.
     * It is typically used to inflate the menu from a menu resource (XML) or perform
     * other initialization related to the options menu. Returning true indicates that
     * the menu should be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    /**
     * Called when a menu item in the options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return Returns true if the menu item selection is handled; false otherwise.
     *
     * This method is called when a user selects an item from the options menu.
     * It identifies the selected item by its ID and performs the appropriate action.
     * If the selected item is related to the ActionBarDrawerToggle, it handles the
     * selection to open or close the Navigation Drawer. Returns true if the selection
     * is handled; otherwise, it delegates to the superclass for default handling.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onProfilePictureChange(ProfilePictureChangeEvent event){
        boolean profilePictureDeleted = event.isProfilePictureDeleted();
        boolean profilePictureChanged = event.isProfilePictureChanged();

        if(profilePictureDeleted){
            Glide.with(this)
                    .load(R.drawable.baseline_person_24)
                    .circleCrop()
                    .into(studentPhoto);
        }
        if(profilePictureChanged){
            Intent intent = getIntent();
            if(intent != null){
                Student student = (Student) intent.getSerializableExtra("user");
                SharedPreferences preferences = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);
                String key = "profile_data_" + student.getStudent_id();
                String combinedData = preferences.getString(key,"");
                String[] dataParts = combinedData.split("\\|");
                if(dataParts.length == 2){
                    int studentId = Integer.parseInt(dataParts[0]);
                    String encodedImage = dataParts[1];
                    if(student.getStudent_id() == studentId){
                        byte[] byteArray = Base64.decode(encodedImage,Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
                        Glide.with(this)
                                .load(decodedImage)
                                .circleCrop()
                                .into(studentPhoto);
                    }
                }
            }
        }
    }

}
