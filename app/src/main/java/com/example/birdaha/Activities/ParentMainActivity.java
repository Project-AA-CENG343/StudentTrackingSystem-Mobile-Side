package com.example.birdaha.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.birdaha.Adapters.CustomExpandableListAdapter;
import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.Fragments.HomePageFragment;
import com.example.birdaha.Fragments.NotificationFragment;
import com.example.birdaha.Fragments.StudentProfileFragment;
import com.example.birdaha.Helper.FragmentNavigationManager;
import com.example.birdaha.Helper.LocalDataManager;
import com.example.birdaha.Interface.NavigationManager;
import com.example.birdaha.R;
import com.example.birdaha.Users.Parent;
import com.example.birdaha.Users.Student;
import com.example.birdaha.Utilities.NotificationService.NotificationJobService;
import com.example.birdaha.Utilities.NotificationService.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * The ParentMainActivity serves as the primary activity for parent users in the application.
 * This activity manages a DrawerLayout with a navigation drawer, an ExpandableListView
 * to display students' information, and handles fragment transactions for various screens.
 * It extends AppCompatActivity to ensure compatibility across different Android versions.
 */
public class ParentMainActivity extends AppCompatActivity {

    /***
     * DrawerLayout for menu usage
     */
    private DrawerLayout drawerLayout;

    /**
     * Toggle for open/close DrawerLayout
     */
    private ActionBarDrawerToggle drawerToggle;

    /***
     * ExpandableListView for parent's students field
     */
    private ExpandableListView expandableListView;

    /**
     * ExpandableListAdapter
     */
    private ExpandableListAdapter adapter;

    /**
     * Parent's students field TextView text (TextView_my_students)
     */
    private String my_students_title;

    /**
     * Map for ExpandableList items
     */
    private Map<String, List<Student>> lstChild;

    /**
     * NavigationManager for switch between fragments
     */
    private NavigationManager navigationManager;

    private Parent currentParent;

    /***
     *
     * It creates content of Activity
     *
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentParent = (Parent) getIntent().getSerializableExtra("user");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);

        // Get the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the title
            actionBar.setTitle("");
        }

        Service.start(NotificationJobService.class, this, 102, "notification");

        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout_window_field);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        TextView nameSurname = findViewById(R.id.TextView_name_surname);
        nameSurname.setText(currentParent.getName());

        expandableListView = (ExpandableListView) findViewById(R.id.ExpandableList_my_students);    //expandable list for students
        navigationManager = FragmentNavigationManager.getmInstance(this); //get singleton instance

        setupExpandableListViewAnimation(fadeInAnimation);
        getData(); //get data which use in expandable-list
        fillMyStudents(); //it fill expandable-list

        setupDrawer();

        if (savedInstanceState == null)
            setDefaultFragment();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        TextView TextView_home_page = findViewById(R.id.TextView_home_page);
        TextView_home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment f = fragmentManager.findFragmentById(R.id.FrameLayout_container);

                if(!(f instanceof HomePageFragment))
                    navigationManager.showFragment(HomePageFragment.newInstance("userId", currentParent), false);

                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        TextView TextView_notifications = findViewById(R.id.TextView_notifications);
        TextView_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment f = fragmentManager.findFragmentById(R.id.FrameLayout_container);
                Parent parent = null;
                if(getIntent() != null){
                    parent = (Parent) getIntent().getSerializableExtra("user");
                }

                if(!(f instanceof NotificationFragment))
                    navigationManager.showFragment(NotificationFragment.newInstance("userId",parent.getName(), parent.getParent_id(),"parent",0), false);

                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    /*
        TextView TextView_logout = findViewById(R.id.TextView_logout);
        TextView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDataManager.clearSharedPreference(getApplicationContext());
                Intent intent = new Intent(ParentMainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    private void setupExpandableListViewAnimation(final Animation animation) {
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (!parent.isGroupExpanded(groupPosition)) {
                    // Apply animation for expanding
                    v.startAnimation(animation);
                }
                return false;
            }
        });

    }

    /**
     * Called after the activity's `onCreate()` method has returned, which signifies that
     * the activity's state is restored and it's ready to be interacted with.
     *
     * @param savedInstanceState A Bundle containing the saved state of the activity,
     *                            or null if there is no saved state.
     *
     * Synchronizes the state of the ActionBarDrawerToggle, updating the ActionBar's
     * navigation icon or drawer indicator to reflect the current DrawerLayout state.
     * This ensures visual consistency when the user interacts with the Navigation Drawer.
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * Called when the device's configuration changes.
     *
     * @param newConfig The new Configuration object representing the device's new configuration.
     *
     * Updates the state of the Navigation Drawer in response to changes in the device's configuration.
     * Ensures the ActionBarDrawerToggle adapts to the device's new configuration.
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Sets the default fragment for the navigation.
     * If the navigation manager is not null, it displays the Home Page Fragment
     * as the default fragment without adding it to the back stack.
     */
    private void setDefaultFragment() {

        if(navigationManager != null)
            navigationManager.showFragment(HomePageFragment.newInstance("", currentParent), false);

    }

    /**
     * Sets up the Navigation Drawer with the ActionBarDrawerToggle for handling
     * opening and closing events of the drawer.
     *
     * The method initializes the ActionBarDrawerToggle, sets its open/close strings,
     * and overrides its onDrawerOpened and onDrawerClosed methods to update the
     * options menu when the drawer state changes.
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
     * Fills the ExpandableListView with student data using a CustomExpandableListAdapter.
     * Sets up the adapter, populates the ExpandableListView, and sets a listener for child clicks.
     * When a child is clicked, it displays the profile of the selected student.
     */
    private void fillMyStudents() {
        adapter = new CustomExpandableListAdapter(this, my_students_title, lstChild);
        expandableListView.setAdapter(adapter);
        //expandableListView.addView((View) adapter.getGroup(0));

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Intent intent = getIntent();
                if(intent != null){
                    Student student = lstChild.get(my_students_title).get(childPosition);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment f = fragmentManager.findFragmentById(R.id.FrameLayout_container);
                    if(!(f instanceof StudentProfileFragment) || StudentProfileFragment.getCurrStudent().getSchool_no() != student.getSchool_no()) {
                        navigationManager.showFragment(StudentProfileFragment.newInstance(student, false), false);
                    }

                }


                //String selectedItem = ((List) (lstChild.get(my_students_title))).get(childPosition).toString();
                //Student selectedStudent = (Student) ((List) (lstChild.get(my_students_title))).get(childPosition);
                //navigationManager.showFragment(StudentProfileFragment.newInstance(selectedStudent),false);
                // Display the profile of the selected student
                //navigationManager.showFragment(StudentProfileFragment.newInstance("stuId"), false);

                // Close the Navigation Drawer after a child is clicked
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }


    /**
     * Prepares data to populate the ExpandableListView with student information.
     * Creates a title for the list and a list of child items.
     * Initializes lstChild as a LinkedHashMap with the title and child items.
     */
    private void getData() {

        String title = "Öğrencilerim";
        List<Student> childItem = currentParent.getStudents();

        for(Student stu : childItem)
        {
            Classroom classroom = new Classroom(""+stu.getClassroom().getName(), stu.getClassroom_id());
            stu.setClassroom(classroom);
        }

        // Prepare data for ExpandableListView
        lstChild = new LinkedHashMap<>();
        lstChild.put(title, childItem);
        my_students_title = title;

    }

    /**
     * Initialize the contents of the Activity's options menu.
     *
     * @param menu The options menu in which items are placed.
     * @return Returns true for the menu to be displayed; if you return false,
     *         the menu will not be shown.
     *
     * This method is called during the creation of the options menu for the Activity.
     * You can use it to inflate the menu from a menu resource (XML) or perform
     * any other initialization related to the options menu.
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
}
