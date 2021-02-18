package com.example.guidance.scheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.example.guidance.sensorservices.AmbientTempJobService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Conor K on 15/02/2021.
 */
public class Util {

    private static final String TAG = "Util";

    public static final int AMBIENT_TEMP = 1;
    public static final int STEPS = 2;


    public static boolean scheduleJob(Context context, Class<?> serviceClass, int jobId, int minutes) {

//        if(isJobScheduled(context,jobId)){
//            Log.d(TAG, "Job Already Scheduled");
//            return true;
//        }

        Date currentTime = Calendar.getInstance().getTime();

        ComponentName componentName = new ComponentName(context, serviceClass);
        JobInfo info = new JobInfo.Builder(jobId, componentName)
                .setPersisted(true)
                .setPeriodic(minutes * 60 * 1000)
                .build();


        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        int resultCode = jobScheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job " + jobId + "  Scheduled " + currentTime);
            return true;
        } else {
            Log.d(TAG, "Job "+ jobId + " Scheduling Failed " + " resultCode: " + currentTime);
            return false;
        }
    }



    //TODO ensure that this is redundant
    public static boolean isJobScheduled(Context context, int JobId) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JobId) {
                Log.d(TAG, "isJobScheduled: " + JobId + " " + true);
                return true;
            }
        }
        Log.d(TAG, "isJobScheduled: " + JobId + " " +  false);
        return false;
    }


//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.d("Service status", "Running");
//                return true;
//            }
//        }
//        Log.d("Service status", "Not running");
//        return false;
//    }


}
