package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.Screentime;
import com.example.guidance.realm.model.Socialness;
import com.example.guidance.realm.model.Step;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
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

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentTime);
        cal1.set(cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH),cal1.get(Calendar.DATE),0,0,0);
        Date beginningOfDay = cal1.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentTime);
        cal2.set(cal2.get(Calendar.YEAR),cal2.get(Calendar.MONTH),cal2.get(Calendar.DATE),23,59,59);
        Date endOfDay = cal2.getTime();


//        RealmQuery<Step> query = realm.where(Step.class).lessThan("dateTime", currentTime);
        RealmQuery<Socialness> query = realm.where(Socialness.class).between("dateTime", beginningOfDay,endOfDay);
        Socialness task = query.sort("dateTime", Sort.DESCENDING).findFirst();


        if (task == null) {
            Log.d(TAG, "isSocialnessEntryDate: false");
            return false;
        } else
            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
                    task.getDateTime().getYear() == currentTime.getYear();
    }

    public static Socialness getSocialnessEntryDate(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentTime);
        cal1.set(cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH),cal1.get(Calendar.DATE),0,0,0);
        Date beginningOfDay = cal1.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentTime);
        cal2.set(cal2.get(Calendar.YEAR),cal2.get(Calendar.MONTH),cal2.get(Calendar.DATE),23,59,59);
        Date endOfDay = cal2.getTime();


//        RealmQuery<Step> query = realm.where(Step.class).lessThan("dateTime", currentTime);
        RealmQuery<Socialness> query = realm.where(Socialness.class).between("dateTime", beginningOfDay,endOfDay);
        Socialness task = query.sort("dateTime", Sort.DESCENDING).findFirst();


        if (task == null) {
            Log.d(TAG, "isThereAnEntryToday: false");
            return null;
        } else if (task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
                task.getDateTime().getYear() == currentTime.getYear()) {

            return task;

        } else {
            return null;
        }

    }

//    public static int getSocialnessDateRating(Context context, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//        RealmQuery<Socialness> query = realm.where(Socialness.class).lessThan("dateTime", currentTime);
//        Socialness task = query.sort("dateTime", Sort.DESCENDING).findFirst();
//        realm.close();
//        return task.getRating();
//    }

    public static Integer getSocialnessDateRating(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentTime);
        cal1.set(cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH),cal1.get(Calendar.DATE),0,0,0);
        Date beginningOfDay = cal1.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentTime);
        cal2.set(cal2.get(Calendar.YEAR),cal2.get(Calendar.MONTH),cal2.get(Calendar.DATE),23,59,59);
        Date endOfDay = cal2.getTime();


//        RealmQuery<Step> query = realm.where(Step.class).lessThan("dateTime", currentTime);
        RealmQuery<Socialness> query = realm.where(Socialness.class).between("dateTime", beginningOfDay,endOfDay);
        Socialness task = query.sort("dateTime", Sort.DESCENDING).findFirst();

        if(task==null){
            return null;
        }

        realm.close();
        return task.getRating();
    }

    public static RealmResults<Socialness> getSocialnessOverPreviousDays(Context context, Date currentTime, int day) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        int Day = -day;
        cal.add(Calendar.DATE, Day);
        Date to = cal.getTime();

        RealmQuery<Socialness> query = realm.where(Socialness.class).between("dateTime", to, currentTime);

        return query.sort("dateTime", Sort.DESCENDING).findAll();
    }

}
