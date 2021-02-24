package com.example.guidance.jobServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import com.example.guidance.services.DailyQuestionService;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.realm.DatabaseFunctions.isSocialnessEntryToday;

/**
 * Created by Conor K on 22/02/2021.
 */
public class DailyQuestionJobService extends JobService {

    private static final String TAG = "SocialnessJobService";


    @Override
    public boolean onStartJob(JobParameters params) {

        Date currentTime = Calendar.getInstance().getTime();

        if (isSocialnessEntryToday(this, currentTime)) {

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent serviceIntent = new Intent(this, DailyQuestionService.class);
                startForegroundService(serviceIntent);
            } else {

            }

        }




        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
