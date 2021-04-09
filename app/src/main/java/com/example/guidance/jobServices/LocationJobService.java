package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.services.LocationService;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;

/**
 * Created by Conor K on 25/02/2021.
 */
public class LocationJobService extends JobService {

    private static final String TAG = "LocationJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        Intent serviceIntent = new Intent("CHECK_LOCATION", null, this, LocationService.class);


        Data_Type data = getDataType(this);
        //validation to ensure that the user has allowed the location dataType to be collected and stored
        if (data.isLocation()) {

            //if the API level is O or above then the LocationService needs to be run as an foregroundService
            //otherwise it can be run as a service
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
        //stops the LocationService service from running
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "Job Cancelled Before Completion " + currentTime);
        Intent serviceIntent = new Intent(this, LocationService.class);
        stopService(serviceIntent);
        return false;
    }
}
