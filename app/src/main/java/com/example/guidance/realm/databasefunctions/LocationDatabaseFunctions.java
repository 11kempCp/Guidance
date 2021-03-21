package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.R;
import com.example.guidance.realm.model.Location;
import com.google.android.gms.tasks.Task;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Conor K on 20/03/2021.
 */
public class LocationDatabaseFunctions {

    private static final String TAG = "LocationDatabaseFunctions";

    public static void insertLocation(Context context, Date currentTime, double latitude, double longitude) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            Location init = r.createObject(Location.class, new ObjectId());
            init.setDateTime(currentTime);
            init.setLatitude(latitude);
            init.setLongitude(longitude);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "executed transaction : insertLocation " + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertLocation transaction failed: ", error);

            }
        });


        realm.close();
    }


    public static void locationEntry(Context context, Date currentTime, double latitude, double longitude) {

        if (!isLocationStoredAlready(context, currentTime, latitude, longitude)) {
            insertLocation(context, currentTime, latitude, longitude);
        } else {
            Log.d(TAG, "locationEntry: entry already entered");
        }
    }

    public static boolean isLocationStoredAlready(Context context, Date currentTime, double latitude, double longitude) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        RealmQuery<Location> query = realm.where(Location.class).equalTo("latitude", latitude).equalTo("longitude", longitude);
        //Finds the last entry at the specified coordinates
        Location task = query.sort("dateTime", Sort.DESCENDING).findFirst();

        Log.d(TAG, "isLocationStoredAlready: "  + task + " lat " + latitude  + " lon " + longitude);

        if (task == null) {
            Log.d(TAG, "task null ");
            return false;

        } else {
            long timestamp1 = task.getDateTime().getTime();
            long timestamp2 = currentTime.getTime();
            return Math.abs(timestamp1 - timestamp2) < TimeUnit.MINUTES.toMillis(context.getResources().getInteger(R.integer.location) / 2);

        }
    }

    public static Location getMostRecentLocation(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        RealmQuery<Location> query = realm.where(Location.class).lessThan("dateTime", currentTime);

        return query.sort("dateTime", Sort.DESCENDING).findFirst();
    }



    public static RealmResults<Location> getLocationOverPreviousDays(Context context, Date currentTime, int day) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        int Day = -day;
        cal.add(Calendar.DATE, Day);
        Date to = cal.getTime();

//        RealmQuery<Location> query = realm.where(Location.class).lessThan("dateTime", currentTime);
        RealmQuery<Location> query = realm.where(Location.class).between("dateTime", to, currentTime);

        return query.sort("dateTime", Sort.DESCENDING).findAll();
    }

}
