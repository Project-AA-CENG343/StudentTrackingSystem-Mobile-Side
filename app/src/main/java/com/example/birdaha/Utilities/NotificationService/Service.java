package com.example.birdaha.Utilities.NotificationService;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class Service {

    private Service() {
    }

    public static void start(Class<? extends JobService> cls, Context context, int jobId, String jobName)
    {
        ComponentName componentName = new ComponentName(context, cls);
        JobInfo info = new JobInfo.Builder(jobId, componentName)
                .setPeriodic(1)
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("JobScheduler", "Job scheduled for "+jobName);
        }
    }
}
