package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.Build;
import android.util.Log;

import com.example.guidance.realm.model.AppData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.guidance.Util.Util.isPermsUsageStats;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.ScreentimeDatabaseFunctions.screentimeEntry;

/**
 * Created by Conor K on 15/03/2021.
 */
public class ScreentimeJobService extends JobService {

    private static final String TAG = "ScreentimeJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");

        //ensures that the screentime data type has been set to true by the user
        // as well as ensuring that the user has given the usage stats permission
        if (getDataType(this).isScreentime() && isPermsUsageStats(this)) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            //The start of the day with which to grab usage data
            startTime.set(startTime.get(Calendar.YEAR),startTime.get(Calendar.MONTH),startTime.get(Calendar.DAY_OF_MONTH),0,0);
            // The end of the day with which to grab usage data
            endTime.set(endTime.get(Calendar.YEAR),endTime.get(Calendar.MONTH),endTime.get(Calendar.DAY_OF_MONTH),23,59);


            // converts the start and end date into milliseconds
            long start = startTime.getTimeInMillis();
            long end = endTime.getTimeInMillis();
            int interval_type = UsageStatsManager.INTERVAL_DAILY;

            UsageStatsManager usageStatsManager = (UsageStatsManager) this.getSystemService(USAGE_STATS_SERVICE);
            //queries the usage stats for the device
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(interval_type, start, end);

            //Used to add the AppData's whose TotalTimeInForeground is greater than 0
            ArrayList<AppData> appDataArrayList = new ArrayList<>();

            //loops over all of the apps for the specified day
            for (UsageStats us : queryUsageStats) {

                //filters out applications that were not used
                if (us.getTotalTimeInForeground() != 0) {

                    //Creates a new AppData object
                    AppData appData = new AppData();

                    //sets the package name and TotalTimeInForeground
                    appData.setPackageName(us.getPackageName());
                    appData.setTotalTimeInForeground(us.getTotalTimeInForeground());

                    // TotalTimeVisible and TotalTimeForegroundServiceUsed were implemented in android
                    // Q therefore they need to be set to null for devices running Android P or lower
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        appData.setTotalTimeVisible(us.getTotalTimeVisible());
                        appData.setTotalTimeForegroundServiceUsed(us.getTotalTimeForegroundServiceUsed());
                    }else{
                        appData.setTotalTimeVisible(null);
                        appData.setTotalTimeForegroundServiceUsed(null);
                    }

                    //adds the new AppData object to the appDataArrayList
                    appDataArrayList.add(appData);

                }
            }


//            Log.d(TAG, "appDataList: " + appDataArrayList);


            Date currentTime = Calendar.getInstance().getTime();
            //stores/updates the ScreenTime object for the current day
            screentimeEntry(this, currentTime, interval_type, appDataArrayList);
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
