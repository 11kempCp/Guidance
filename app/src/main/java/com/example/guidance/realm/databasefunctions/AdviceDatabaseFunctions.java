package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.Util.AdviceJustification;
import com.example.guidance.realm.model.Advice;
import com.example.guidance.realm.model.AdviceUsageData;
import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.Justification;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Mood;
import com.example.guidance.realm.model.Screentime;
import com.example.guidance.realm.model.Socialness;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Conor K on 30/03/2021.
 */
public class AdviceDatabaseFunctions {

    private static final String TAG = "AdviceDatabaseFunctions";


    public static void insertAdvice(Context context, Date dateTimeAdviceGiven, String adviceType
            , Date dateTimeAdviceFor, Float steps, AppData screentime,
                                    Float socialness, Float mood, AdviceJustification justification) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            Advice init = r.createObject(Advice.class, new ObjectId());
            init.setDateTimeAdviceGiven(dateTimeAdviceGiven);
            init.setAdviceType(adviceType);
            init.setDateTimeAdviceFor(dateTimeAdviceFor);
            init.setSteps(steps);
            init.setScreentime(screentime);
            init.setSocialness(socialness);
            init.setMood(mood);

            AdviceUsageData adviceUsageData = r.createObject(AdviceUsageData.class, new ObjectId());
            adviceUsageData.setDateTimeAdviceFor(dateTimeAdviceGiven);
            adviceUsageData.setAdviceType(adviceType);
            adviceUsageData.setDateTimeAdviceGiven(dateTimeAdviceFor);
            adviceUsageData.setAdviceTaken(null);
            init.setAdviceUsageData(adviceUsageData);

            Justification just = r.createObject(Justification.class, new ObjectId());
            just.setJustificationStep(justification.getJustificationStep());
            RealmList <AppData> appDataRealmList = new RealmList<>(justification.getJustificationScreentime());

            just.setJustificationScreentime(appDataRealmList);

            RealmList <Location> locationRealmList = new RealmList<>();
            locationRealmList.addAll(justification.getJustificationLocation().subList(0, justification.getJustificationLocation().size()));
            just.setJustificationLocation(locationRealmList);

            RealmList <Socialness> socialnessRealmList = new RealmList<>();
            socialnessRealmList.addAll(justification.getJustificationSocialness().subList(0, justification.getJustificationSocialness().size()));
            just.setJustificationSocialness(socialnessRealmList);

            RealmList <Mood> moodRealmList = new RealmList<>();
            moodRealmList.addAll(justification.getJustificationMood().subList(0, justification.getJustificationMood().size()));
            just.setJustificationMood(moodRealmList);


            init.setJustification(just);

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : insertAdvice" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertAdvice transaction failed: ", error);

            }
        });
        realm.close();

    }

    public static void insertAdviceUsageData(Context context, Date dateTimeAdviceGiven, String adviceType
            , Date dateTimeAdviceFor, boolean adviceTaken) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            AdviceUsageData init = r.createObject(AdviceUsageData.class, new ObjectId());
            init.setDateTimeAdviceGiven(dateTimeAdviceGiven);
            init.setAdviceType(adviceType);
            init.setDateTimeAdviceFor(dateTimeAdviceFor);
            init.setAdviceTaken(adviceTaken);



        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : insertAdvice" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertAdvice transaction failed: ", error);

            }
        });
        realm.close();

    }

    public static void updateAdviceUsageData(Context context, boolean adviceTaken, ObjectId objectId) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            RealmQuery<AdviceUsageData> query = r.where(AdviceUsageData.class).equalTo("_id", objectId);
            AdviceUsageData result = query.sort("dateTime", Sort.DESCENDING).findFirst();

            if (result == null) {
                Log.d(TAG, "updateAdviceUsageData: ERROR");
            } else {
                result.setAdviceTaken(adviceTaken);
                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "updateSteps onSuccess:");
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : updateAdviceUsageData" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateAdviceUsageData transaction failed: ", error);

            }
        });
        realm.close();

    }

    public static ArrayList<Advice> getUnresolvedAdvice(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Advice> tasksQuery = realm.where(Advice.class).findAll();
//        realm.close();
        ArrayList<Advice> adviceArrayList = new ArrayList<>();

        tasksQuery.sort("dateTimeAdviceGiven", Sort.ASCENDING);
        for(Advice advice: tasksQuery){

            if(advice.getAdviceUsageData().getAdviceTaken()==null){
                adviceArrayList.add(advice);
            }

        }

        return adviceArrayList;
    }

    public static RealmResults<AdviceUsageData> getAdviceUsageData(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        return realm.where(AdviceUsageData.class).findAll();
    }



}
