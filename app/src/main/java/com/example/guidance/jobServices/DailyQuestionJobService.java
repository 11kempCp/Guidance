package com.example.guidance.jobServices;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.guidance.R;
import com.example.guidance.activity.DailyQuestionActivity;
import com.example.guidance.app.App;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.realm.DatabaseFunctions.isMoodEntryToday;
import static com.example.guidance.realm.DatabaseFunctions.isSocialnessEntryToday;
import static com.example.guidance.scheduler.Util.DAILY_QUESTION;

/**
 * Created by Conor K on 22/02/2021.
 */
public class DailyQuestionJobService extends JobService {

    private static final String TAG = "DailyQuestionJobService";


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: DailyQuestionJobService");
        Date currentTime = Calendar.getInstance().getTime();


        if (currentTime.getHours() <= 14 && currentTime.getHours() <= 22) {
            if (isSocialnessEntryToday(this, currentTime) || isMoodEntryToday(this, currentTime)) {

                //Create an Intent for the activity you want to start
                Intent resultIntent = new Intent(this, DailyQuestionActivity.class);
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(resultIntent);
                // Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                //TODO "DAILY_QUESTION" ?
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL_ID);
                builder.setContentIntent(resultPendingIntent)
                        .setContentTitle(getString(R.string.daily_question))
                        .setContentText(getString(R.string.notification_daily_question))
                        .setSmallIcon(R.drawable.ic_daily_question)
                        .setContentIntent(resultPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_LOW);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(DAILY_QUESTION, builder.build());

            }


        }


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
