package com.example.guidance.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.guidance.R;
import com.example.guidance.jobServices.AdviceJobService;
import com.example.guidance.jobServices.AmbientTempJobService;
import com.example.guidance.jobServices.DailyQuestionJobService;
import com.example.guidance.jobServices.LocationJobService;
import com.example.guidance.jobServices.QuestionnaireJobService;
import com.example.guidance.jobServices.ScreentimeJobService;
import com.example.guidance.jobServices.StepsJobService;
import com.example.guidance.jobServices.WeatherJobService;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACTIVITY_RECOGNITION;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.example.guidance.Util.IA.FEMALE;
import static com.example.guidance.Util.IA.MALE;
import static com.example.guidance.Util.IA.NO_JUSTIFICATION;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
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
    public static final int SCREENTIME = 7;
    public static final int ADVICE = 8;

    //  todo change back so that WEATHER is called

//        public static final List<Integer> utilList = Arrays.asList(AMBIENT_TEMP, STEPS, LOCATION, DAILY_QUESTION, WEATHER, QUESTIONNAIRE, SCREENTIME, ADVICE);
    public static final List<Integer> utilList = Arrays.asList(AMBIENT_TEMP, STEPS, LOCATION, DAILY_QUESTION, QUESTIONNAIRE, SCREENTIME, ADVICE);

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

    public static boolean scheduleJob(Context context, Class<?> serviceClass, int jobId, int minutes, int networkType) {

        Date currentTime = Calendar.getInstance().getTime();

        ComponentName componentName = new ComponentName(context, serviceClass);
        JobInfo info = new JobInfo.Builder(jobId, componentName)
                .setPersisted(true)
                .setPeriodic(minutes * 60 * 1000)
                .setRequiredNetworkType(networkType)
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

        requestPermsSteps(context, activity);
        requestPermsFineLocation(context, activity);
    }

    public static void requestPermsSteps(Context context, Activity activity) {
        Log.d(TAG, "requestPermsSteps: " + activity.getPackageName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
                //ask for permission
                requestPermissions(activity, new String[]{ACTIVITY_RECOGNITION}, STEPS);
            }
        }
    }

    public static void requestPermsFineLocation(Context context, Activity activity) {
        Log.d(TAG, "requestPermsFineLocation: " + activity.getPackageName());

        //TODO improve, refer to LocationUpdatesForegroundService github repo,
        // specifically MainActivity requestPermissions function
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, LOCATION);
        }
    }

    public static boolean isPermsUsageStats(Context context) {

        boolean granted;
        AppOpsManager appOps = (AppOpsManager) context
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        return granted;
    }

    public static boolean isPermsLocation(Context context) {
        return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isPermsSteps(Context context) {
        return ContextCompat.checkSelfPermission(context, ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED;
    }


    public static void scheduledUnscheduledJobs(Context context) {

        List<Integer> unscheduledJobs = getUnscheduledJobs(context);

        Data_Type data = getDataType(context);

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
                                    ACCESS_FINE_LOCATION);

                        }
                        break;

                    case DAILY_QUESTION:
                        if ((data.isSocialness() || data.isMood())) {
                            Log.d(TAG, "scheduledUnscheduledJobs: " + DAILY_QUESTION);
                            scheduleDailyQuestions(context);
                        }
                        break;

                    case WEATHER:

                        if (data.isWeather() || data.isSun() || data.isExternal_temp()) {
                            Log.d(TAG, "scheduledUnscheduledJobs: " + WEATHER);

                            scheduleWeather(context);
                        }

                        break;
                    case QUESTIONNAIRE:
                        //todo potentially change to hourly?
                        scheduleJob(context, QuestionnaireJobService.class, QUESTIONNAIRE, context.getResources().getInteger(R.integer.daily));
                        break;

                    case SCREENTIME:
                        if (data.isScreentime()) {
                            scheduleScreentime(context);
                        }
                        break;
                    case ADVICE:
                        scheduleAdvice(context);
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

    public static void checkPermissionsAndSchedule(Context context, int jobID, Class<?> cls, int minutes, PackageManager packageManager, String feature) {
        if (packageManager.hasSystemFeature(feature)) {
            Log.d(TAG, feature + " feature " + true);


            boolean scheduled = Util.scheduleJob(context, cls, jobID, minutes);
            Log.d(TAG, " scheduled " + scheduled);

        } else {
            Log.d(TAG, feature + " feature " + false);
        }
    }

    public static void checkPermissionsAndSchedule(Context context, int jobID, Class<?> cls, int minutes, String permission) {


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

    }


    public static List<Integer> getUnscheduledJobs(Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

//        Log.d(TAG, "scheduleUnscheduledJobs: ");
        List<Integer> unscheduledJobs = new ArrayList<>();
        List<Integer> scheduledJobs = new ArrayList<>();

        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            scheduledJobs.add(jobInfo.getId());
        }


        for (int jobId : utilList) {

            if (jobScheduler.getPendingJob(jobId) == null) {
                if (!scheduledJobs.contains(jobId)) {
                    unscheduledJobs.add(jobId);
                }
            }


        }

        return unscheduledJobs;
    }


    public static boolean unscheduledJob(Context context, int jobID) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (jobScheduler.getPendingJob(jobID) != null) {
            jobScheduler.cancel(jobID);
            Log.d(TAG, "unscheduledJob: " + jobID);
            return true;
        }
        Log.d(TAG, "unscheduledJob: Job " + jobID + " not scheduled");
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
        NotificationManager notificationManager = (NotificationManager) Objects.requireNonNull(getApplicationContext()).getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_id);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) Objects.requireNonNull(getApplicationContext()).getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static void setToolbarColor(Intelligent_Agent intelligent_agent, Toolbar toolbar, Resources resources) {
        if (intelligent_agent != null) {
            String gender = intelligent_agent.getGender();
            if (gender.equals(FEMALE)) {
                Log.d(TAG, "onCreate: femaleAppTheme_NoActionBar");
                toolbar.setBackgroundColor(resources.getColor(R.color.femalePrimaryColour));
            } else if (gender.equals(MALE)) {
                Log.d(TAG, "onCreate: malePrimaryColour");

                toolbar.setBackgroundColor(resources.getColor(R.color.malePrimaryColour));
            } else {
                Log.d(TAG, "onCreate: defaultPrimaryColour");
                toolbar.setBackgroundColor(resources.getColor(R.color.defaultPrimaryColour));
            }
        }
    }

    public static void setActivityTheme(Intelligent_Agent intelligent_agent, Activity activity) {
        if (intelligent_agent != null) {
            String gender = intelligent_agent.getGender();
            if (gender.equals(FEMALE)) {
                Log.d(TAG, "onCreate: setTheme femaleAppTheme_NoActionBar");
                activity.setTheme(R.style.femaleAppTheme_NoActionBar);
            } else if (gender.equals(MALE)) {
                Log.d(TAG, "onCreate: setTheme maleAppTheme_NoActionBar");

                activity.setTheme(R.style.maleAppTheme_NoActionBar);
            } else {
                Log.d(TAG, "onCreate: setTheme defaultAppTheme_NoActionBar");
                activity.setTheme(R.style.defaultAppTheme_NoActionBar);
            }
        }
    }

    public static void navigationViewVisibility(NavigationView navigationView, Intelligent_Agent intelligent_agent, Data_Type dataType) {

        navigationViewIntelligentAgent(navigationView, intelligent_agent);
        navigationViewDailyQuestion(navigationView, dataType);

    }

    public static void navigationViewIntelligentAgent(NavigationView navigationView, Intelligent_Agent intelligent_agent) {
        if (intelligent_agent != null) {
            navigationView.getMenu().findItem(R.id.nav_justification).setVisible(!intelligent_agent.getAdvice().equals(NO_JUSTIFICATION));
        }
    }

    public static void navigationViewDailyQuestion(NavigationView navigationView, Data_Type dataType) {
        if (dataType != null) {

            if (!dataType.isMood() && !dataType.isSocialness()) {
                navigationView.getMenu().findItem(R.id.nav_daily_question).setVisible(false);
            }

        }
    }

    public static void scheduleAmbientTemp(Context context) {

    }

    public static boolean scheduleDailyQuestions(Context context) {
        return scheduleJob(context, DailyQuestionJobService.class, DAILY_QUESTION, context.getResources().getInteger(R.integer.daily_question));
    }

    public static void scheduleLocation(Context context) {

    }

    public static void scheduleQuestionnaire(Context context) {

    }

    public static boolean scheduleScreentime(Context context) {
        return scheduleJob(context, ScreentimeJobService.class, SCREENTIME, context.getResources().getInteger(R.integer.screentime));
    }

    public static void scheduleSteps(Context context) {
    }

    public static boolean scheduleWeather(Context context) {
        return scheduleJob(context, WeatherJobService.class, WEATHER, context.getResources().getInteger(R.integer.weather), JobInfo.NETWORK_TYPE_UNMETERED);
    }

    public static boolean scheduleAdvice(Context context) {
        //todo change from default time
        return scheduleJob(context, AdviceJobService.class, ADVICE, context.getResources().getInteger(R.integer.default_time));
    }


}
