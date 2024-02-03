package com.example.birdaha.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.birdaha.Activities.MainActivity;
import com.example.birdaha.Helper.LocalDataManager;
import com.example.birdaha.R;
import com.example.birdaha.Users.ChangePasswordParent;
import com.example.birdaha.Users.ChangePasswordStudent;
import com.example.birdaha.Users.ChangePasswordTeacher;
import com.example.birdaha.Users.UserRespond;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The NotificationFragment class represents a fragment displaying notifications.
 * This fragment inflates a layout to display notifications and handles any necessary view setup.
 */
public class NotificationFragment extends Fragment {

    /**
     * Key to retrieve the title content.
     */
    private static final String KEY_TITLE="Content";

    /**
     * Empty constructor for the NotificationFragment.
     */
    public NotificationFragment() {
        // Required empty public constructor
    }

    private static NotificationFragment instance = null;

    /**
     * Creates a new instance of the NotificationFragment.
     *
     * @param param1 Title content to be displayed.
     * @return A new instance of NotificationFragment.
     */
    public static NotificationFragment newInstance(String param1,String name,int userId,String userType,long schoolNo) {
        if(instance == null)
        {
            instance = new NotificationFragment();
            Bundle args = new Bundle();
            args.putString("name",name);
            args.putInt("userId",userId);
            args.putString("userType",userType);
            args.putLong("schoolNo",schoolNo);
            args.putString(KEY_TITLE, param1);
            instance.setArguments(args);
        }

        return instance;
    }

    /**
     * Called when the fragment is created.
     *
     * @param savedInstanceState A Bundle containing the saved state of the fragment,
     *                            or null if there is no saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing the saved state of the fragment,
     *                           or null if there is no saved state.
     * @return The root View of the fragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preferences,container,false);

        TextInputEditText oldPassword = view.findViewById(R.id.oldPassword);
        TextInputEditText newPassword = view.findViewById(R.id.newPassword);
        TextInputEditText confirmPassword = view.findViewById(R.id.confirmPassword);

        TextView name = view.findViewById(R.id.preferencesName);
        TextView id = view.findViewById(R.id.preferencesId);

        Button changePassword = view.findViewById(R.id.changePasswordButton);

        Bundle bundle = getArguments();
        if(bundle != null){
            int userId = bundle.getInt("userId");
            name.setText(bundle.getString("name"));
            long schoolno = bundle.getLong("schoolNo");
            if(schoolno != 0){
                id.setText(String.valueOf(schoolno));
            }
            else{
                id.setVisibility(View.INVISIBLE);
            }
            //id.setText(String.valueOf(userId));

            String userType = bundle.getString("userType");
            if(userType.equals("teacher")){
                changePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String oldPw = LocalDataManager.getSharedPreference(requireContext().getApplicationContext(),"password","");
                        if(!oldPassword.getText().toString().equals(oldPw)){
                            Toast.makeText(requireContext(), "Eski şifrenizi yanlış girdiniz", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(newPassword.getText()) || TextUtils.isEmpty(confirmPassword.getText()) || TextUtils.isEmpty(oldPassword.getText())){
                            Toast.makeText(requireContext(), "Lütfen gerekli yerleri doldurun", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                            Toast.makeText(requireContext(), "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(newPassword.getText().toString().equals(oldPassword.getText().toString())){
                            Toast.makeText(requireContext(), "Yeni şifreniz eskisi ile aynı olamaz", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://sinifdoktoruadmin.online/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ChangePasswordTeacher teacherChange = new ChangePasswordTeacher(userId,newPassword.getText().toString());
                        MainActivity.RequestUser changePassword = retrofit.create(MainActivity.RequestUser.class);
                        changePassword.changePasswordTeacher(teacherChange).enqueue(new Callback<UserRespond>() {
                            @Override
                            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                                if(response.isSuccessful() && response.body() != null){
                                    Toast.makeText(requireContext(), "Şifreniz başarıyla değiştirildi!", Toast.LENGTH_SHORT).show();
                                    LocalDataManager.setSharedPreference(requireContext().getApplicationContext(), "password", newPassword.getText().toString());
                                    oldPassword.setText("");
                                    newPassword.setText("");
                                    confirmPassword.setText("");

                                }
                                else{
                                    Toast.makeText(requireContext(), "Bir hata oluştu " + response.code() , Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserRespond> call, Throwable t) {
                                //Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
            else if(userType.equals("student")){
                changePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String oldPw = LocalDataManager.getSharedPreference(requireContext().getApplicationContext(),"password","");
                        if(!oldPassword.getText().toString().equals(oldPw)){
                            Toast.makeText(requireContext(), "Eski şifrenizi yanlış girdiniz", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(newPassword.getText()) || TextUtils.isEmpty(confirmPassword.getText()) || TextUtils.isEmpty(oldPassword.getText())){
                            Toast.makeText(requireContext(), "Lütfen gerekli yerleri doldurun", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                            Toast.makeText(requireContext(), "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(newPassword.getText().toString().equals(oldPassword.getText().toString())){
                            Toast.makeText(requireContext(), "Yeni şifreniz eskisi ile aynı olamaz", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://sinifdoktoruadmin.online/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ChangePasswordStudent studentChange = new ChangePasswordStudent(userId,newPassword.getText().toString());
                        MainActivity.RequestUser changePassword = retrofit.create(MainActivity.RequestUser.class);
                        changePassword.changePasswordStudent(studentChange).enqueue(new Callback<UserRespond>() {
                            @Override
                            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                                if(response.isSuccessful() && response.body() != null){
                                    Toast.makeText(requireContext(), "Şifreniz başarıyla değiştirildi!", Toast.LENGTH_SHORT).show();
                                    LocalDataManager.setSharedPreference(requireContext().getApplicationContext(), "password", newPassword.getText().toString());
                                    oldPassword.setText("");
                                    newPassword.setText("");
                                    confirmPassword.setText("");

                                }
                                else{
                                    Toast.makeText(requireContext(), "Bir hata oluştu " + response.code() , Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserRespond> call, Throwable t) {
                                //Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
            else if(userType.equals("parent")){
                changePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String oldPw = LocalDataManager.getSharedPreference(requireContext().getApplicationContext(),"password","");
                        if(!oldPassword.getText().toString().equals(oldPw)){
                            Toast.makeText(requireContext(), "Eski şifrenizi yanlış girdiniz", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(newPassword.getText()) || TextUtils.isEmpty(confirmPassword.getText()) || TextUtils.isEmpty(oldPassword.getText())){
                            Toast.makeText(requireContext(), "Lütfen gerekli yerleri doldurun", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                            Toast.makeText(requireContext(), "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(newPassword.getText().toString().equals(oldPassword.getText().toString())){
                            Toast.makeText(requireContext(), "Yeni şifreniz eskisi ile aynı olamaz", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://sinifdoktoruadmin.online/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        ChangePasswordParent parentChange = new ChangePasswordParent(userId,newPassword.getText().toString());
                        MainActivity.RequestUser changePassword = retrofit.create(MainActivity.RequestUser.class);
                        changePassword.changePasswordParent(parentChange).enqueue(new Callback<UserRespond>() {
                            @Override
                            public void onResponse(Call<UserRespond> call, Response<UserRespond> response) {
                                if(response.isSuccessful() && response.body() != null){
                                    Toast.makeText(requireContext(), "Şifreniz başarıyla değiştirildi!", Toast.LENGTH_SHORT).show();
                                    LocalDataManager.setSharedPreference(requireContext().getApplicationContext(), "password", newPassword.getText().toString());
                                    oldPassword.setText("");
                                    newPassword.setText("");
                                    confirmPassword.setText("");

                                }
                                else{
                                    Toast.makeText(requireContext(), "Bir hata oluştu " + response.code() , Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserRespond> call, Throwable t) {
                                //Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }
        SwitchCompat notification = view.findViewById(R.id.notificationsSwitch);
        SwitchCompat vibration = view.findViewById(R.id.vibrateSwitch);
        SwitchCompat sound = view.findViewById(R.id.soundSwitch);

        notification.setChecked(LocalDataManager.getSharedPreference(requireContext(), "notification", "notifications", true));
        sound.setChecked(LocalDataManager.getSharedPreference(requireContext(), "sound", "notifications", true));
        vibration.setChecked(LocalDataManager.getSharedPreference(requireContext(), "vibration", "notifications", true));

        notification.setOnCheckedChangeListener((switchView, isChecked) -> {
            LocalDataManager.setSharedPreference(requireContext(), "notification",  isChecked, "notifications");
        });

        sound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LocalDataManager.setSharedPreference(requireContext(), "sound",  isChecked, "notifications");
        });

        vibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LocalDataManager.setSharedPreference(requireContext(), "vibration",  isChecked, "notifications");
        });

        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDataManager.clearSharedPreference(requireContext().getApplicationContext());
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });


        return view;

        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        /*View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        Context context = requireContext();

        Switch notification = view.findViewById(R.id.Switch_show_notifications);
        Switch sound = view.findViewById(R.id.Switch_voice);
        Switch vibration = view.findViewById(R.id.Switch_vibration);

        notification.setChecked(LocalDataManager.getSharedPreference(context, "notification", "notifications", true));
        sound.setChecked(LocalDataManager.getSharedPreference(context, "sound", "notifications", true));
        vibration.setChecked(LocalDataManager.getSharedPreference(context, "vibration", "notifications", true));

        notification.setOnCheckedChangeListener((switchView, isChecked) -> {
            LocalDataManager.setSharedPreference(context, "notification",  isChecked, "notifications");
        });

        sound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LocalDataManager.setSharedPreference(context, "sound",  isChecked, "notifications");
        });

        vibration.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LocalDataManager.setSharedPreference(context, "vibration",  isChecked, "notifications");
        });

        return view;*/
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored
     * in to the view. This gives subclasses a chance to initialize themselves once they know their view
     * hierarchy has been completely created.
     *
     * @param view               The View returned by onCreateView().
     * @param savedInstanceState A Bundle containing the saved state of the fragment,
     *                           or null if there is no saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*String title = getArguments().getString(KEY_TITLE);
        ((TextView)view.findViewById(R.id.title)).setText(title);*/


    }

    @Override
    public void onDetach(){
        super.onDetach();
        instance = null;
    }
}