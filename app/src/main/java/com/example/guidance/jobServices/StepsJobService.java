package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.services.StepsService;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.StepsDatabaseFunctions.insertStepsCounter;
import static com.example.guidance.realm.databasefunctions.StepsDatabaseFunctions.isStepEntryDate;
import static com.example.guidance.realm.databasefunctions.StepsDatabaseFunctions.updateStepToday;

/**
 * Created by Conor K on 14/02/2021.
 */

public class StepsJobService extends JobService {
    private static final String TAG = "StepsJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent serviceIntent = new Intent(this, StepsService.class);

        Data_Type data = getDataType(this);

        if(data.isSteps()){
            Date currentTime = Calendar.getInstance().getTime();
            if (isStepEntryDate(this, currentTime)) {
                Log.d(TAG, "Updated Existing Entry");
//            Toast.makeText(this, "Updated Existing Entry", Toast.LENGTH_SHORT).show();
                updateStepToday(this, StepsService.getSteps(), currentTime);
            } else {
                StepsService.resetSensor();
                Log.d(TAG, "New Entry Saved into Database");
//            Toast.makeText(this, "New Entry Saved into Database", Toast.LENGTH_SHORT).show();
                insertStepsCounter(this, StepsService.getSteps(), currentTime);
            }

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
        Intent serviceIntent = new Intent(this, StepsService.class);
        stopService(serviceIntent);
        return false;
    }


}
