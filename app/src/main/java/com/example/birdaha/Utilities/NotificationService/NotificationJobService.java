package com.example.birdaha.Utilities.NotificationService;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.birdaha.General.NotificationDataModel;
import com.example.birdaha.General.NotificationModel;
import com.example.birdaha.General.StudentSharedPrefModel;
import com.example.birdaha.Helper.LocalDataManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class NotificationJobService extends JobService {
    interface NotificationRetrofit{
        @GET("api/v1/notification/{studentId}")
        Call<NotificationModel> getNotifications(@Path("studentId") int studentId);
    }

    private final String TITLE = "Sınıf Bilgilendirmesi";
    private final String CHANNEL_ID = "NOTIFICATION";

    private static NotificationModel model = new NotificationModel();

    @Override
    public boolean onStartJob(JobParameters params) {
        // Sunucuya istek gönderme işlemini burada gerçekleştirin

        String studentsArrayJson = LocalDataManager.getSharedPreference(getApplicationContext(), "studentsArray", NotificationDataModel.getDefaultJson());
        NotificationDataModel notificationDataModel = NotificationDataModel.fromJson(studentsArrayJson);

        new Thread( () -> {
            for(StudentSharedPrefModel student : notificationDataModel.getStudents())
            {
                fetchNotification(student.getStudentId(), new NotificationCallback() {

                    @Override
                    public void onCompleted() {
                        String content = getContent(student);
                        if(!content.equals(""))
                        {
                            Notification.builder(getApplicationContext(), TITLE, content, "NOTIFICATION", student.getStudentId());
                        }
                    }

                });

            }
        }).start();

        return false; // İş hala devam ediyorsa true, tamamlandıysa false döndürün
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // İş durdurulduğunda yapılacaklar
        return false; // İşi yeniden başlatmak istiyorsanız true döndürün
    }



    public static NotificationModel fetchNotification(int studentId, NotificationCallback callback)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sinifdoktoruadmin.online/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NotificationRetrofit getNotification = retrofit.create(NotificationRetrofit.class);
        getNotification.getNotifications(studentId).enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    model.setModel((NotificationModel) response.body(), response.code());
                }
                else{
                    //Toast.makeText(getApplicationContext(), "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                }

                callback.onCompleted();
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onCompleted();
            }


        });

        return model;
    }


    public static NotificationModel fetchNotification(int studentId)
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sinifdoktoruadmin.online/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NotificationRetrofit getNotification = retrofit.create(NotificationRetrofit.class);
        getNotification.getNotifications(studentId).enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    model.setModel((NotificationModel) response.body(), response.code());
                }
                else{
                    //Toast.makeText(getApplicationContext(), "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return model;
    }

    private String getContent(StudentSharedPrefModel student)
    {
        int responseCode = model.getResponseCode();
        if(responseCode == 200)
        {
            if(model.getHomeworkId() != student.getLastHomeworkId() && model.getAnnouncementId() != student.getLastAnnouncementId())
                return student.getClassroomName() + " sınıfında yeni ödev ve duyuru var";
            else if(model.getAnnouncementId() != student.getLastAnnouncementId())
                return student.getClassroomName() + " sınıfında yeni duyuru var";
            else if (model.getHomeworkId() != student.getLastHomeworkId())
                return student.getClassroomName() + " sınıfında yeni ödev var";
        }
        else if((responseCode == 202) && model.getAnnouncementId() != student.getLastAnnouncementId())
            return student.getClassroomName() + " sınıfında yeni duyuru var";
        else if ((responseCode == 203) && model.getHomeworkId() != student.getLastHomeworkId())
            return student.getClassroomName() + " sınıfında yeni ödev var";

        return  "";
    }


    private interface NotificationCallback {
        void onCompleted();
    }

}
