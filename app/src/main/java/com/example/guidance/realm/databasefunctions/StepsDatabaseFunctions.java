package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.Step;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by Conor K on 20/03/2021.
 */
public class StepsDatabaseFunctions {

    private static final String TAG = "StepsDatabaseFunctions";

    public static void updateStepToday(Context context, float currentSensorValue, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            // Sort chronologically? because realm is lazily searched there is no
            // guarantee that it will return the last entry inputted
            // TODO Check that this returns the correct result
            RealmQuery<Step> query = r.where(Step.class).lessThan("dateTime", currentTime);
            Step result = query.sort("dateTime", Sort.DESCENDING).findFirst();

            if (result == null) {
                Log.d(TAG, "isThereAnEntryToday: ERROR");
            } else {
                Log.d(TAG, "settingStepCount");
                result.setDateTime(currentTime);
                result.setStepCount(currentSensorValue);
                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "updateSteps onSuccess:");
                Log.d(TAG, "executed transaction : updateStepToday" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateStepToday transaction failed: ", error);

            }
        });


        realm.close();

    }

    public static boolean isStepEntryDate(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

//        Step task = realm.where(Step.class).lessThan("dateTime", currentTime).findFirst();

        RealmQuery<Step> query = realm.where(Step.class).lessThan("dateTime", currentTime);
        Step task = query.sort("dateTime", Sort.DESCENDING).findFirst();


        if (task == null) {
            Log.d(TAG, "isThereAnEntryToday: false");
            return false;
        } else
            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
                    task.getDateTime().getYear() == currentTime.getYear();
    }

    public static void insertStepsCounter(Context context, float currentSensorValue, Date currentTime) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            Step init = r.createObject(Step.class, new ObjectId());
            init.setStepCount(currentSensorValue);
            init.setDateTime(currentTime);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "executed transaction : saveStepsCounterToDatabase" + currentTime);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "saveStepsCounterToDatabase transaction failed: ", error);

            }
        });
        realm.close();

    }

}
