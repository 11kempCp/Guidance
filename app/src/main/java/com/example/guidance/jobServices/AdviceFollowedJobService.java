package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.example.guidance.realm.model.Advice;
import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Mood;
import com.example.guidance.realm.model.Screentime;
import com.example.guidance.realm.model.Socialness;
import com.example.guidance.realm.model.Step;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmResults;

import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.getUnresolvedAdvice;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.updateAdviceUsageData;
import static com.example.guidance.realm.databasefunctions.LocationDatabaseFunctions.getLocationEntryDate;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.getMoodEntryDate;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.location;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.mood;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.screentime;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.socialness;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.step;
import static com.example.guidance.realm.databasefunctions.ScreentimeDatabaseFunctions.getScreentimeDate;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.getSocialnessEntryDate;
import static com.example.guidance.realm.databasefunctions.StepsDatabaseFunctions.getStepEntryDate;

/**
 * Created by Conor K on 31/03/2021.
 */
public class AdviceFollowedJobService extends JobService {

    private static final String TAG = "AdviceFollowedJobService";


    private static final int idealThresholdDistance = 50; //meters


    @Override
    public boolean onStartJob(JobParameters params) {

        Log.d(TAG, "onStartJob: ");

        //gets a list of all Advice whose AdviceFollowed variable is equal to null
        ArrayList<Advice> unresolvedAdvice = getUnresolvedAdvice(this);
        Date currentTime = Calendar.getInstance().getTime();

        //if unresolvedAdvice is empty then stop the AdviceFollowedJobService
        if (unresolvedAdvice.isEmpty()) {
            Log.d(TAG, "unresolvedAdvice.isEmpty(): true");
            stopSelf();
        }

        //for each Advice in the unresolvedAdvice, a check is done to ensure that the advice taken is
        //equal to null, it then checks if the dateTime the advice is for has past. If so then checks to see
        //if the advice has been followed, by checking the data for the date the advice was for.
        //If there is insufficient data for the date then it is assumed the advice was not followed
        for (Advice advice : unresolvedAdvice) {
            if (advice.getAdviceUsageData().getAdviceTaken() == null) {
                if (currentTime.after(advice.getDateTimeAdviceFor())) {
                    Log.d(TAG, "currentTime is after advice.getDateTimeAdviceFor()");

//                    updateAdviceUsageData(this, wasAdviceFollowed(advice, advice.getDateTimeAdviceFor()), advice.getAdviceUsageData().get_id());

                    //change to program after study. Advice was impossible to update due to the wrong ID being referenced
                    updateAdviceUsageData(this, wasAdviceFollowed(advice, advice.getDateTimeAdviceFor()), advice.getAdviceUsageData().get_id());
                } else {
                    Log.d(TAG, "currentTime is not after advice.getDateTimeAdviceFor()");

                }
            }
        }

        return false;
    }

    private Boolean wasAdviceFollowed(Advice advice, Date dateTimeAdviceFor) {
        Log.d(TAG, "wasAdviceFollowed: adviceType " + advice.getAdviceType());

        //Change to code after the study has ended
        if (dateTimeAdviceFor == null) {
            Log.d(TAG, "wasAdviceFollowed: dateTimeAdviceForNull");
            return null;
        }

        if (advice == null) {
            Log.d(TAG, "wasAdviceFollowed: advice");
            return null;
        }

        switch (advice.getAdviceType()) {
            case step:
                Float steps = advice.getSteps();
                return adviceFollowedSteps(dateTimeAdviceFor, steps);
            case screentime:
                if (advice.getScreentime() == null) {
                    Log.d(TAG, "wasAdviceFollowed: adviceScreenTime is: " + advice.getScreentime());

                } else {
                    Log.d(TAG, "wasAdviceFollowed: adviceScreenTime is: " + advice.getScreentime());

                    String packageName = advice.getScreentime().getPackageName();
                    Long totalTimeInForeground = advice.getScreentime().getTotalTimeInForeground();
                    return adviceFollowedScreentime(dateTimeAdviceFor, packageName, totalTimeInForeground);
                }

            case location:

                return adviceFollowedLocation(dateTimeAdviceFor);
            case socialness:
                Float getSocialness = advice.getSocialness();
                return adviceFollowedSocialness(dateTimeAdviceFor, getSocialness);
            case mood:
                Float getMood = advice.getMood();
                return adviceFollowedMood(dateTimeAdviceFor, getMood);
        }


        //if the advice type is none of the above then it is incorrect
        return null;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    public boolean adviceFollowedSteps(Date dateTimeAdviceFor, Float steps) {

        float stepCountPreviousDay;

        Step stepEntryDate = getStepEntryDate(this, dateTimeAdviceFor);

        //Change to code after the study has ended
//        if (stepEntryDate == null || steps == null) {

        if (stepEntryDate == null || steps == null) {
            //insufficient data
            Log.d(TAG, "adviceFollowedSteps: insufficient data ");

            return false;
        }

        stepCountPreviousDay = stepEntryDate.getStepCount();

        return stepCountPreviousDay <= steps;
    }

    public boolean adviceFollowedScreentime(Date dateTimeAdviceFor, String packageName, Long totalTimeInForeground) {

        Screentime screentimeDate = getScreentimeDate(this, dateTimeAdviceFor);

//        if (screentimeDate == null ) {
        if (screentimeDate == null || totalTimeInForeground == null) {
            //insufficient data
            Log.d(TAG, "adviceFollowedScreentime: insufficient data ");
            return false;
        }

        AppData[] appDataList = screentimeDate.getAppData().toArray(new AppData[0]);

        for (AppData appData : appDataList) {

            if (appData.getPackageName().equals(packageName)) {

                return appData.getTotalTimeInForeground() >= totalTimeInForeground;
            }
        }


        return false;
    }

    private boolean adviceFollowedLocation(Date dateTimeAdviceFor) {

        RealmResults<Location> locationOnDateTimeAdviceFor = getLocationEntryDate(this, dateTimeAdviceFor);

        if (locationOnDateTimeAdviceFor != null) {


            android.location.Location previous_location = new android.location.Location("");
            android.location.Location new_location = new android.location.Location("");

            boolean first = true;

            for (Location loc : locationOnDateTimeAdviceFor) {


                if (first) {
                    first = false;
                } else {

                    new_location.setLatitude(loc.getLatitude());
                    new_location.setLongitude(loc.getLongitude());
                    float distanceInMeters = previous_location.distanceTo(new_location);

                    if (!(distanceInMeters >= idealThresholdDistance)) {
                        return true;
                    }
                }

                previous_location.setLatitude(loc.getLatitude());
                previous_location.setLongitude(loc.getLongitude());
            }
        } else {
            Log.d(TAG, "adviceFollowedLocation: insufficient data ");
            return false;
        }

        return false;
    }

    private boolean adviceFollowedSocialness(Date dateTimeAdviceFor, Float socialnessFromJustificationForAdvice) {
        Socialness socialnessOnDateTimeAdviceFor = getSocialnessEntryDate(this, dateTimeAdviceFor);
//Change to code after the study has ended
//        if (socialnessOnDateTimeAdviceFor == null) {
        if (socialnessOnDateTimeAdviceFor == null || socialnessFromJustificationForAdvice == null) {
            Log.d(TAG, "adviceFollowedSocialness: insufficient data ");
            return false;
        }

        return socialnessOnDateTimeAdviceFor.getRating() >= socialnessFromJustificationForAdvice;

    }

    private boolean adviceFollowedMood(Date dateTimeAdviceFor, Float moodFromJustificationForAdvice) {
        Mood moodOnDateTimeAdviceFor = getMoodEntryDate(this, dateTimeAdviceFor);
//Change to code after the study has ended
//        if (moodOnDateTimeAdviceFor == null) {
        if (moodOnDateTimeAdviceFor == null || moodFromJustificationForAdvice == null) {
            Log.d(TAG, "adviceFollowedMood: insufficient data ");
            return false;
        }

        return moodOnDateTimeAdviceFor.getRating() >= moodFromJustificationForAdvice;

    }

}
