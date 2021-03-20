package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.Mood;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by Conor K on 20/03/2021.
 */
public class MoodDatabaseFunctions {

    private static final String TAG = "MoodDatabaseFunctions";


    public static void moodEntry(Context context, Date currentTime, int value) {
        if (isMoodEntryToday(context, currentTime)) {
            updateMoodToday(context, value, currentTime);
        } else {
            insertMood(context, value, currentTime);
        }
    }

    public static int getTodaysMoodEntry(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        RealmQuery<Mood> query = realm.where(Mood.class).lessThan("dateTime", currentTime);
        Mood task = query.sort("dateTime", Sort.DESCENDING).findFirst();

        realm.close();
        return task.getRating();
    }

    public static boolean isMoodEntryToday(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        RealmQuery<Mood> query = realm.where(Mood.class).lessThan("dateTime", currentTime);
        Mood task = query.sort("dateTime", Sort.DESCENDING).findFirst();


        if (task == null) {
            Log.d(TAG, "isThereAnEntryToday: false");
            return false;
        } else
            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
                    task.getDateTime().getYear() == currentTime.getYear();
    }

    private static void insertMood(Context context, int value, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            Mood init = r.createObject(Mood.class, new ObjectId());
            init.setRating(value);
            init.setDateTime(currentTime);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "executed transaction : insertMood" + currentTime);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertMood transaction failed: ", error);

            }
        });
        realm.close();

    }

    private static void updateMoodToday(Context context, int value, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            // Sort chronologically? because realm is lazily searched there is no
            // guarantee that it will return the last entry inputted
            // TODO Check that this returns the correct result
            RealmQuery<Mood> query = r.where(Mood.class).lessThan("dateTime", currentTime);
            Mood result = query.sort("dateTime", Sort.DESCENDING).findFirst();

            if (result == null) {
                Log.d(TAG, "isThereAnEntryToday: ERROR");
            } else {
                result.setDateTime(currentTime);
                result.setRating(value);
                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "updateSteps onSuccess:");
                Log.d(TAG, "executed transaction : updateMoodToday" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateMoodToday transaction failed: ", error);

            }
        });


        realm.close();

    }
}
