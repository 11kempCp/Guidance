package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.guidance.Util.Util;
import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.Screentime;

import org.bson.types.ObjectId;

import java.util.ArrayList;
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
public class ScreentimeDatabaseFunctions {

    private static final String TAG = "ScreentimeDatabaseFunctions";


    public static void screentimeEntry(Context context, Date currentTime, int interval_type, ArrayList<AppData> appData) {

        if (isScreentimeEntryToday(context, currentTime)) {
            updateScreentimeToday(context, currentTime, interval_type, appData);
        } else {
            insertScreentime(context, currentTime, interval_type, appData);
        }
    }

    public static boolean isScreentimeEntryToday(Context context, Date currentTime) {
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
        RealmQuery<Screentime> query = realm.where(Screentime.class).between("dateTime", beginningOfDay,endOfDay);
        Screentime task = query.sort("dateTime", Sort.DESCENDING).findFirst();


        if (task == null) {
            Log.d(TAG, "isThereAnEntryToday: false");
            return false;
        } else
            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
                    task.getDateTime().getYear() == currentTime.getYear();
    }

    public static void updateScreentimeToday(Context context, Date currentTime, int interval_type, ArrayList<AppData> appData) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            // Sort chronologically? because realm is lazily searched there is no
            // guarantee that it will return the last entry inputted
            RealmQuery<Screentime> query = r.where(Screentime.class).lessThan("dateTime", currentTime);
            Screentime result = query.sort("dateTime", Sort.DESCENDING).findFirst();

            if (result == null) {
                Log.d(TAG, "updateScreentimeToday: ERROR");
            } else {
                result.setDateTime(currentTime);
                result.setInterval_type(interval_type);
                boolean packageInAppDataAlready;
                for (AppData app : appData) {
                    packageInAppDataAlready = false;
                    for (AppData ad : result.getAppData()) {

                        if (ad.getPackageName().equals(app.getPackageName())) {
                            ad.setTotalTimeInForeground(app.getTotalTimeInForeground());
                            ad.setTotalTimeVisible(app.getTotalTimeVisible());

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                ad.setTotalTimeForegroundServiceUsed(app.getTotalTimeForegroundServiceUsed());
                                ad.setTotalTimeVisible(app.getTotalTimeVisible());
                            }

                            packageInAppDataAlready = true;
                            break;
                        }
                    }
                    if (!packageInAppDataAlready) {
                        AppData applicationData = r.createObject(AppData.class, new ObjectId());

                        applicationData.setPackageName(app.getPackageName());
                        applicationData.setTotalTimeInForeground(app.getTotalTimeInForeground());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            applicationData.setTotalTimeVisible(app.getTotalTimeVisible());
                            applicationData.setTotalTimeForegroundServiceUsed(app.getTotalTimeForegroundServiceUsed());
                        } else {
                            applicationData.setTotalTimeVisible(null);
                            applicationData.setTotalTimeForegroundServiceUsed(null);
                        }


                        result.getAppData().add(applicationData);

                    }
                }

                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "updateSteps onSuccess:");
                Log.d(TAG, "executed transaction : updateScreentimeToday" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateScreentimeToday transaction failed: ", error);

            }
        });

        realm.close();
    }

    private static void insertScreentime(Context context, Date currentTime, int interval_type, ArrayList<AppData> appData) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            Screentime init = r.createObject(Screentime.class, new ObjectId());
            init.setDateTime(currentTime);
            init.setInterval_type(interval_type);

            for (AppData app : appData) {
                AppData applicationData = r.createObject(AppData.class, new ObjectId());

                applicationData.setPackageName(app.getPackageName());
                applicationData.setTotalTimeInForeground(app.getTotalTimeInForeground());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    applicationData.setTotalTimeVisible(app.getTotalTimeVisible());
                    applicationData.setTotalTimeForegroundServiceUsed(app.getTotalTimeForegroundServiceUsed());
                } else {
                    applicationData.setTotalTimeVisible(null);
                    applicationData.setTotalTimeForegroundServiceUsed(null);
                }

                init.getAppData().add(applicationData);
            }

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "executed transaction : insertScreentime" + currentTime);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertScreentime transaction failed: ", error);

            }
        });
        realm.close();

    }

    public static RealmResults<Screentime> getScreentimeOverPreviousDays(Context context, Date currentTime, int day) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        int Day = -day;
        cal.add(Calendar.DATE, Day);
        Date to = cal.getTime();

        RealmQuery<Screentime> query = realm.where(Screentime.class).between("dateTime", to, currentTime);

        return query.sort("dateTime", Sort.DESCENDING).findAll();
    }

    public static Screentime getScreentimePreviousDay(Context context, Date currentTime, int day) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


//        Calendar cal = Calendar.getInstance();
//        cal.setTime(currentTime);
//        int Day = -day;
//        cal.add(Calendar.DATE, Day);
//        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),23,59,59);
//        Date previousDay = cal.getTime();
        Date previousDay = Util.changeDayEndOfDay(currentTime,-1);

        Log.d(TAG, "getScreentimePreviousDay: " + previousDay);

        RealmQuery<Screentime> query = realm.where(Screentime.class).lessThan("dateTime", previousDay);

        return query.sort("dateTime", Sort.DESCENDING).findFirst();
    }



    public static Screentime getScreentimeDate(Context context, Date currentTime) {
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
        RealmQuery<Screentime> query = realm.where(Screentime.class).between("dateTime", beginningOfDay,endOfDay);
        Screentime task = query.sort("dateTime", Sort.DESCENDING).findFirst();


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
}
