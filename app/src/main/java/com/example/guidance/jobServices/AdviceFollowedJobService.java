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

        ArrayList<Advice> unresolvedAdvice = getUnresolvedAdvice(this);
        Date currentTime = Calendar.getInstance().getTime();
        if(unresolvedAdvice.isEmpty()){

            Log.d(TAG, "unresolvedAdvice.isEmpty(): true");
            stopSelf();
        }

        for (Advice advice : unresolvedAdvice) {
            if (advice.getAdviceUsageData().getAdviceTaken() == null) {
                if (currentTime.after(advice.getDateTimeAdviceFor())) {
                    Log.d(TAG, "currentTime is after advice.getDateTimeAdviceFor()");
                    updateAdviceUsageData(this, wasAdviceFollowed(advice, advice.getDateTimeAdviceFor()), advice.get_id());
                }else{
                    Log.d(TAG, "currentTime is not after advice.getDateTimeAdviceFor()");

                }
            }
        }

        return false;
    }

    private Boolean wasAdviceFollowed(Advice advice, Date dateTimeAdviceFor) {
        Log.d(TAG, "wasAdviceFollowed: adviceType " + advice.getAdviceType());

        switch (advice.getAdviceType()) {
            case step:
                return adviceFollowedSteps(dateTimeAdviceFor, advice.getSteps());
            case screentime:

                return adviceFollowedScreentime(dateTimeAdviceFor, advice.getScreentime().getPackageName(), advice.getScreentime().getTotalTimeInForeground());

            case location:

                return adviceFollowedLocation(dateTimeAdviceFor);
            case socialness:

                return adviceFollowedSocialness(dateTimeAdviceFor, advice.getSocialness());
            case mood:

                return adviceFollowedMood(dateTimeAdviceFor, advice.getMood());
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

        if (stepEntryDate == null) {
            //insufficient data
            Log.d(TAG, "adviceFollowedSteps: insufficient data ");

            return false;
        }

        stepCountPreviousDay = stepEntryDate.getStepCount();

        return stepCountPreviousDay <= steps;
    }

    public boolean adviceFollowedScreentime(Date dateTimeAdviceFor, String packageName, Long totalTimeInForeground) {

        Screentime screentimeDate = getScreentimeDate(this, dateTimeAdviceFor);

        if (screentimeDate == null) {
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

        if (socialnessOnDateTimeAdviceFor == null) {
            Log.d(TAG, "adviceFollowedSocialness: insufficient data ");
            return false;
        }

        return socialnessOnDateTimeAdviceFor.getRating() >= socialnessFromJustificationForAdvice;

    }

    private boolean adviceFollowedMood(Date dateTimeAdviceFor, Float moodFromJustificationForAdvice) {
        Mood moodOnDateTimeAdviceFor = getMoodEntryDate(this, dateTimeAdviceFor);

        if (moodOnDateTimeAdviceFor == null) {
            Log.d(TAG, "adviceFollowedMood: insufficient data ");
            return false;
        }

        return moodOnDateTimeAdviceFor.getRating() >= moodFromJustificationForAdvice;

    }

}
