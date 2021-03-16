package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.util.Log;

import com.example.guidance.realm.model.AppData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.guidance.Util.Util.isPermsUsageStats;
import static com.example.guidance.realm.DatabaseFunctions.getDataType;
import static com.example.guidance.realm.DatabaseFunctions.screentimeEntry;

/**
 * Created by Conor K on 15/03/2021.
 */
public class ScreentimeJobService extends JobService {

    private static final String TAG = "ScreentimeJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");

        if (getDataType(this).isScreentime() && isPermsUsageStats(this)) {
            Calendar time = Calendar.getInstance();
            //todo decide starttime and endtime
            long f = 1 * 24 * 60 * 60 * 1000;
            time.setTimeInMillis(time.getTimeInMillis() - f);
            long startTime = time.getTimeInMillis();
            long endTime = Calendar.getInstance().getTimeInMillis();
            int interval_type = UsageStatsManager.INTERVAL_WEEKLY;

            UsageStatsManager usageStatsManager = (UsageStatsManager) this.getSystemService(USAGE_STATS_SERVICE);
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(interval_type, startTime, endTime);

            ArrayList<AppData> appDataList = new ArrayList<>();
            for (UsageStats us : queryUsageStats) {
                if (us.getTotalTimeInForeground() != 0) {

                    AppData appData = new AppData();

                    appData.setPackageName(us.getPackageName());
                    appData.setTotalTimeInForeground(us.getTotalTimeInForeground());
                    appData.setTotalTimeVisible(us.getTotalTimeVisible());
                    appData.setTotalTimeForegroundServiceUsed(us.getTotalTimeForegroundServiceUsed());

                    appDataList.add(appData);

                }
            }


            Log.d(TAG, "appDataList: " + appDataList);

            Date currentTime = Calendar.getInstance().getTime();
            screentimeEntry(this, currentTime, interval_type, appDataList);
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
