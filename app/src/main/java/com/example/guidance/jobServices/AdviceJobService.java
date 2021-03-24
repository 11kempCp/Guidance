package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Mood;
import com.example.guidance.realm.model.Screentime;
import com.example.guidance.realm.model.Socialness;
import com.example.guidance.realm.model.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.RealmResults;

import static com.example.guidance.Util.Util.isSameDate;
import static com.example.guidance.realm.databasefunctions.LocationDatabaseFunctions.getLocationOverPreviousDays;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.getMoodOverPreviousDays;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.*;
import static com.example.guidance.realm.databasefunctions.ScreentimeDatabaseFunctions.getScreentimePreviousDay;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.getSocialnessOverPreviousDays;
import static com.example.guidance.realm.databasefunctions.StepsDatabaseFunctions.getStepPreviousDay;

/**
 * Created by Conor K on 20/03/2021.
 */
public class AdviceJobService extends JobService {

    private static final String TAG = "AdviceJobService";

    private static boolean adviceLocation = false;
    private static boolean adviceSteps = false;
    private static boolean adviceScreentime = false;


    private static final int idealStepcount = 3000; //steps
    private static final int idealScreentimeUsage = 60; //minutes
    private static final int idealThresholdDistance = 50; //meters
    private static final double idealMood = 2.5; //average of more than this
    private static final double idealSocialness = 2.5; //average of more than this


//    private static final String step = "Step";
//    private static final String screentime = "Screentime";
//    private static final String location = "Location";
//    private static final String socialness = "Socialness";
//    private static final String mood = "Mood";

    private static final int days = 3;


    //todo implement ranking as a realm model
    LinkedHashMap<String, Boolean> ranking = new LinkedHashMap<>();


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");


        ranking.put(step, false);
        ranking.put(screentime, false);
        ranking.put(location, false);
        ranking.put(socialness, false);
        ranking.put(mood, false);

        adviceLocation();
        adviceScreentime();
        adviceStep();
        adviceSocialness();
        adviceMood();


        for (String f : ranking.keySet()) {
            if (ranking.get(f)) {
                Log.d(TAG, "ranking " + ranking);

                Log.d(TAG, "Advice " + f + " as " + ranking.get(f));
//                giveAdvice();
                break;
            }
        }


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }

//    public void getSteps() {
//        Toast.makeText(this, "Steps " + StepsService.getmSteps(), Toast.LENGTH_SHORT).show();
//    }


    public void adviceLocation() {
        Log.d(TAG, "adviceLocation: ");
        Date currentTime = Calendar.getInstance().getTime();

        RealmResults<Location> previousDaysLocations = getLocationOverPreviousDays(this, currentTime, days);

        Log.d(TAG, "adviceLocation: " + previousDaysLocations + " " + previousDaysLocations.size());

//        Location previous_location = null;
        android.location.Location previous_location = new android.location.Location("");
        android.location.Location new_location = new android.location.Location("");

        boolean first = true;
        Date skipThisDay = null;


        for (Location loc : previousDaysLocations) {

            if (skipThisDay != null) {
                if (isSameDate(loc.getDateTime(), skipThisDay)) {
                    first = true;
                    continue;
                }
            }

            if (first) {
                first = false;
            } else {


                new_location.setLatitude(loc.getLatitude());
                new_location.setLongitude(loc.getLongitude());
                float distanceInMeters = previous_location.distanceTo(new_location);
//                Log.d(TAG, "adviceLocation: distanceInMeters " + distanceInMeters);


                if (!(distanceInMeters <= idealThresholdDistance)) {
                    ranking.put(location, true);
                    skipThisDay = loc.getDateTime();
                }


            }

            previous_location.setLatitude(loc.getLatitude());
            previous_location.setLongitude(loc.getLongitude());
        }
    }

    public void adviceScreentime() {

        Date currentTime = Calendar.getInstance().getTime();
        Screentime screentimePreviousDay = getScreentimePreviousDay(this, currentTime, 1);


        AppData[] appDataList;
        Screentime most_visible_screentime;
        boolean sort = false;

        Log.d(TAG, "adviceScreentime: " + screentimePreviousDay)
        ;
        Log.d(TAG, "adviceScreentime date: " + screentimePreviousDay.getDateTime());
        most_visible_screentime = screentimePreviousDay;
        if (screentimePreviousDay.getAppData().size() > 6) {
            appDataList = new AppData[5];
        } else {
            appDataList = new AppData[screentimePreviousDay.getAppData().size()];
        }

        for (AppData appData : screentimePreviousDay.getAppData()) {

            if (!sort) {
                for (int i = 0; i <= appDataList.length; i++) {
                    if (appDataList[i] == null) {
                        appDataList[i] = appData;
                        Log.d(TAG, "adviceScreentime: " + Arrays.toString(appDataList));
                        if (i == appDataList.length - 1) {
                            sort = true;
                            break;
                        }
                        break;
                    }
                }
            } else {
                appDataList = sortList(appDataList);

                if (appDataList[appDataList.length - 1].getTotalTimeInForeground() < appData.getTotalTimeInForeground()) {
                    appDataList[appDataList.length - 1] = appData;
                }
            }
        }

        int i = 0;
        for (AppData appData : appDataList) {
            i++;
            long TotalTimeInForeground = TimeUnit.MILLISECONDS.toMinutes(appData.getTotalTimeInForeground());
            long TotalTimeVisible = TimeUnit.MILLISECONDS.toMinutes(appData.getTotalTimeVisible());
            long TotalTimeForegroundServiceUsed = TimeUnit.MILLISECONDS.toMinutes(appData.getTotalTimeForegroundServiceUsed());
            Log.d(TAG, i + " + adviceScreentime: date " + most_visible_screentime.getDateTime() + " " + appData.getPackageName() + " TotalTimeInForeground " + TotalTimeInForeground + " TotalTimeVisible " + TotalTimeVisible + " TotalTimeForegroundServiceUsed " + TotalTimeForegroundServiceUsed);
        }

        if (idealScreentimeUsage >= TimeUnit.MILLISECONDS.toMinutes(appDataList[0].getTotalTimeInForeground())) {
            ranking.put(screentime, true);
        }


    }

    public AppData[] sortList(AppData[] appDataList) {
        List<AppData> dataList = new ArrayList<>(Arrays.asList(appDataList));

        Log.d(TAG, "THIS : " + dataList);

        Collections.sort(dataList, (o1, o2) -> {
                    Log.d(TAG, "compare: " + dataList);
                    return o1.getTotalTimeInForeground().compareTo(o2.getTotalTimeInForeground());
                }
        );

        Log.d(TAG, "sorted : " + dataList);
        return dataList.toArray(new AppData[0]);
    }


    public void adviceStep() {
        Date currentTime = Calendar.getInstance().getTime();
        float stepCountPreviousDay;

        Step stepPreviousDay = getStepPreviousDay(this, currentTime, 1);
        stepCountPreviousDay = stepPreviousDay.getStepCount();

        if (stepCountPreviousDay < idealStepcount) {

            ranking.put(step, true);

        }
    }

    public void adviceSocialness() {
        Date currentTime = Calendar.getInstance().getTime();
        float stepTotalOverPreviousDays = 0;

        RealmResults<Socialness> socialnessOverPreviousDays = getSocialnessOverPreviousDays(this, currentTime, days);

        for (Socialness social : socialnessOverPreviousDays) {
            stepTotalOverPreviousDays += social.getRating();
        }

        float average = (stepTotalOverPreviousDays / socialnessOverPreviousDays.size());
        if (idealSocialness > average) {
            ranking.put(socialness, true);
        }

    }

    public void adviceMood() {
        Date currentTime = Calendar.getInstance().getTime();
        float moodTotalOverPreviousDays = 0;

        RealmResults<Mood> moodOverPreviousDays = getMoodOverPreviousDays(this, currentTime, days);

        for (Mood mood : moodOverPreviousDays) {
            moodTotalOverPreviousDays += mood.getRating();
        }
        float average = (moodTotalOverPreviousDays / moodOverPreviousDays.size());
        if (idealMood > average) {
            ranking.put(mood, true);
        }

    }


}
