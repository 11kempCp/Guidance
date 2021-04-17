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
import com.example.guidance.activity.QuestionaireActivity;
import com.example.guidance.app.App;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.Util.Util.QUESTIONNAIRE;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.getSizeAllQuestionnaire;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.isQuestionnaire;

/**
 * Created by Conor K on 10/03/2021.
 */
public class QuestionnaireJobService extends JobService {

    private static final String TAG = "QuestionnaireJobService";


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");
        Date currentTime = Calendar.getInstance().getTime();
        //checks if there is a questionnaire present in the Realm already
        if(isQuestionnaire(this)){

            //checks if study period has ended
            if (currentTime.after(getIntelligentAgent(this).getEnd_Date())) {

                //checks to see if the start questionnaire and the end questionnaire have been answered
                if(getSizeAllQuestionnaire(this)<2){

                    //Create an Intent for the activity you want to start
                    Intent resultIntent = new Intent(this, QuestionaireActivity.class);
                    // Create the TaskStackBuilder and add the intent, which inflates the back stack
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntentWithParentStack(resultIntent);
                    // Get the PendingIntent containing the entire back stack
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL_ID);
                    builder.setContentIntent(resultPendingIntent)
                            .setContentTitle(getString(R.string.questionnaire))
                            .setContentText(getString(R.string.notification_questionnaire))
                            .setSmallIcon(R.drawable.ic_questionnaire)
                            .setContentIntent(resultPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_LOW);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    notificationManager.notify(QUESTIONNAIRE, builder.build());
                }

            }
        }





        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        stopForeground(true);

        return false;
    }

}
