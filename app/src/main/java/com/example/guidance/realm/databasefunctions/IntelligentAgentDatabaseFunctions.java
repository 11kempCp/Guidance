package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.guidance.R;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.example.guidance.realm.model.User_Information;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

import static com.example.guidance.Util.IA.FEMALE;
import static com.example.guidance.Util.IA.HIGH;
import static com.example.guidance.Util.IA.LOW;
import static com.example.guidance.Util.IA.MACHINE_LEARNING;
import static com.example.guidance.Util.IA.MALE;
import static com.example.guidance.Util.IA.NO_JUSTIFICATION;
import static com.example.guidance.Util.IA.SPEECH;
import static com.example.guidance.Util.IA.TEXT;
import static com.example.guidance.Util.IA.TRADITIONAL_PROGRAMMING;
import static com.example.guidance.Util.IA.WITH_JUSTIFICATION;

/**
 * Created by Conor K on 20/03/2021.
 */
public class IntelligentAgentDatabaseFunctions {

    private static final String TAG = "IntelligentAgentDatabaseFunctions";


    public static void initialiseIntelligentAgent(Context context, String passcode, int study_duration_days, boolean studyStatus, String accessToken) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            Intelligent_Agent init = r.createObject(Intelligent_Agent.class, new ObjectId());

            String Analysis = "";
            String Advice = "";
            String Gender = "";
            String Interaction = "";
            String Output = "";

            if (passcode.contains(MACHINE_LEARNING)) {
                Analysis = MACHINE_LEARNING;
            } else if (passcode.contains(TRADITIONAL_PROGRAMMING)) {
                Analysis = TRADITIONAL_PROGRAMMING;
            }

            if (passcode.contains(NO_JUSTIFICATION)) {
                Advice = NO_JUSTIFICATION;
            } else if (passcode.contains(WITH_JUSTIFICATION)) {
                Advice = WITH_JUSTIFICATION;
            }

            if (passcode.contains(FEMALE)) {
                Gender = FEMALE;
            } else if (passcode.contains(MALE)) {
                Gender = MALE;
            }


            if (passcode.contains(HIGH)) {
                Interaction = HIGH;
            } else if (passcode.contains(LOW)) {
                Interaction = LOW;
            }

            if (passcode.contains(SPEECH)) {
                Output = SPEECH;
            } else if (passcode.contains(TEXT)) {
                Output = TEXT;
            }

            Date currentTime = Calendar.getInstance().getTime();


            Calendar cal = Calendar.getInstance();
            cal.setTime(currentTime);
            cal.add(Calendar.DATE, study_duration_days);

            Date end_date = cal.getTime();

            init.setDate_Initialised(currentTime);
            init.setEnd_Date(end_date);
            init.setAnalysis(Analysis);
            init.setAdvice(Advice);
            init.setGender(Gender);
            init.setInteraction(Interaction);
            init.setOutput(Output);
            init.setStudyStatus(studyStatus);
            init.setAccessToken(accessToken);


        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : initialiseIntelligentAgent" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "initialiseIntelligentAgent transaction failed: ", error);
            }
        });
        realm.close();
    }

    public static void intelligentAgentEntry(Context context, String passcode) {


        if (!isIntelligentAgentInitialised(context)) {
            initialiseIntelligentAgent(context, passcode, R.integer.study_period_length_days, false, null);
        } else {
            Log.d(TAG, "intelligentAgentEntry: IA already initialised");
        }
    }

    public static boolean isIntelligentAgentInitialised(Context context) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        Intelligent_Agent query = realm.where(Intelligent_Agent.class).findFirst();
//        Log.d(TAG, "isIntelligentAgentInitialised: query " + query);
        return query != null;

    }

    public static void intelligentAgentSetGender(Context context, String gender) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {

            Intelligent_Agent result = r.where(Intelligent_Agent.class).findFirst();

            if (result == null) {
                Log.d(TAG, "intelligentAgentSetGender: ERROR");
            } else {

                result.setGender(gender);
                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "updateSteps onSuccess:");
                Date ct = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : intelligentAgentSetGender " + gender + " " + ct);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "intelligentAgentSetGender transaction failed: ", error);

            }
        });


        realm.close();

    }

    public static Intelligent_Agent getIntelligentAgent(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Intelligent_Agent> tasksQuery = realm.where(Intelligent_Agent.class);
//        realm.close();

        return tasksQuery.findFirst();
    }


    public static void updateIntelligentAgent(Context context, boolean status){
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            RealmQuery<Intelligent_Agent> query = r.where(Intelligent_Agent.class);
            Intelligent_Agent result = query.findFirst();

            if (result == null) {
                Log.d(TAG, "isThereAnEntryToday: ERROR");
            } else {
                Log.d(TAG, "settingStepCount");
                result.setStudyStatus(status);
                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "executed transaction : updateUserInformation" );

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateUserInformation transaction failed: ", error);

            }
        });


    }

    public static void updateAPIKey(Context context, String key){
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            RealmQuery<Intelligent_Agent> query = r.where(Intelligent_Agent.class);
            Intelligent_Agent result = query.findFirst();

            if (result == null) {
                Log.d(TAG, "isThereAnEntryToday: ERROR");
            } else {
                Log.d(TAG, "settingStepCount");
                result.setAccessToken(key);
                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "executed transaction : updateAPIKey" );
                Toast.makeText(context, "Updated API Key", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateAPIKey transaction failed: ", error);

            }
        });


    }

    public static void updateCount(Context context, int count){
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            RealmQuery<Intelligent_Agent> query = r.where(Intelligent_Agent.class);
            Intelligent_Agent result = query.findFirst();

            if (result == null) {
                Log.d(TAG, "isThereAnEntryToday: ERROR");
            } else {
                Log.d(TAG, "settingStepCount");
                result.setCount(count);
                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "executed transaction : updateAPIKey" );
                Toast.makeText(context, "Updated API Key", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateAPIKey transaction failed: ", error);

            }
        });


    }

}
