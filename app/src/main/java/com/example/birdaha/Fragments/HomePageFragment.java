package com.example.birdaha.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.transition.Slide;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.birdaha.Adapters.HomeworkAdapter;
import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.General.AnnouncementsStudent;
import com.example.birdaha.General.AnnouncementsTeacher;
import com.example.birdaha.General.Event;
import com.example.birdaha.General.EventAndAnnouncements;
import com.example.birdaha.General.GeneralAnnouncement;
import com.example.birdaha.General.HomeworksStudent;
import com.example.birdaha.General.HomeworksTeacher;
import com.example.birdaha.Helper.LocalDataManager;
import com.example.birdaha.R;
import com.example.birdaha.Users.Parent;
import com.example.birdaha.Users.Student;
import com.example.birdaha.Users.Teacher;
import com.example.birdaha.Users.User;
import com.example.birdaha.Utilities.AnnouncementSerialize;
import com.example.birdaha.Utilities.HomeworkSerialize;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * The HomePageFragment class represents the fragment displaying the home page content.
 * This fragment is responsible for inflating the layout and setting up the view components
 * required for displaying the home page.
 */
public class HomePageFragment extends Fragment {
    ArrayList<Event> events = new ArrayList<>();
    ArrayList<SlideModel> slideModels = new ArrayList<>();
    private static HomePageFragment instance = null;
    private boolean isPulled = false;
    private View curView;

    private static User user2;


    interface EventAndAnnouncement{
        @GET("api/v1/event/announcement")
        Call<EventAndAnnouncements> getEventAndAnnouncement();
    }

    interface AddHomework {
        @GET("/api/v1/teacher/homeworks/{classroomId}")
        Call<HomeworksTeacher> getHomeworks(@Path("classroomId") int classroomId);
    }

    public interface MakeAnnouncement {

        @GET("api/v1/teacher/announcements/{classroomId}")
        Call<AnnouncementsTeacher> getAnnouncements(@Path("classroomId") int classroomId);
    }

    interface GetAnnouncement {
        @GET("api/v1/announcements/{classroomId}")
        Call<AnnouncementsStudent> getAnnouncements(@Path("classroomId") int classroomId);
    }

    interface GetHomework{
        @GET("api/v1/homeworks/{classroomId}/{studentId}")
        Call<HomeworksStudent> getHomeworks(@Path("classroomId") int classroomId,
                                            @Path("studentId") int studentId);
    }

    private static final String KEY_TITLE = "Content";


    private HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance(String param1, User user) {

        if(instance == null)
        {
            user2 = user;
            instance = new HomePageFragment();
            Bundle args = new Bundle();
            args.putString(KEY_TITLE, param1);
            instance.setArguments(args);
        }

        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPulled = false;
        // Here you can handle the arguments if needed
        if (getArguments() != null) {
            String title = getArguments().getString(KEY_TITLE);
            // Use 'title' as needed
        }

        // This callback is only called when MyFragment is at least started
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (getParentFragmentManager().getBackStackEntryCount() > 1) {
                    // There is more than 1 fragment in the back stack, so allow going back
                    getParentFragmentManager().popBackStack();
                } else {
                    // Back stack has only 1 fragment or is empty, do nothing
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home_page, container, false);
        if(isPulled)
            layout.findViewById(R.id.responselayout).setVisibility(View.GONE);

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        curView = view;

        if(!isPulled)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://sinifdoktoruadmin.online/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            EventAndAnnouncement getEventAnnouncement = retrofit.create(EventAndAnnouncement.class);
            getEventAnnouncement.getEventAndAnnouncement().enqueue(new Callback<EventAndAnnouncements>() {
                @Override
                public void onResponse(Call<EventAndAnnouncements> call, Response<EventAndAnnouncements> response) {
                    if(response.isSuccessful() && response.body() != null){
                        EventAndAnnouncements respond = response.body();
                        events = respond.getEvents();

                        for(Event currentEvent : events){
                            //Log.d("Event id",String.valueOf(currentEvent.getEvent_id()));
                            if(currentEvent.getImage() != null){
                                byte[] byteArray = Base64.decode(currentEvent.getImage(),Base64.DEFAULT);
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
                                File directory = new File(requireContext().getCacheDir(), "event_images");
                                if(!directory.exists()){
                                    if(directory.mkdirs()){
                                        Log.d("Directory","Directory created");
                                    }
                                }
                                else{
                                    Log.d("Directory","No Directory");
                                }
                                File imageFile = new File(directory,"event_" + currentEvent.getEvent_id() + ".jpg");
                                try{
                                    FileOutputStream fos = new FileOutputStream(imageFile);
                                    decodedImage.compress(Bitmap.CompressFormat.JPEG,100,fos);
                                    fos.close();
                                } catch (IOException e){
                                    e.printStackTrace();
                                }
                                Uri imageUri = Uri.fromFile(imageFile);

                                slideModels.add(new SlideModel(imageUri.toString(), currentEvent.getTitle(),ScaleTypes.CENTER_CROP));
                            }
                            else{
                                slideModels.add(new SlideModel(R.drawable.img_1,currentEvent.getTitle(),ScaleTypes.CENTER_CROP));
                            }
                        }

                        setImageSlider(slideModels, events);

                        List<GeneralAnnouncement> generalAnnouncements = respond.getGeneralAnnouncements();
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.announcement_container, new GeneralAnnouncementFragment(generalAnnouncements))
                                .commit();
                    }
                    else{
                    }

                    isPulled = true;
                    fetchHomeworks();


                }
                @Override
                public void onFailure(Call<EventAndAnnouncements> call, Throwable t) {
                    //Toast.makeText(requireActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            setImageSlider(instance.slideModels, instance.events);
        }
    }

    public void fetchHomeworks()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sinifdoktoruadmin.online/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(user2 instanceof Teacher)
        {

            AddHomework getHomework = retrofit.create(AddHomework.class);

            for(Classroom classroom : user2.getClassrooms())
            {
                getHomework.getHomeworks(classroom.getClassroom_id()).enqueue(new Callback<HomeworksTeacher>() {
                    @Override
                    public void onResponse(Call<HomeworksTeacher> call, Response<HomeworksTeacher> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            HomeworksTeacher models = response.body();
                            HomeworkSerialize obj = new HomeworkSerialize();
                            obj.arr = models.getHomeworks();
                            String json = new Gson().toJson(obj);
                            LocalDataManager.setSharedPreference(getActivity().getApplicationContext(), "homework"+classroom.getName(), json);
                            fetchAnnouncements();

                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<HomeworksTeacher> call, Throwable t) {
                        //Log.d("Fail", t.getMessage());
                    }
                });
            }
        }
        else if(user2 instanceof Student)
        {
            Classroom classroom = user2.getClassroom();
            GetHomework getHomework = retrofit.create(GetHomework.class);
            getHomework.getHomeworks(classroom.getClassroom_id(),user2.getStudent_id()).enqueue(new Callback<HomeworksStudent>() {
                @Override
                public void onResponse(Call<HomeworksStudent> call, Response<HomeworksStudent> response) {
                    if(response.isSuccessful() && response.body() != null){
                        HomeworksStudent models = response.body();
                        HomeworkSerialize obj = new HomeworkSerialize();
                        obj.arr = models.getHomeworks();
                        String json = new Gson().toJson(obj);
                        LocalDataManager.setSharedPreference(getActivity().getApplicationContext(), "homework"+classroom.getName(), json);
                        fetchAnnouncements();

                    }
                    else{
                    }
                }
                @Override
                public void onFailure(Call<HomeworksStudent> call, Throwable t) {
                }
            });
        }
        else if(user2 instanceof Parent)
        {
            for(Student student : user2.getStudents())
            {
                Classroom classroom = student.getClassroom();
                GetHomework getHomework = retrofit.create(GetHomework.class);
                getHomework.getHomeworks(classroom.getClassroom_id(),student.getStudent_id()).enqueue(new Callback<HomeworksStudent>() {
                    @Override
                    public void onResponse(Call<HomeworksStudent> call, Response<HomeworksStudent> response) {
                        if(response.isSuccessful() && response.body() != null){
                            HomeworksStudent models = response.body();
                            HomeworkSerialize obj = new HomeworkSerialize();
                            obj.arr = models.getHomeworks();
                            String json = new Gson().toJson(obj);
                            LocalDataManager.setSharedPreference(getActivity().getApplicationContext(), "homework"+classroom.getName(), json);
                            fetchAnnouncements();
                        }
                        else{
                        }
                    }
                    @Override
                    public void onFailure(Call<HomeworksStudent> call, Throwable t) {
                    }
                });
            }

        }


    }

    private void fetchAnnouncements()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sinifdoktoruadmin.online/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(user2 instanceof Teacher)
        {
            MakeAnnouncement getAnnouncement = retrofit.create(MakeAnnouncement.class);
            for(Classroom classroom : user2.getClassrooms())
            {
                getAnnouncement.getAnnouncements(classroom.getClassroom_id()).enqueue(new Callback<AnnouncementsTeacher>() {
                    @Override
                    public void onResponse(Call<AnnouncementsTeacher> call, Response<AnnouncementsTeacher> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AnnouncementsTeacher models = response.body();
                            AnnouncementSerialize obj = new AnnouncementSerialize();
                            obj.arr = models.getClassroomAnnouncements();
                            String json = new Gson().toJson(obj);
                            LocalDataManager.setSharedPreference(getContext(), "announcement"+classroom.getName(), json);
                            curView.findViewById(R.id.responselayout).setVisibility(View.GONE);
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<AnnouncementsTeacher> call, Throwable t) {
                    }
                });
            }
        }
        else if(user2 instanceof Student)
        {
            Classroom classroom = user2.getClassroom();

            GetAnnouncement getAnnouncement = retrofit.create(GetAnnouncement.class);
            getAnnouncement.getAnnouncements(classroom.getClassroom_id()).enqueue(new Callback<AnnouncementsStudent>() {
                @Override
                public void onResponse(Call<AnnouncementsStudent> call, Response<AnnouncementsStudent> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AnnouncementsStudent models = response.body();
                        AnnouncementSerialize obj = new AnnouncementSerialize();
                        obj.arr = models.getClassAnnouncements();
                        String json = new Gson().toJson(obj);
                        LocalDataManager.setSharedPreference(getActivity().getApplicationContext(), "announcement"+classroom.getName(), json);
                        curView.findViewById(R.id.responselayout).setVisibility(View.GONE);
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<AnnouncementsStudent> call, Throwable t) {
                }
            });
        }
        else if(user2 instanceof Parent)
        {
            for(Student student : user2.getStudents())
            {
                Classroom classroom = student.getClassroom();
                GetAnnouncement getAnnouncement = retrofit.create(GetAnnouncement.class);
                getAnnouncement.getAnnouncements(classroom.getClassroom_id()).enqueue(new Callback<AnnouncementsStudent>() {
                    @Override
                    public void onResponse(Call<AnnouncementsStudent> call, Response<AnnouncementsStudent> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AnnouncementsStudent models = response.body();
                            AnnouncementSerialize obj = new AnnouncementSerialize();
                            obj.arr = models.getClassAnnouncements();
                            String json = new Gson().toJson(obj);
                            LocalDataManager.setSharedPreference(getActivity().getApplicationContext(), "announcement"+classroom.getName(), json);
                            curView.findViewById(R.id.responselayout).setVisibility(View.GONE);
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<AnnouncementsStudent> call, Throwable t) {
                    }
                });
            }
        }
    }

    private void setImageSlider(ArrayList<SlideModel> slideModels, ArrayList<Event> events)
    {

        ImageSlider imageSlider = curView.findViewById(R.id.image_slider);
        imageSlider.setImageList(slideModels,ScaleTypes.FIT);
        imageSlider.setSlideAnimation(AnimationTypes.ZOOM_OUT);

        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

                // Get the Vibrator service
                Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    // Create a vibration effect
                    // You can customize this effect as per your preference
                    VibrationEffect effect = VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE);
                    vibrator.vibrate(effect);
                }
                //SlideModel currentEvent = slideModels.get(i);
                Event currentEvent = events.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(curView.getContext());
                LayoutInflater inflater = LayoutInflater.from(curView.getContext());
                View overlayView = inflater.inflate(R.layout.event_detail_overlay, null);
                ImageView imageView = overlayView.findViewById(R.id.event_detail_image);
                EditText detail = overlayView.findViewById(R.id.event_detail_detail);
                EditText title = overlayView.findViewById(R.id.event_detail_title);
                if(currentEvent.getImage() != null){
                    byte[] byteArray = Base64.decode(currentEvent.getImage(),Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
                    Glide.with(requireActivity())
                            .load(decodedImage)
                            .into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(requireContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                            dialog.setContentView(R.layout.dialog_full_screen_image);

                            ImageView fullscreenImage = dialog.findViewById(R.id.fullScreenImageView);
                            fullscreenImage.setImageBitmap(decodedImage);
                            fullscreenImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    });
                }
                else{
                    Glide.with(requireActivity())
                            .load(R.drawable.img_1)
                            .into(imageView);
                }


                //imageView.setImageResource(currentEvent.getImagePath());
                detail.setText(currentEvent.getDetails());
                title.setText(currentEvent.getTitle());

                title.setEnabled(false);
                detail.setEnabled(false);

                builder.setView(overlayView);

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void doubleClick(int position) {
                // Implement your logic for double click here
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        instance = null;
    }
}