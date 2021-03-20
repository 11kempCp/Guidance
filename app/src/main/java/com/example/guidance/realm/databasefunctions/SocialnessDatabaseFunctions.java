package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.Socialness;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by Conor K on 20/03/2021.
 */
public class SocialnessDatabaseFunctions {

    private static final String TAG = "SocialnessDatabaseFunctions";


    public static void socialnessEntry(Context context, Date currentTime, int value) {
        if (isSocialnessEntryDate(context, currentTime)) {
            updateSocialnessToday(context, value, currentTime);
        } else {
            saveSocialnessToDatabase(context, value, currentTime);
        }
    }

    private static void saveSocialnessToDatabase(Context context, int value, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            Socialness init = r.createObject(Socialness.class, new ObjectId());
            init.setRating(value);
            init.setDateTime(currentTime);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "executed transaction : saveSocialnessToDatabase" + currentTime);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "saveSocialnessToDatabase transaction failed: ", error);

            }
        });
        realm.close();

    }

    private static void updateSocialnessToday(Context context, int value, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            // Sort chronologically? because realm is lazily searched there is no
            // guarantee that it will return the last entry inputted
            // TODO Check that this returns the correct result
            RealmQuery<Socialness> query = r.where(Socialness.class).lessThan("dateTime", currentTime);
            Socialness result = query.sort("dateTime", Sort.DESCENDING).findFirst();

            if (result == null) {
                Log.d(TAG, "isThereAnEntryToday: ERROR");
            } else {
                Log.d(TAG, "settingStepCount");
                result.setDateTime(currentTime);
                result.setRating(value);
                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "executed transaction : updateSocialnessToday" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateSocialnessToday transaction failed: ", error);

            }
        });


        realm.close();

    }


    public static boolean isSocialnessEntryDate(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

//        Socialness task = realm.where(Socialness.class).lessThan("dateTime", currentTime).findFirst();

        RealmQuery<Socialness> query = realm.where(Socialness.class).lessThan("dateTime", currentTime);
        Socialness task = query.sort("dateTime", Sort.DESCENDING).findFirst();


        if (task == null) {
            Log.d(TAG, "isThereAnEntryToday: false");
            return false;
        } else
            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
                    task.getDateTime().getYear() == currentTime.getYear();
    }

    public static int getSocialnessDateRating(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Socialness> query = realm.where(Socialness.class).lessThan("dateTime", currentTime);
        Socialness task = query.sort("dateTime", Sort.DESCENDING).findFirst();
        realm.close();
        return task.getRating();
    }
}
