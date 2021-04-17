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
import com.example.guidance.realm.model.Data_Type;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.Util.Util.DAILY_QUESTION;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.isMoodEntryToday;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.isSocialnessEntryDate;

/**
 * Created by Conor K on 22/02/2021.
 */
public class DailyQuestionJobService extends JobService {

    private static final String TAG = "DailyQuestionJobService";


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: DailyQuestionJobService");
        Date currentTime = Calendar.getInstance().getTime();

        Data_Type data = getDataType(this);
        //validation to ensure that the user wants to store their socialness or mood data
        if ((data.isSocialness() || data.isMood())) {

            //timeframe in which the notification will be displayed
            if (currentTime.getHours() >= 14 && currentTime.getHours() <= 22) {

                //TODO if user has selected one but not the other
                if(!isSocialnessEntryDate(this, currentTime) || !isMoodEntryToday(this,currentTime)){
                    //Create an Intent for the activity you want to start
                    Intent resultIntent = new Intent(this, DailyQuestionActivity.class);
                    // Create the TaskStackBuilder and add the intent, which inflates the back stack
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntentWithParentStack(resultIntent);
                    // Get the PendingIntent containing the entire back stack
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

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
        }




        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        //removes the notification if the job is cancelled
        stopForeground(true);
        return false;
    }
}
