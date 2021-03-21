package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Screentime;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.RealmResults;

import static com.example.guidance.Util.Util.isSameDate;
import static com.example.guidance.realm.databasefunctions.LocationDatabaseFunctions.getLocationOverPreviousDays;
import static com.example.guidance.realm.databasefunctions.ScreentimeDatabaseFunctions.getScreentimeOverPreviousDays;

/**
 * Created by Conor K on 20/03/2021.
 */
public class AdviceJobService extends JobService {

    private static final String TAG = "AdviceJobService";
    private static double adviceLocation = 1;
    private static final double temp = 0.5;
    int underThreshold;
    int overThreshold;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");

//        adviceLocation();
        adviceScreentime();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }


    public void adviceLocation() {
        Log.d(TAG, "adviceLocation: ");
        Date currentTime = Calendar.getInstance().getTime();

        RealmResults<Location> previousDaysLocations = getLocationOverPreviousDays(this, currentTime, 3);

        Log.d(TAG, "adviceLocation: " + previousDaysLocations + " " + previousDaysLocations.size());

//        Location previous_location = null;
        android.location.Location previous_location = new android.location.Location("");
        android.location.Location new_location = new android.location.Location("");

        boolean first = true;
        int thresholdDistance = 50;
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
                Log.d(TAG, "adviceLocation: distanceInMeters " + distanceInMeters);




                if (distanceInMeters <= thresholdDistance) {
                    adviceLocation = adviceLocation * temp;
                    underThreshold++;
                } else {
                    overThreshold++;
                    adviceLocation = 1;
                    skipThisDay = loc.getDateTime();
                }


            }

            previous_location.setLatitude(loc.getLatitude());
            previous_location.setLongitude(loc.getLongitude());
        }

        Log.d(TAG, "adviceLocation: adviceLocation " + adviceLocation);
        Log.d(TAG, "adviceLocation: underThreshold " + underThreshold);
        Log.d(TAG, "adviceLocation: overThreshold " + overThreshold);


    }

    public void adviceScreentime(){

        Date currentTime = Calendar.getInstance().getTime();
        RealmResults<Screentime> previousDaysLocations = getScreentimeOverPreviousDays(this, currentTime, 5);


        AppData most_visible_app = null;
        Screentime most_visible_screentime = null;

        for(Screentime screentime: previousDaysLocations){
            for(AppData appData: screentime.getAppData()){

                if(most_visible_app == null){
                    most_visible_app = appData;
                    most_visible_screentime = screentime;
                }else if(most_visible_app.getTotalTimeInForeground() < appData.getTotalTimeInForeground()){
                    most_visible_app = appData;
                    most_visible_screentime = screentime;
                }


            }

        }

        if(most_visible_app.getTotalTimeInForeground()==null){
            Log.d(TAG, "adviceScreentime: PROBLEM");
        }

        long TotalTimeInForeground = TimeUnit.MILLISECONDS.toMinutes(most_visible_app.getTotalTimeInForeground());
        long TotalTimeVisible = TimeUnit.MILLISECONDS.toMinutes(most_visible_app.getTotalTimeVisible());
        long TotalTimeForegroundServiceUsed = TimeUnit.MILLISECONDS.toMinutes(most_visible_app.getTotalTimeForegroundServiceUsed());
        Log.d(TAG, "adviceScreentime: date " + most_visible_screentime.getDateTime()  + " " + most_visible_app.getPackageName() + " TotalTimeInForeground " + TotalTimeInForeground + " TotalTimeVisible " + TotalTimeVisible + " TotalTimeForegroundServiceUsed " + TotalTimeForegroundServiceUsed);

    }



}
