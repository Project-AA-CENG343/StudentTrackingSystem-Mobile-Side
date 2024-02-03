package com.example.birdaha.Utilities.NotificationService;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.birdaha.Activities.MainActivity;
import com.example.birdaha.Helper.LocalDataManager;
import com.example.birdaha.R;

import java.util.List;

public class Notification {
    private Notification() {
    }

    private static void createNotificationChannel(Context context, CharSequence name, String description, String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.deleteNotificationChannel(CHANNEL_ID);

            notificationManager.createNotificationChannel(channel);
        }

    }

    public static void builder(Context context, String title, String content, String CHANNEL_ID, int notificationId) {

        boolean isSoundEnabled = LocalDataManager.getSharedPreference(context, "sound", "notifications", true);
        boolean isNotificationEnabled = LocalDataManager.getSharedPreference(context, "notification", "notifications", true);
        boolean isVibrateEnabled = LocalDataManager.getSharedPreference(context, "vibration", "notifications", true);

        if (isNotificationEnabled) {
            createNotificationChannel(context, "name", "desc", CHANNEL_ID);

            PendingIntent pendingIntent;
            if(!isAppInForeground(context))
            {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                );

            }
            else {
                pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setGroup("CLASS_NOTIFICATIONS")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(notificationId, builder.build());

        }
    }

    private static boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

}
