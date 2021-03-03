package com.example.guidance.scheduler;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.guidance.R;
import com.example.guidance.jobServices.AmbientTempJobService;
import com.example.guidance.jobServices.DailyQuestionJobService;
import com.example.guidance.jobServices.LocationJobService;
import com.example.guidance.jobServices.StepsJobService;
import com.example.guidance.model.Data_Storing;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACTIVITY_RECOGNITION;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.example.guidance.realm.DatabaseFunctions.getDataStoring;
import static com.example.guidance.realm.DatabaseFunctions.initialiseDataStoring;
import static com.example.guidance.realm.DatabaseFunctions.isDataStoringInitialised;
import static com.example.guidance.realm.DatabaseFunctions.isMoodEntryToday;
import static com.example.guidance.realm.DatabaseFunctions.isSocialnessEntryToday;


/**
 * Created by Conor K on 15/02/2021.
 */
public class Util {

    private static final String TAG = "Util";

    public static final int AMBIENT_TEMP = 1;
    public static final int STEPS = 2;
    public static final int LOCATION = 3;
    public static final int DAILY_QUESTION = 4;
    public static final int WEATHER = 5;


    public static final List<Integer> utilList = Arrays.asList(AMBIENT_TEMP, STEPS, LOCATION, DAILY_QUESTION, WEATHER);


    public static boolean scheduleJob(Context context, Class<?> serviceClass, int jobId, int minutes) {

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
            Log.d(TAG, "Job " + jobId + " Scheduling Failed " + " resultCode: " + currentTime);
            return false;
        }
    }


    public static void requestPerms(Context context, Activity activity) {
//        ACTIVITY_RECOGNITION Request, needed for Step Counter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, ACTIVITY_RECOGNITION)
                    == PackageManager.PERMISSION_DENIED) {
                //ask for permission
                requestPermissions(activity, new String[]{ACTIVITY_RECOGNITION}, STEPS);
            }
        }

        //TODO improve, refer to LocationUpdatesForegroundService github repo,
        // specifically MainActivity requestPermissions function
        if (ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            requestPermissions(activity, new String[]{ACCESS_COARSE_LOCATION}, LOCATION);
        }

    }


    public static void scheduledUnscheduledJobs(Context context) {

        List<Integer> unscheduledJobs = unscheduledJobs(context);

        //TODO remove this implementation in favour of passcode implementation
        Data_Storing data;
        if (!isDataStoringInitialised(context)) {
            initialiseDataStoring(context);
        }

        data = getDataStoring(context);

        Date currentTime = Calendar.getInstance().getTime();

        if (data != null) {

            PackageManager packageManager = context.getPackageManager();
            for (int job : unscheduledJobs) {
                //TODO add more Jobs that are completed to the switch statement here
                switch (job) {
                    case AMBIENT_TEMP:

                        //TODO change minutes to a call from the strings file
                        if (data.isAmbient_temp()) {
                            checkPermissionsAndSchedule(context,
                                    AMBIENT_TEMP,
                                    AmbientTempJobService.class,
                                    context.getResources().getInteger(R.integer.ambient_temp),
                                    packageManager,
                                    "none",
                                    PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE);
                        }


//                    text = "AMBIENT_TEMP ";
//                    // change from 15 minutes to another time schedule
//
//                    if (packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE) & data.isAmbient_temp()) {
//                        boolean scheduled = Util.scheduleJob(context, AmbientTempJobService.class, AMBIENT_TEMP, 15);
//                        Log.d(TAG, "Scheduled " + text + scheduled);
//                    } else {
//                        Log.d(TAG, "FEATURE_SENSOR_AMBIENT_TEMPERATURE = false");
//                    }


                        break;
                    case STEPS:
//                    text = "STEPS ";
//                    permission = ACTIVITY_RECOGNITION;
//                    tfs = PackageManager.FEATURE_SENSOR_STEP_COUNTER;
//
//                    if (packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER) & data.isSteps()) {
//                        Log.d(TAG, "FEATURE_SENSOR_STEP_COUNTER " + true);
//                        if (checkSelfPermission(context, ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
//                            Log.d(TAG, "Permission " + permission + true);
//                            // change from 15 minutes to once per day
//                            boolean scheduled = Util.scheduleJob(context, StepsJobService.class, STEPS, 15);
//                            Log.d(TAG, "Scheduled " + text + scheduled);
//                        }
//                    } else {
//                        Log.d(TAG, tfs + false);
//                    }

                        if (data.isSteps()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                checkPermissionsAndSchedule(context,
                                        STEPS,
                                        StepsJobService.class,
                                        context.getResources().getInteger(R.integer.steps),
                                        packageManager,
                                        ACTIVITY_RECOGNITION,
                                        PackageManager.FEATURE_SENSOR_STEP_COUNTER);
                            }
                        }


                        break;
                    case LOCATION:
                        //todo change from default time
                        //todo likely change permission and feature
                        if (data.isLocation()) {
                            Log.d(TAG, "scheduledUnscheduledJobs: " + LOCATION);
                            checkPermissionsAndSchedule(context,
                                    LOCATION,
                                    LocationJobService.class,
                                    context.getResources().getInteger(R.integer.default_time),
                                    packageManager,
                                    ACCESS_COARSE_LOCATION,
                                    "none");

                        }
                        break;

                    case DAILY_QUESTION:
                        if ((data.isSocialness() || data.isMood()) && (!isMoodEntryToday(context, currentTime) || !isSocialnessEntryToday(context, currentTime))) {
                            Log.d(TAG, "scheduledUnscheduledJobs: " + DAILY_QUESTION);

                            checkPermissionsAndSchedule(context,
                                    DAILY_QUESTION,
                                    DailyQuestionJobService.class,
                                    context.getResources().getInteger(R.integer.daily_question),
                                    packageManager,
                                    "none",
                                    "none");

                        }
                        break;

                    case WEATHER:

                        if (data.isWeather() || data.isSun()) {
                            Log.d(TAG, "scheduledUnscheduledJobs: " + WEATHER);
                        }
                        break;
                }
            }

        } else {
            Log.d(TAG, "scheduledUnscheduledJobs: data == null");
        }


    }

    public static void checkPermissionsAndSchedule(Context context, int utilInt, Class<?> cls, int minutes, PackageManager packageManager, String permission, String feature) {
        if (packageManager.hasSystemFeature(feature) || feature.equals("none")) {
            Log.d(TAG, feature + " feature " + true);

            if (permission.equals("none")) {
                boolean scheduled = Util.scheduleJob(context, cls, utilInt, minutes);
                Log.d(TAG, "Scheduled " + utilInt + " " + scheduled);

            } else if (checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission " + permission + true);
                boolean scheduled = Util.scheduleJob(context, cls, utilInt, minutes);
                Log.d(TAG, "Scheduled " + utilInt + " " + scheduled);
            } else {
                Log.d(TAG, "Permission " + permission + " " + false);

            }
        } else {
            Log.d(TAG, permission + " permission " + false);
        }
    }

    public static List<Integer> unscheduledJobs(Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

//        Log.d(TAG, "scheduleUnscheduledJobs: ");
        List<Integer> temp = new ArrayList<>();
        List<Integer> testing = new ArrayList<>();

        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            testing.add(jobInfo.getId());
        }


        for (int something : utilList) {

            if (jobScheduler.getPendingJob(something) == null) {
                if (!testing.contains(something)) {
                    temp.add(something);
                }
            }


        }

        return temp;
    }


    public static boolean isJobScheduled(Context context, int JobId) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JobId) {
                Log.d(TAG, "isJobScheduled: " + JobId + " " + true);
                return true;
            }
        }
        Log.d(TAG, "isJobScheduled: " + JobId + " " + false);
        return false;
    }


    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";




}
