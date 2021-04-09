package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.services.WeatherService;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;

/**
 * Created by Conor K on 04/03/2021.
 */
public class WeatherJobService extends JobService {
    private static final String TAG = "WeatherJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent serviceIntent = new Intent(this, WeatherService.class);

        //gets the data_types that the user has selected to be stored
        Data_Type data = getDataType(this);

        //ensures that one of the data types is selected before launching the weatherService
        if (data.isWeather() || data.isSun() || data.isExternal_temp()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }

        return false;

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "Job Cancelled Before Completion " + currentTime);
        Intent serviceIntent = new Intent(this, WeatherService.class);
        stopService(serviceIntent);

        return false;
    }
}
