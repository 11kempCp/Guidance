package com.example.guidance.Util;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.guidance.R;
import com.example.guidance.jobServices.AmbientTempJobService;
import com.example.guidance.jobServices.DailyQuestionJobService;
import com.example.guidance.jobServices.LocationJobService;
import com.example.guidance.jobServices.QuestionnaireJobService;
import com.example.guidance.jobServices.StepsJobService;
import com.example.guidance.jobServices.WeatherJobService;
import com.example.guidance.realm.model.Data_Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACTIVITY_RECOGNITION;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.example.guidance.realm.DatabaseFunctions.getDataType;
import static io.realm.Realm.getApplicationContext;


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
    public static final int QUESTIONNAIRE = 6;

    //  todo change back so that WEATHER is called

    public static final List<Integer> utilList = Arrays.asList(AMBIENT_TEMP, STEPS, LOCATION, DAILY_QUESTION, WEATHER, QUESTIONNAIRE);
//    public static final List<Integer> utilList = Arrays.asList(AMBIENT_TEMP, STEPS, LOCATION, DAILY_QUESTION, QUESTIONNAIRE);


    public static boolean scheduleJob(Context context, Class<?> serviceClass, int jobId, int minutes) {


        //todo requires internet connectivity
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

        requestPermsSteps(context, activity);
        requestPermsFineLocation(context, activity);


    }

    public static void requestPermsSteps(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, ACTIVITY_RECOGNITION)
                    == PackageManager.PERMISSION_DENIED) {
                //ask for permission
                requestPermissions(activity, new String[]{ACTIVITY_RECOGNITION}, STEPS);
            }
        }
    }

    public static void requestPermsFineLocation(Context context, Activity activity) {
        //TODO improve, refer to LocationUpdatesForegroundService github repo,
        // specifically MainActivity requestPermissions function
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, LOCATION);
        }
    }


    public static void scheduledUnscheduledJobs(Context context) {

        List<Integer> unscheduledJobs = getUnscheduledJobs(context);

        //TODO remove this implementation in favour of passcode implementation
//        Data_Type data;
//        if (!isDataTypeInitialised(context)) {
//            initialiseDataType(context);
//        }

        Data_Type data = getDataType(context);

        Date currentTime = Calendar.getInstance().getTime();

        if (data != null) {

            PackageManager packageManager = context.getPackageManager();
            for (int job : unscheduledJobs) {
                //TODO add more Jobs that are completed to the switch statement here
                switch (job) {
                    case AMBIENT_TEMP:

                        if (data.isAmbient_temp()) {
                            checkPermissionsAndSchedule(context,
                                    AMBIENT_TEMP,
                                    AmbientTempJobService.class,
                                    context.getResources().getInteger(R.integer.ambient_temp),
                                    packageManager,
                                    null,
                                    PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE);
                        }

                        break;
                    case STEPS:

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
                        //todo likely change permission and feature
                        // change ACCESS_FINE_LOCATION to ACCESS_COARSE_LOCATION
                        if (data.isLocation()) {
                            Log.d(TAG, "scheduledUnscheduledJobs: " + LOCATION);
                            checkPermissionsAndSchedule(context,
                                    LOCATION,
                                    LocationJobService.class,
                                    context.getResources().getInteger(R.integer.location),
                                    packageManager,
                                    ACCESS_FINE_LOCATION,
                                    null);

                        }
                        break;

                    case DAILY_QUESTION:
                        //TODO DAILY_QUESTION not working
                        //TODO change default time
                        if ((data.isSocialness() || data.isMood())) {
                            Log.d(TAG, "scheduledUnscheduledJobs: " + DAILY_QUESTION);

                            checkPermissionsAndSchedule(context,
                                    DAILY_QUESTION,
                                    DailyQuestionJobService.class,
                                    context.getResources().getInteger(R.integer.default_time),
                                    packageManager,
                                    null,
                                    null);

                        }
                        break;

                    case WEATHER:

                        if (data.isWeather() || data.isSun() || data.isExternal_temp()) {
                            Log.d(TAG, "scheduledUnscheduledJobs: " + WEATHER);
                            checkPermissionsAndSchedule(context,
                                    WEATHER,
                                    WeatherJobService.class,
                                    context.getResources().getInteger(R.integer.weather),
                                    packageManager,
                                    null,
                                    null);


                        }


                        break;
                    case QUESTIONNAIRE:
                        //todo potentially change to hourly?
                        checkPermissionsAndSchedule(context,
                                QUESTIONNAIRE,
                                QuestionnaireJobService.class,
                                context.getResources().getInteger(R.integer.daily),
                                packageManager,
                                null,
                                null);

                        break;
                }
            }

        } else {
            Log.d(TAG, "scheduledUnscheduledJobs: data == null");
        }


    }

    public static void checkPermissionsAndSchedule(Context context, int jobID, Class<?> cls, int minutes, PackageManager packageManager, String permission, String feature) {
        if (packageManager.hasSystemFeature(feature) || feature == null) {
            Log.d(TAG, feature + " feature " + true);

            if (permission == null) {
                boolean scheduled = Util.scheduleJob(context, cls, jobID, minutes);
                Log.d(TAG, "Scheduled " + jobID + " " + scheduled);

            } else if (checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission " + permission + true);
                boolean scheduled = Util.scheduleJob(context, cls, jobID, minutes);
                Log.d(TAG, "Scheduled " + jobID + " " + scheduled);
            } else {
                Log.d(TAG, "Permission " + permission + " " + false);

            }
        } else {
            Log.d(TAG, permission + " permission " + false);
        }
    }


    public static List<Integer> getUnscheduledJobs(Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

//        Log.d(TAG, "scheduleUnscheduledJobs: ");
        List<Integer> temp = new ArrayList<>();
        List<Integer> scheduledJobs = new ArrayList<>();

        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            scheduledJobs.add(jobInfo.getId());
        }


        for (int jobId : utilList) {

            if (jobScheduler.getPendingJob(jobId) == null) {
                if (!scheduledJobs.contains(jobId)) {
                    temp.add(jobId);
                }
            }


        }

        return temp;
    }


    public static boolean unscheduledJob(Context context, int jobID) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (jobScheduler.getPendingJob(jobID) != null) {
            jobScheduler.cancel(jobID);
            return true;
        }
        return false;


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


    public static Date convertEpochToDate(long t) {
        Date date = new Date();
        t = t * 1000;
        date.setTime(t);
        return date;
    }

    public static void stopBackgroundNotification(int notification_id) {
        Log.d(TAG, "stopBackgroundNotification: " + notification_id);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_id);
    }


}
