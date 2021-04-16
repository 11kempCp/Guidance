package com.example.guidance.jobServices;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;
import com.example.guidance.R;
import com.example.guidance.activity.QuestionaireActivity;
import com.example.guidance.app.App;
import com.example.guidance.gsonTemplates.AdviceUsageDataSerializer;
import com.example.guidance.gsonTemplates.DataTypeSerializer;
import com.example.guidance.gsonTemplates.DataTypeUsageDataSerializer;
import com.example.guidance.gsonTemplates.IntelligentAgentSerializer;
import com.example.guidance.gsonTemplates.QuestionnaireSerializer;
import com.example.guidance.gsonTemplates.RankingUsageDataSerializer;
import com.example.guidance.gsonTemplates.UserInformationSerializer;
import com.example.guidance.realm.model.AdviceUsageData;
import com.example.guidance.realm.model.DataTypeUsageData;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.example.guidance.realm.model.Questionnaire;
import com.example.guidance.realm.model.RankingUsageData;
import com.example.guidance.realm.model.User_Information;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static com.example.guidance.Util.Output.create;
import static com.example.guidance.Util.Output.getFile;
import static com.example.guidance.Util.Output.isFilePresent;
import static com.example.guidance.Util.Output.read;
import static com.example.guidance.Util.Util.QUESTIONNAIRE;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.getAllAdviceUsageData;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getAllUsageData;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.updateCount;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.updateIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.getAllQuestionnaire;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.getSizeAllQuestionnaire;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.getRankingUsageData;
import static com.example.guidance.realm.databasefunctions.UserInformationDatabaseFunctions.getUserInformation;

/**
 * Created by Conor K on 10/04/2021.
 */
public class ExportJobService extends JobService {

    private static final String TAG = "ExportJobService";


    @Override
    public boolean onStartJob(JobParameters params) {

        Date currentTime = Calendar.getInstance().getTime();
        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);


        Log.d(TAG, "onStartJob: ");
//        exportStudyData(this);
//        createNotification();

        if(currentTime.after(intelligent_agent.getEnd_Date()) && getSizeAllQuestionnaire(this) == 2 && !intelligent_agent.isStudyStatus() && intelligent_agent.getAccessToken()!=null){

            //todo see if exportStudyData can return true
            //todo display message to user that the study is over
            exportStudyData(this);
            updateIntelligentAgent(this, true);

            createNotification();
        }


        return false;
    }

    private void createNotification() {
        //Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, QuestionaireActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL_ID);


        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(getString(R.string.study_status));
        bigTextStyle.bigText(getString(R.string.notification_study));

        builder.setContentIntent(resultPendingIntent)
                .setContentTitle(getString(R.string.study_status))
                .setStyle(bigTextStyle)
//                .setContentText(text)
                .setSmallIcon(R.drawable.ic_guide)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL_ID);
//        builder.setContentIntent(resultPendingIntent)
//                .setContentTitle(getString(R.string.study_status))
//                .setContentText(getString(R.string.notification_study))
//                .setSmallIcon(R.drawable.ic_guide)
//                .setContentIntent(resultPendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(QUESTIONNAIRE, builder.build());
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public static void exportStudyData(Context context){

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        RealmResults<DataTypeUsageData> dataTypeRealmResults = getAllUsageData(realm);
        User_Information user_information = getUserInformation(context);

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(Data_Type.class, new DataTypeSerializer())
                .registerTypeAdapter(DataTypeUsageData.class, new DataTypeUsageDataSerializer())
                .registerTypeAdapter(User_Information.class, new UserInformationSerializer())
                .registerTypeAdapter(AdviceUsageData.class, new AdviceUsageDataSerializer())
                .registerTypeAdapter(RankingUsageData.class, new RankingUsageDataSerializer())
                .registerTypeAdapter(Questionnaire.class, new QuestionnaireSerializer())
                .registerTypeAdapter(Intelligent_Agent.class, new IntelligentAgentSerializer())
                .create();

//
//
        String json;
        StringBuilder dataTypeUsageData = new StringBuilder();

        String data_type = gson.toJson(realm.copyFromRealm(getDataType(context)));

        for (DataTypeUsageData d : dataTypeRealmResults) {
            json = gson.toJson(realm.copyFromRealm(d));
            dataTypeUsageData.append(json);
        }

        String userInfo = gson.toJson(realm.copyFromRealm(user_information));
        String adviceUsageData = gson.toJson(realm.copyFromRealm(getAllAdviceUsageData(realm)));
        String rankingUsageData = gson.toJson(realm.copyFromRealm(getRankingUsageData(realm)));
        String questionnaire = gson.toJson(realm.copyFromRealm(getAllQuestionnaire(realm)));
        String intelligent_agent = gson.toJson(realm.copyFromRealm(getIntelligentAgent(context)));


//        System.out.println("data_type " + data_type);
//        System.out.println("dataTypeUsageData " + dataTypeUsageData);
//        System.out.println("userInfo " + userInfo);
//        System.out.println("adviceUsageData " + adviceUsageData);
//        System.out.println("rankingUsageData " + rankingUsageData);
//        System.out.println("questionnaire " + questionnaire);
//        System.out.println("intelligent_agent " + intelligent_agent);


//        String name = "upload_";
        Intelligent_Agent IA = getIntelligentAgent(context);

        String count = "C_" + IA.getCount();
        String name = IA.getAnalysis() + IA.getAdvice()+IA.getGender()+IA.getInteraction()+IA.getOutput();
        String date = Calendar.getInstance().getTime().toString();
        String endJson = ".json";
        String filename = count + name + date + endJson;

        String jsonString = gson.toJson(realm.copyFromRealm(user_information))
                + gson.toJson(realm.copyFromRealm(getAllAdviceUsageData(realm)))
                + gson.toJson(realm.copyFromRealm(getRankingUsageData(realm)))
                + gson.toJson(realm.copyFromRealm(getAllQuestionnaire(realm)))
                + gson.toJson(realm.copyFromRealm(getIntelligentAgent(context)));

        Log.d(TAG, "exportStudyData: " + jsonString);


        boolean isFP = isFilePresent(context, filename);
        if (isFP) {
            String pppp = read(context, filename);

            Log.d(TAG, "exportStudyData: isFP " + true + "data is " + pppp);

            //do the json parsing here and do the rest of functionality of app
        } else {
            boolean isFileCreated = create(context, filename, jsonString);
            if (isFileCreated) {
                //proceed with storing the first todo  or show ui

                Log.d(TAG, "exportStudyData: isFileCreated" + true);
            } else {
                //show error or try again.

                Log.d(TAG, "exportStudyData: isFileCreated" + false);


            }
        }


        if (isFilePresent(context, filename)) {


            Log.d(TAG, "exportStudyData: " + filename);





//            String mPath = getFile(context, filename).getPath();

            File file = getFile(context, filename);

//            Log.d(TAG, "uploadFileAA: " + mPath);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            try {

                // Create Dropbox client
                DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");

                System.out.println(IA.getAccessToken());
                DbxClientV2 client = new DbxClientV2(config, IA.getAccessToken());

                // Get current account info
                FullAccount account = client.users().getCurrentAccount();
                System.out.println(account.getName().getDisplayName());


                if(isFilePresent(context, filename)){

                    try (InputStream in = new FileInputStream(file)) {
                        FileMetadata metadata = client.files().uploadBuilder(file.getPath())
                                .uploadAndFinish(in);



                        Log.d(TAG, "exportStudyData: " + metadata.getExportInfo());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }



            } catch (DbxException e) {
                e.printStackTrace();
            }

        }

        updateCount(context, IA.getCount()+1);
    }






}
