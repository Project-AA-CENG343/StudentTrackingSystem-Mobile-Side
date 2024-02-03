package com.example.birdaha.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.birdaha.Adapters.HomeworkAdapter;
import com.example.birdaha.Classrooms.Classroom;
import com.example.birdaha.General.HwModel;
import com.example.birdaha.General.UpdateRespond;
import com.example.birdaha.R;
import com.example.birdaha.Users.Teacher;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeworkDialogFragment extends DialogFragment {


    private EditText hw_title, hw_due_date, hw_content, hw_course_name;
    private Teacher teacher;
    private Classroom classroom;
    private String image;
    private Button hw_adding_image;
    private ImageView homeworkImage;

    HomeworkAdapter homeworkAdapter;

    ArrayList<HwModel> hwModels = new ArrayList<>();
    public HomeworkDialogFragment(HomeworkAdapter homeworkAdapter) {
        this.homeworkAdapter = homeworkAdapter;
    }

    private ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if(result != null){
                    Uri imageUri = result;
                    try{
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageUri);
                        Glide.with(requireContext())
                                .load(bitmap)
                                .into(homeworkImage);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,20,bos);
                        byte[] byteArray = bos.toByteArray();
                        image = Base64.encodeToString(byteArray,Base64.DEFAULT);
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
    );
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.full_screen_hw_adding_dialog, container, false);

        hw_title = view.findViewById(R.id.hw_title);
        hw_content = view.findViewById(R.id.hw_content);
        hw_due_date = view.findViewById(R.id.dateEditText);
        hw_adding_image = view.findViewById(R.id.hw_adding_image);
        homeworkImage = view.findViewById(R.id.homework_image);

        // Set up the date picker dialog
        hw_due_date.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(), // Use getActivity() as the context
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        // Format the date in your preferred format (yyyy-MM-dd)
                        String date = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        hw_due_date.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        hw_adding_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });

        // Initialize the close button
        ImageButton closeButton = view.findViewById(R.id.fullscreen_dialog_close);
        closeButton.setOnClickListener(v -> dismiss());

        // Initialize the action button
        TextView actionButton = view.findViewById(R.id.fullscreen_dialog_action);
        actionButton.setOnClickListener(v -> {

            if(TextUtils.isEmpty(hw_title.getText())){
                Toast.makeText(requireContext(), "Ödev adı boş", Toast.LENGTH_SHORT).show();
                return;
            }

                String title = hw_title.getText().toString();
                String content = hw_content.getText().toString();
                String due_date = hw_due_date.getText().toString();
                HwModel hwModel = new HwModel(classroom.getClassroom_id(), teacher.getTeacher_id(), teacher.getCourse().getName(), due_date, title, content, image);
                hwModel.setGetImage(image);
                homeworkAdapter.getHwModels().add(hwModel);
                homeworkAdapter.notifyDataSetChanged();

                        // Retrofit call
                        Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://sinifdoktoruadmin.online/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ClassroomHomeworkScreen.AddHomework addHomework = retrofit.create(ClassroomHomeworkScreen.AddHomework.class);
                addHomework.addHomework(hwModel).enqueue(new Callback<UpdateRespond>() {
                    @Override
                    public void onResponse(Call<UpdateRespond> call, Response<UpdateRespond> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Handle successful response
                            Toast.makeText(getActivity(), "Ödev başarıyla eklendi", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle unsuccessful response
                            //Log.d("ResponseError", new Gson().toJson(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateRespond> call, Throwable t) {
                        // Handle failure
                        //Log.d("Error", t.getMessage());
                    }
                });
                dismiss(); // Dismiss the dialog after action
            });

            //TextView dialog_title = view.findViewById(R.id.hw_title);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            teacher = (Teacher) getArguments().getSerializable("teacher");
            classroom = (Classroom) getArguments().getSerializable("classroom");
        }


    }





    // The system calls this only when creating the layout in a dialog.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        // Handle the dialog dismiss event
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        // Handle the dialog cancel event
    }



}