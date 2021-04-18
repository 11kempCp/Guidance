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
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.guidance.R;
import com.example.guidance.jobServices.AdviceFollowedJobService;
import com.example.guidance.jobServices.AdviceJobService;
import com.example.guidance.jobServices.AmbientTempJobService;
import com.example.guidance.jobServices.DailyQuestionJobService;
import com.example.guidance.jobServices.ExportJobService;
import com.example.guidance.jobServices.LocationJobService;
import com.example.guidance.jobServices.QuestionnaireJobService;
import com.example.guidance.jobServices.ScreentimeJobService;
import com.example.guidance.jobServices.StepsJobService;
import com.example.guidance.jobServices.WeatherJobService;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.google.android.material.navigation.NavigationView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
import static com.example.guidance.Util.IA.withJustification;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.isAllDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.isQuestionaireAnswered;
import static com.example.guidance.realm.databasefunctions.WeatherDatabaseFunctions.isExistingWeatherWeek;
import static io.realm.Realm.getApplicationContext;


/**
 * Created by Conor K on 15/02/2021.
 */
public class Util {

    private static final String TAG = "Util";

    /**
     * Integers relating to the jobID of each JobService, also used for each notification
     */
    public static final int AMBIENT_TEMP = 1;
    public static final int STEPS = 2;
    public static final int LOCATION = 3;
    public static final int DAILY_QUESTION = 4;
    public static final int WEATHER = 5;
    public static final int QUESTIONNAIRE = 6;
    public static final int SCREENTIME = 7;
    public static final int ADVICE = 8;
    public static final int ADVICE_FOLLOWED = 9;
    public static final int EXPORT = 10;

    //  todo change back so that WEATHER is called
    /**
     * List of the Above jobID's, used to identify which jobs are currently not scheduled as well as schedule them
     */
//    public static final List<Integer> utilList = Arrays.asList(AMBIENT_TEMP, STEPS, LOCATION, DAILY_QUESTION, QUESTIONNAIRE, SCREENTIME, ADVICE, ADVICE_FOLLOWED);
    public static final List<Integer> utilList = Arrays.asList();
//    public static final List<Integer> utilList = Arrays.asList(AMBIENT_TEMP, STEPS, LOCATION, DAILY_QUESTION,WEATHER, QUESTIONNAIRE, SCREENTIME, ADVICE ,ADVICE_FOLLOWED);

    /**
     * @param context      Context allows access to application-specific resources and classes
     * @param serviceClass The jobClass being scheduled
     * @param jobId        The jobId of the job to be scheduled
     * @param minutes      how often the job should be periodically scheduled
     * @return returns if the job has been scheduled or not
     */
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

    /**
     * @param context      Context allows access to application-specific resources and classes
     * @param serviceClass The jobClass being scheduled
     * @param jobId        The jobId of the job to be scheduled
     * @return returns if the job has been scheduled or not
     */
    public static boolean scheduleJob(Context context, Class<?> serviceClass, int jobId) {
        Date currentTime = Calendar.getInstance().getTime();
        ComponentName componentName = new ComponentName(context, serviceClass);
        JobInfo info = new JobInfo.Builder(jobId, componentName)
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

    /**
     * @param context            Context allows access to application-specific resources and classes
     * @param serviceClass       The jobClass being scheduled
     * @param jobId              The jobId of the job to be scheduled
     * @param minutes            how often the job should be periodically scheduled
     * @param requiresDeviceIdle if the device needs to be idle for the job to function
     * @return returns if the job has been scheduled or not
     */
    public static boolean scheduleJob(Context context, Class<?> serviceClass, int jobId, int minutes, boolean requiresDeviceIdle) {
        Date currentTime = Calendar.getInstance().getTime();
        ComponentName componentName = new ComponentName(context, serviceClass);
        JobInfo info = new JobInfo.Builder(jobId, componentName)
                .setPersisted(true)
                .setPeriodic(minutes * 60 * 1000)
                .setRequiresDeviceIdle(requiresDeviceIdle)
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

    /**
     * @param context      Context allows access to application-specific resources and classes
     * @param serviceClass The jobClass being scheduled
     * @param jobId        The jobId of the job to be scheduled
     * @param minutes      how often the job should be periodically scheduled
     * @param networkType  the network type required for the job
     * @return returns if the job has been scheduled or not
     */
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

    /**
     * Blanket call to request all permissions
     *
     * @param context  Context allows access to application-specific resources and classes
     * @param activity The activity in which the function call is made from
     */

    public static void requestPerms(Context context, Activity activity) {

        requestPermsSteps(context, activity);
        requestPermsFineLocation(context, activity);
    }

    /**
     * requests permissions to access the ACTIVITY_RECOGNITION permission,
     * for steps functionality
     *
     * @param context  Context allows access to application-specific resources and classes
     * @param activity The activity in which the function call is made from
     */

    public static void requestPermsSteps(Context context, Activity activity) {
        Log.d(TAG, "requestPermsSteps: " + activity.getPackageName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
                //ask for permission
                requestPermissions(activity, new String[]{ACTIVITY_RECOGNITION}, STEPS);
            }
        }
    }

    /**
     * requests permissions to access the ACCESS_FINE_LOCATION permission,
     * for location functionality. On API 29+ the highest permission this can request is while the application is in use.
     * To get 24/7 access to location user needs to externally assign 24/7 permission to the application
     *
     * @param context  Context allows access to application-specific resources and classes
     * @param activity The activity in which the function call is made from
     */

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

    /**
     * requests permissions to access the device usage stats,
     * relevant for the viewing a users screentime stats.
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns if the permission has been granted
     */

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

    /**
     * checks if location permissions have been granted
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns true if ACCESS_FINE_LOCATION permission has been granted by the user
     */

    public static boolean isPermsLocation(Context context) {
        return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * checks if steps permission has been granted
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns true if the  ACTIVITY_RECOGNITION permission has been granted by the user
     */
    //todo api level 29 requirement
    public static boolean isPermsSteps(Context context) {
        return checkSelfPermission(context, ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * schedules the jobs that have not been scheduled
     *
     * @param context Context allows access to application-specific resources and classes
     */
    public static void scheduledUnscheduledJobs(Context context) {

        List<Integer> unscheduledJobs = getUnscheduledJobs(context);

        Data_Type data = getDataType(context);


        if (data != null && isQuestionaireAnswered(context) && getIntelligentAgent(context) != null) {

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
                            Date currentTime = Calendar.getInstance().getTime();
                            if (!isExistingWeatherWeek(context, currentTime)) {
                                scheduleWeather(context);
                            }
                        }

                        break;
                    case QUESTIONNAIRE:
                        scheduleJob(context, QuestionnaireJobService.class, QUESTIONNAIRE, context.getResources().getInteger(R.integer.questionnaire));
                        break;

                    case SCREENTIME:
                        if (data.isScreentime()) {
                            scheduleScreentime(context);
                        }
                        break;
                    case ADVICE:
                        scheduleAdvice(context);
                        break;
                    case ADVICE_FOLLOWED:
                        scheduleAdviceFollowed(context);
                        break;
                }
            }

        } else {
            Log.d(TAG, "scheduledUnscheduledJobs: data == null");
        }


    }


    /**
     * checks the permissions required for the job and if the device has the rehired feature for the job to function before scheduling it
     *
     * @param context        Context allows access to application-specific resources and classes
     * @param jobID          The jobId of the job to be scheduled
     * @param cls            the jobService being scheduled
     * @param minutes        how often the job should be periodically scheduled
     * @param packageManager Class for retrieving various kinds of information related to the application packages that are currently installed on the device.
     * @param permission     permission to be checked before scheduling the job
     * @param feature        the feature to be checked before scheduling the job
     */


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

    /**
     * checks if the device has a feature before scheduling the job
     *
     * @param context        Context allows access to application-specific resources and classes
     * @param jobID          The jobId of the job to be scheduled
     * @param cls            the jobService being scheduled
     * @param minutes        how often the job should be periodically scheduled
     * @param packageManager Class for retrieving various kinds of information related to the application packages that are currently installed on the device.
     * @param feature        the feature to be checked before scheduling the job
     */

    public static void checkPermissionsAndSchedule(Context context, int jobID, Class<?> cls, int minutes, PackageManager packageManager, String feature) {
        if (packageManager.hasSystemFeature(feature)) {
            Log.d(TAG, feature + " feature " + true);


            boolean scheduled = Util.scheduleJob(context, cls, jobID, minutes);
            Log.d(TAG, " scheduled " + scheduled);

        } else {
            Log.d(TAG, feature + " feature " + false);
        }
    }


    /**
     * checks the permissions required for the job before scheduling the job
     *
     * @param context    Context allows access to application-specific resources and classes
     * @param jobID      The jobId of the job to be scheduled
     * @param cls        the jobService being scheduled
     * @param minutes    how often the job should be periodically scheduled
     * @param permission permission to be checked before scheduling the job
     * @return returns if the job has been scheduled
     */

    public static boolean checkPermissionsAndSchedule(Context context, int jobID, Class<?> cls, int minutes, String permission) {

        boolean scheduled = false;
        if (permission == null) {
            scheduled = Util.scheduleJob(context, cls, jobID, minutes);
            Log.d(TAG, "Scheduled " + jobID + " " + scheduled);
            return scheduled;

        } else if (checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission " + permission + true);
            scheduled = Util.scheduleJob(context, cls, jobID, minutes);
            Log.d(TAG, "Scheduled " + jobID + " " + scheduled);
            return scheduled;
        } else {
            Log.d(TAG, "Permission " + permission + " " + false);
            return scheduled;

        }

    }


    /**
     * Returns a list of jobID's for unscheduled jobServices
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns a list of jobID's for unscheduled jobServices
     */

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

    /**
     * Unscheduled the specified jobService
     *
     * @param context Context allows access to application-specific resources and classes
     * @param jobID   The jobId of the job currently scheduled
     * @return returns true if the job has been unscheduled, false if the job has not been unscheduled
     */

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

    /**
     * Checks if a job is currently scheduled
     *
     * @param context Context allows access to application-specific resources and classes
     * @param jobID   The jobId of the job currently scheduled
     * @return returns true if the job has been scheduled, false if the job has not been scheduled
     */
    public static boolean isJobScheduled(Context context, int jobID) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == jobID) {
                Log.d(TAG, "isJobScheduled: " + jobID + " " + true);
                return true;
            }
        }
        Log.d(TAG, "isJobScheduled: " + jobID + " " + false);
        return false;
    }

    /**
     * Converts Epoch time to Date
     *
     * @param time time since midnight 1/1/1970 in milliseconds
     * @return returns a date object of the imputed Epoch time
     */

    public static Date convertEpochToDate(long time) {
        Date date = new Date();
        time = time * 1000;
        date.setTime(time);
        return date;
    }

    /**
     * Stops a background notification from being displayed
     *
     * @param notification_id the ID assigned to the notification being displayed, should be the same as the job it originated from
     */

    public static void stopBackgroundNotification(int notification_id) {
        Log.d(TAG, "stopBackgroundNotification: " + notification_id);
        NotificationManager notificationManager = (NotificationManager) Objects.requireNonNull(getApplicationContext()).getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_id);
    }

    /**
     * checks if the specified service is currently running
     *
     * @param serviceClass the service class being checked
     * @return returns true if the service is currently running, false if the service is currently not running
     */

    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) Objects.requireNonNull(getApplicationContext()).getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the color of the provided toolbar
     *
     * @param intelligent_agent The attributes of the intelligent agent currently on the device
     * @param toolbar           the toolbar whose color is being changed
     * @param resources         used to access the primary color for the relevant gender
     */

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

    /**
     * Sets the theme for the specified activity
     *
     * @param intelligent_agent The attributes of the intelligent agent currently on the device
     * @param activity          the activity whose theme needs to be set
     */

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

    /**
     * Blanket function to correct the viability of any navigation items
     *
     * @param navigationView    the navigationView whose items need to be corrected
     * @param intelligent_agent The attributes of the intelligent agent currently on the device
     * @param dataType          the current dataTypes to be collected by the user
     */
    public static void navigationViewVisibility(NavigationView navigationView, Intelligent_Agent intelligent_agent, Data_Type dataType) {

        navigationViewIntelligentAgent(navigationView, intelligent_agent);
        navigationViewDailyQuestion(navigationView, dataType);
        navigationViewAdviceRanking(navigationView, dataType);

        studyStatus(navigationView, intelligent_agent);

    }

    private static void studyStatus(NavigationView navigationView, Intelligent_Agent intelligent_agent) {

        View header = navigationView.getHeaderView(0);
        TextView studyStatus = (TextView) header.findViewById(R.id.textViewStudyStatus);

//         studyStatus = navigationView.getHeaderView(0).get(R.id.textViewStudyStatus);

        Date currentDate = Calendar.getInstance().getTime();
        String text = "Study Status: Not Started";

        if(intelligent_agent!=null){
            if (currentDate.after(intelligent_agent.getEnd_Date())) {
                text = "Study Status: Finished";
            } else {
                text = "Study Status: Ongoing";

            }
        }



        studyStatus.setText(text);


    }

    /**
     * Sets the nav_justification item to visible if the intelligent agent has the With_Justification attribute,
     * sets the nav_justification item to invisible if the intelligent agent does not possess the with_justification attribute
     *
     * @param navigationView    the navigationView whose items need to be corrected
     * @param intelligent_agent The attributes of the intelligent agent currently on the device
     */
    public static void navigationViewIntelligentAgent(NavigationView navigationView, Intelligent_Agent intelligent_agent) {
        if (intelligent_agent != null) {
            navigationView.getMenu().findItem(R.id.nav_justification).setVisible(withJustification(intelligent_agent));
        }
    }

    /**
     * Sets the nav_daily_question item to visible if either the mood or socialness data type are selected by the user
     * sets the nav_daily_question item to invisible if both the mood and socialness data types are deselected by the user
     *
     * @param navigationView the navigationView whose items need to be corrected
     * @param dataType       the current dataTypes to be collected by the user
     */
    public static void navigationViewDailyQuestion(NavigationView navigationView, Data_Type dataType) {
        if (dataType != null) {

            if (!dataType.isMood() && !dataType.isSocialness()) {
                navigationView.getMenu().findItem(R.id.nav_daily_question).setVisible(false);
            }

        }
    }

    /**
     * Sets the nav_daily_question item to visible if either the mood or socialness data type are selected by the user
     * sets the nav_daily_question item to invisible if both the mood and socialness data types are deselected by the user
     *
     * @param navigationView the navigationView whose items need to be corrected
     * @param visibility     that viability change of the navigation element
     */
    public static void navigationViewDailyQuestion(NavigationView navigationView, boolean visibility) {

        navigationView.getMenu().findItem(R.id.nav_daily_question).setVisible(visibility);

    }

    /**
     * Sets the nav_ranking item to visible if a data type has been selected by the user
     * sets the nav_ranking item to invisible if no data type has been selected by the user
     *
     * @param navigationView the navigationView whose items need to be corrected
     * @param dataType       the current dataTypes to be collected by the user
     */
    public static void navigationViewAdviceRanking(NavigationView navigationView, Data_Type dataType) {
        if (dataType != null) {

            if (!isAllDataType(dataType)) {
                navigationView.getMenu().findItem(R.id.nav_ranking).setVisible(false);
            }

        }
    }

    /**
     * Sets the nav_ranking item to visible if a data type has been selected by the user
     * sets the nav_ranking item to invisible if no data type has been selected by the user
     *
     * @param navigationView the navigationView whose items need to be corrected
     * @param visibility     that viability change of the navigation element
     */
    public static void navigationViewAdviceRanking(NavigationView navigationView, boolean visibility) {

        navigationView.getMenu().findItem(R.id.nav_ranking).setVisible(visibility);


    }


    /**
     * schedules the AmbientTempJobService
     *
     * @param context Context allows access to application-specific resources and classes
     */
    public static void scheduleAmbientTemp(Context context) {

    }

    /**
     * schedules the DailyQuestionJobService
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns if the DailyQuestionJobService has been scheduled
     */
    public static boolean scheduleDailyQuestions(Context context) {
        return scheduleJob(context, DailyQuestionJobService.class, DAILY_QUESTION, context.getResources().getInteger(R.integer.daily_question));
    }

    /**
     * schedules the LocationJobService
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns if the LocationJobService has been scheduled
     */
    public static boolean scheduleLocation(Context context) {
        return checkPermissionsAndSchedule(context, LOCATION,
                LocationJobService.class,
                context.getResources().getInteger(R.integer.location),
                ACCESS_FINE_LOCATION);
    }

    /**
     * schedules the QuestionnaireJobService
     *
     * @param context Context allows access to application-specific resources and classes
     */
    public static void scheduleQuestionnaire(Context context) {

    }

    /**
     * schedules the ScreentimeJobService
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns if the ScreentimeJobService has been scheduled
     */
    public static boolean scheduleScreentime(Context context) {
        return scheduleJob(context, ScreentimeJobService.class, SCREENTIME, context.getResources().getInteger(R.integer.screentime));
    }

    /**
     * schedules the StepsJobService
     *
     * @param context Context allows access to application-specific resources and classes
     */
    public static void scheduleSteps(Context context) {
    }

    /**
     * schedules the WeatherJobService
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns if the WeatherJobService has been scheduled
     */
    public static boolean scheduleWeather(Context context) {
        return scheduleJob(context, WeatherJobService.class, WEATHER, context.getResources().getInteger(R.integer.weather), JobInfo.NETWORK_TYPE_UNMETERED);
    }

    /**
     * schedules the AdviceJobService
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns if the AdviceJobService has been scheduled
     */
    public static boolean scheduleAdvice(Context context) {
        return scheduleJob(context, AdviceJobService.class, ADVICE, context.getResources().getInteger(R.integer.advice));
    }

    /**
     * schedules the AdviceFollowedJobService
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns if the AdviceFollowedJobService has been scheduled
     */
    public static boolean scheduleAdviceFollowed(Context context) {
        return scheduleJob(context, AdviceFollowedJobService.class, ADVICE_FOLLOWED, context.getResources().getInteger(R.integer.advice_followed), true);
    }

    /**
     * schedules the ExportJobService
     *
     * @param context Context allows access to application-specific resources and classes
     * @return returns if the ExportJobService has been scheduled
     */
    public static boolean scheduleExport(Context context) {
        return scheduleJob(context, ExportJobService.class, EXPORT);

    }

    /**
     * Compares two dates
     *
     * @param firstDate  first date to be compared
     * @param secondDate second date to be compared
     * @return true if both dates are the same, false if both dates are not the same
     */
    public static boolean isSameDate(Date firstDate, Date secondDate) {
        return firstDate.getDate() == secondDate.getDate() && firstDate.getMonth() == secondDate.getMonth() &&
                firstDate.getYear() == secondDate.getYear();
    }


    /**
     * Changes the date inputed by x amount of days
     *
     * @param date the date to be changed
     * @param day  number of days the date is to be changed by, minus number inputted will take away days from the date
     * @return the modified date
     */
    public static Date changeDayEndOfDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
        return cal.getTime();


    }


    /**
     * Changes the date inputed by x amount of days
     *
     * @param date the date to be changed
     * @param day  number of days the date is to be changed by, minus number inputted will take away days from the date
     * @return the modified date
     */
    public static Date changeDayStartOfDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        return cal.getTime();


    }


    //truncates the coordinates (lat/lon)
    public static double truncate(double coordinate) {
        DecimalFormat df;
        //TODO potentially change to 3dp
        df = new DecimalFormat("##.####");
//        df = new DecimalFormat("###.######");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(coordinate));
    }


}
