package com.example.guidance.scheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.example.guidance.model.Ambient_Temperature;
import com.example.guidance.sensorservices.AmbientTempJobService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Conor K on 15/02/2021.
 */
public class Util {

    private static final String TAG = "Util";

    // schedule the start of the service every 15mins
    public static void scheduleJob(Context context) {
        ComponentName componentName = new ComponentName(context, AmbientTempJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();


        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        int resultCode = jobScheduler.schedule(info);
//        int resultCode = scheduler.schedule(info);
        Date currentTime = Calendar.getInstance().getTime();

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job Scheduled " + currentTime);
        } else {
            Log.d(TAG, "Job Scheduling Failed " + " resultCode: " + resultCode + currentTime);
        }

    }
}
