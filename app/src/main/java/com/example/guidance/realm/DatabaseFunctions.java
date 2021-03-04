package com.example.guidance.realm;

import android.content.Context;
import android.util.Log;

import com.example.guidance.model.Ambient_Temperature;
import com.example.guidance.model.Data_Storing;
import com.example.guidance.model.Location;
import com.example.guidance.model.Mood;
import com.example.guidance.model.Socialness;
import com.example.guidance.model.Step;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by Conor K on 17/02/2021.
 */
public class DatabaseFunctions {


    private static final String TAG = "DatabaseFunctions";

    public static void insertAmbientTemp(Context context, float sensorValue, Date currentTime) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            Ambient_Temperature init = r.createObject(Ambient_Temperature.class, new ObjectId());
            init.setAmbientTemp(sensorValue);
            init.setDateTime(currentTime);
//            Log.d(TAG, "executed transaction : saveAmbientTempToDatabase " + currentTime);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "AmbientTemp onSuccess:");
                Log.d(TAG, "executed transaction : saveAmbientTempToDatabase" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "saveAmbientTempToDatabase transaction failed: ", error);

            }
        });
        realm.close();

    }

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
                Log.d(TAG, "executed transaction : saveMoodToDatabase" + currentTime);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "saveMoodToDatabase transaction failed: ", error);

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

    public static void socialnessEntry(Context context, Date currentTime, int value) {
        if (isSocialnessEntryToday(context, currentTime)) {
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
//                Log.d(TAG, "updateSteps onSuccess:");
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


    public static boolean isSocialnessEntryToday(Context context, Date currentTime) {
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

    public static int getTodaysSocialnessEntry(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Socialness> query = realm.where(Socialness.class).lessThan("dateTime", currentTime);
        Socialness task = query.sort("dateTime", Sort.DESCENDING).findFirst();
        realm.close();
        return task.getRating();
    }

    public static boolean isStepEntryToday(Context context, Date currentTime) {
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

    public static Data_Storing getDataStoring(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Data_Storing> tasksQuery = realm.where(Data_Storing.class);
//        realm.close();

        return tasksQuery.findFirst();
    }

    public static void initialiseDataStoring(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            // Instantiate the class using the factory function.
            Data_Storing init = r.createObject(Data_Storing.class, new ObjectId());
            // Configure the instance.
            init.setSteps(true);
            init.setDistance_traveled(true);
            init.setLocation(true);
            init.setAmbient_temp(true);
            init.setScreentime(true);
            init.setSleep_tracking(true);
            init.setWeather(true);
            init.setExternal_temp(true);
            init.setSun(true);
            init.setSocialness(true);
            init.setMood(true);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "DataStoring onSuccess:");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "onError: ", error);
            }
        });
        realm.close();
    }

    public static boolean isDataStoringInitialised(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        Data_Storing tasksQuery = realm.where(Data_Storing.class).findFirst();

//        realm.close();

        boolean t = tasksQuery != null;
        Log.d(TAG, "isDataStoringInitialised: " + t);
        return tasksQuery != null;
    }

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
        //TODO currently works bececause of FINE_LOCATION,
        // COARSE_LOCATION might result in there being multiple long/lat being the same in the database? loop over those to find one within the last hour?
        Location task = query.sort("dateTime", Sort.DESCENDING).findFirst();


        if (task == null) {
            Log.d(TAG, "isThereAnEntryToday: false");
            return false;
        } else
            //TODO assess if the date is correct or not
            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
                    task.getDateTime().getYear() == currentTime.getYear() && task.getDateTime().getHours() == currentTime.getHours();

    }

}
