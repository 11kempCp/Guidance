package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.guidance.R;
import com.example.guidance.realm.model.Ambient_Temperature;
import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.DataTypeUsageData;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Mood;
import com.example.guidance.realm.model.Question;
import com.example.guidance.realm.model.Questionnaire;
import com.example.guidance.realm.model.Screentime;
import com.example.guidance.realm.model.Socialness;
import com.example.guidance.realm.model.Step;
import com.example.guidance.realm.model.User_Information;
import com.example.guidance.realm.model.Weather;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.Sort;

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
 * Created by Conor K on 17/02/2021.
 */
public class DatabaseFunctions {


    private static final String TAG = "DatabaseFunctions";

//    public static void insertAmbientTemp(Context context, float sensorValue, Date currentTime) {
//
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        realm.executeTransactionAsync(r -> {
//            Ambient_Temperature init = r.createObject(Ambient_Temperature.class, new ObjectId());
//            init.setAmbientTemp(sensorValue);
//            init.setDateTime(currentTime);
////            Log.d(TAG, "executed transaction : saveAmbientTempToDatabase " + currentTime);
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
////                Log.d(TAG, "AmbientTemp onSuccess:");
//                Log.d(TAG, "executed transaction : saveAmbientTempToDatabase" + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "saveAmbientTempToDatabase transaction failed: ", error);
//
//            }
//        });
//        realm.close();
//
//    }

//    public static void updateStepToday(Context context, float currentSensorValue, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            // Sort chronologically? because realm is lazily searched there is no
//            // guarantee that it will return the last entry inputted
//            RealmQuery<Step> query = r.where(Step.class).lessThan("dateTime", currentTime);
//            Step result = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//            if (result == null) {
//                Log.d(TAG, "isThereAnEntryToday: ERROR");
//            } else {
//                Log.d(TAG, "settingStepCount");
//                result.setDateTime(currentTime);
//                result.setStepCount(currentSensorValue);
//                r.insertOrUpdate(result);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
////                Log.d(TAG, "updateSteps onSuccess:");
//                Log.d(TAG, "executed transaction : updateStepToday" + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "updateStepToday transaction failed: ", error);
//
//            }
//        });
//
//
//        realm.close();
//
//    }

//    public static void moodEntry(Context context, Date currentTime, int value) {
//        if (isMoodEntryToday(context, currentTime)) {
//            updateMoodToday(context, value, currentTime);
//        } else {
//            insertMood(context, value, currentTime);
//        }
//    }
//
//    public static int getTodaysMoodEntry(Context context, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        RealmQuery<Mood> query = realm.where(Mood.class).lessThan("dateTime", currentTime);
//        Mood task = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//        realm.close();
//        return task.getRating();
//    }
//
//    public static boolean isMoodEntryToday(Context context, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        RealmQuery<Mood> query = realm.where(Mood.class).lessThan("dateTime", currentTime);
//        Mood task = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//
//        if (task == null) {
//            Log.d(TAG, "isThereAnEntryToday: false");
//            return false;
//        } else
//            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
//                    task.getDateTime().getYear() == currentTime.getYear();
//    }
//
//    private static void insertMood(Context context, int value, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            Mood init = r.createObject(Mood.class, new ObjectId());
//            init.setRating(value);
//            init.setDateTime(currentTime);
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "executed transaction : insertMood" + currentTime);
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "insertMood transaction failed: ", error);
//
//            }
//        });
//        realm.close();
//
//    }
//
//    private static void updateMoodToday(Context context, int value, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            // Sort chronologically? because realm is lazily searched there is no
//            // guarantee that it will return the last entry inputted
//            RealmQuery<Mood> query = r.where(Mood.class).lessThan("dateTime", currentTime);
//            Mood result = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//            if (result == null) {
//                Log.d(TAG, "isThereAnEntryToday: ERROR");
//            } else {
//                result.setDateTime(currentTime);
//                result.setRating(value);
//                r.insertOrUpdate(result);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
////                Log.d(TAG, "updateSteps onSuccess:");
//                Log.d(TAG, "executed transaction : updateMoodToday" + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "updateMoodToday transaction failed: ", error);
//
//            }
//        });
//
//
//        realm.close();
//
//    }

//    public static void socialnessEntry(Context context, Date currentTime, int value) {
//        if (isSocialnessEntryDate(context, currentTime)) {
//            updateSocialnessToday(context, value, currentTime);
//        } else {
//            saveSocialnessToDatabase(context, value, currentTime);
//        }
//    }
//
//    private static void saveSocialnessToDatabase(Context context, int value, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            Socialness init = r.createObject(Socialness.class, new ObjectId());
//            init.setRating(value);
//            init.setDateTime(currentTime);
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "executed transaction : saveSocialnessToDatabase" + currentTime);
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "saveSocialnessToDatabase transaction failed: ", error);
//
//            }
//        });
//        realm.close();
//
//    }
//
//    private static void updateSocialnessToday(Context context, int value, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            // Sort chronologically? because realm is lazily searched there is no
//            // guarantee that it will return the last entry inputted
//            RealmQuery<Socialness> query = r.where(Socialness.class).lessThan("dateTime", currentTime);
//            Socialness result = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//            if (result == null) {
//                Log.d(TAG, "isThereAnEntryToday: ERROR");
//            } else {
//                Log.d(TAG, "settingStepCount");
//                result.setDateTime(currentTime);
//                result.setRating(value);
//                r.insertOrUpdate(result);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "executed transaction : updateSocialnessToday" + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "updateSocialnessToday transaction failed: ", error);
//
//            }
//        });
//
//
//        realm.close();
//
//    }
//
//
//    public static boolean isSocialnessEntryDate(Context context, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
////        Socialness task = realm.where(Socialness.class).lessThan("dateTime", currentTime).findFirst();
//
//        RealmQuery<Socialness> query = realm.where(Socialness.class).lessThan("dateTime", currentTime);
//        Socialness task = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//
//        if (task == null) {
//            Log.d(TAG, "isThereAnEntryToday: false");
//            return false;
//        } else
//            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
//                    task.getDateTime().getYear() == currentTime.getYear();
//    }
//
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

//    public static boolean isStepEntryDate(Context context, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
////        Step task = realm.where(Step.class).lessThan("dateTime", currentTime).findFirst();
//
//        RealmQuery<Step> query = realm.where(Step.class).lessThan("dateTime", currentTime);
//        Step task = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//
//        if (task == null) {
//            Log.d(TAG, "isThereAnEntryToday: false");
//            return false;
//        } else
//            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
//                    task.getDateTime().getYear() == currentTime.getYear();
//    }
//
//    public static void insertStepsCounter(Context context, float currentSensorValue, Date currentTime) {
//
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            Step init = r.createObject(Step.class, new ObjectId());
//            init.setStepCount(currentSensorValue);
//            init.setDateTime(currentTime);
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "executed transaction : saveStepsCounterToDatabase" + currentTime);
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "saveStepsCounterToDatabase transaction failed: ", error);
//
//            }
//        });
//        realm.close();
//
//    }

//    public static Data_Type getDataType(Context context) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//        RealmQuery<Data_Type> tasksQuery = realm.where(Data_Type.class);
////        realm.close();
//
//        return tasksQuery.findFirst();
//    }
//
//    public static void initialiseDataType(Context context) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            // Instantiate the class using the factory function.
//            Data_Type init = r.createObject(Data_Type.class, new ObjectId());
//            // Configure the instance.
//            init.setSteps(true);
//            init.setDistance_traveled(true);
//            init.setLocation(true);
//            init.setAmbient_temp(true);
//            init.setScreentime(true);
//            init.setSleep_tracking(true);
//            init.setWeather(true);
//            init.setExternal_temp(true);
//            init.setSun(true);
//            init.setSocialness(true);
//            init.setMood(true);
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "DataStoring onSuccess:");
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "onError: ", error);
//            }
//        });
//        realm.close();
//    }
//
//    public static boolean isDataTypeInitialised(Context context) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//        Data_Type tasksQuery = realm.where(Data_Type.class).findFirst();
//
////        realm.close();
//
//        boolean t = tasksQuery != null;
//        Log.d(TAG, "isDataStoringInitialised: " + t);
//        return tasksQuery != null;
//    }

//    public static void insertLocation(Context context, Date currentTime, double latitude, double longitude) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            Location init = r.createObject(Location.class, new ObjectId());
//            init.setDateTime(currentTime);
//            init.setLatitude(latitude);
//            init.setLongitude(longitude);
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "executed transaction : insertLocation " + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "insertLocation transaction failed: ", error);
//
//            }
//        });
//
//
//        realm.close();
//    }
//
//
//    public static void locationEntry(Context context, Date currentTime, double latitude, double longitude) {
//
//        if (!isLocationStoredAlready(context, currentTime, latitude, longitude)) {
//            insertLocation(context, currentTime, latitude, longitude);
//        } else {
//            Log.d(TAG, "locationEntry: entry already entered");
//        }
//    }
//
//    public static boolean isLocationStoredAlready(Context context, Date currentTime, double latitude, double longitude) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        RealmQuery<Location> query = realm.where(Location.class).equalTo("latitude", latitude).equalTo("longitude", longitude);
//        //Finds the last entry at the specified coordinates
//        Location task = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//
//        if (task == null) {
//            Log.d(TAG, "task null ");
//            return false;
//
//        } else {
//            long timestamp1 = task.getDateTime().getTime();
////            Log.d(TAG, "isLocationStoredAlready: timestamp2 " + timestamp1);
//            long timestamp2 = currentTime.getTime();
////            Log.d(TAG, "isLocationStoredAlready: timestamp1 " + timestamp2);
//
//
////            Log.d(TAG, "isLocationStoredAlready: calculation1 " + Math.abs(timestamp1 - timestamp2));
////            Log.d(TAG, "isLocationStoredAlready: calculation2 " + TimeUnit.MINUTES.toMillis(context.getResources().getInteger(R.integer.location) / 2));
//
//            return Math.abs(timestamp1 - timestamp2) < TimeUnit.MINUTES.toMillis(context.getResources().getInteger(R.integer.location) / 2);
//
//        }


//        if (task == null) {
//            return false;
//        } else if(task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
//                task.getDateTime().getYear() == currentTime.getYear() && task.getDateTime().getHours() == currentTime.getHours()){
//            int minutes = task.getDateTime().getMinutes();
//            int current_minutes = currentTime.getMinutes();
//            if(minutes <= (current_minutes - 30)){
//
//            }
//
//        }
//    }

//    public static Location getMostRecentLocation(Context context, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        RealmQuery<Location> query = realm.where(Location.class).lessThan("dateTime", currentTime);
//
//        return query.sort("dateTime", Sort.DESCENDING).findFirst();
//    }


//    public static void weatherEntry(Context context, Date currentTime, String weather, Date sunrise,
//                                    Date sunset, double feels_like_morn, double feels_like_day,
//                                    double feels_like_eve, double feels_like_night, double temp_max,
//                                    double temp_min) {
//
//        Data_Type dataType = getDataType(context);
//
//        if (!isExistingWeatherWeek(context, currentTime)) {
//            insertWeather(context, currentTime, weather, sunrise, sunset,
//                    feels_like_morn, feels_like_day, feels_like_eve,
//                    feels_like_night, temp_max, temp_min, dataType.isWeather(), dataType.isExternal_temp(), dataType.isSun());
//        } else {
//            updateWeather(context, currentTime, weather, sunrise, sunset,
//                    feels_like_morn, feels_like_day, feels_like_eve,
//                    feels_like_night, temp_max, temp_min, dataType.isWeather(), dataType.isExternal_temp(), dataType.isSun());
//        }
//
//    }
//
//    public static boolean isExistingWeatherToday(Context context, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        RealmQuery<Weather> query = realm.where(Weather.class).equalTo("dateTime", currentTime);
//        Weather task = query.sort("dateTime", Sort.DESCENDING).findFirst();
////        Log.d(TAG, "isExistingWeather: " + task);
//
//        Log.d(TAG, "isExistingWeatherToday: " + (task != null));
//        return task != null;
//
//    }
//
//    public static boolean isExistingWeatherWeek(Context context, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(currentTime);
//        cal.add(Calendar.DATE, 7);
//        Date end_date = cal.getTime();
//
//        RealmQuery<Weather> query = realm.where(Weather.class).between("dateTime", currentTime, end_date);
//
//        Weather task = query.sort("dateTime", Sort.DESCENDING).findFirst();
////        Log.d(TAG, "isExistingWeather: " + task);
//
//
//        Log.d(TAG, "isExistingWeatherWeek: " + (task != null));
//        return task != null;
//    }
//
//
//    public static void insertWeather(Context context, Date currentTime, String weather, Date sunrise,
//                                     Date sunset, double feels_like_morn, double feels_like_day,
//                                     double feels_like_eve, double feels_like_night, double temp_max,
//                                     double temp_min, boolean isWeather, boolean isExternalTemp, boolean isSun) {
//
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        realm.executeTransactionAsync(r -> {
//
//            Weather init = r.createObject(Weather.class, new ObjectId());
//
//            init.setDateTime(currentTime);
//
//            if (isWeather) {
//                init.setWeather(weather);
//            } else {
//                init.setWeather(null);
//
//            }
//
//
//            if (isSun) {
//                init.setSunRise(sunrise);
//                init.setSunSet(sunset);
//            } else {
//                init.setSunRise(null);
//                init.setSunSet(null);
//            }
//
//            if (isExternalTemp) {
//                init.setFeels_like_morn(feels_like_morn);
//                init.setFeels_like_day(feels_like_day);
//                init.setFeels_like_eve(feels_like_eve);
//                init.setFeels_like_night(feels_like_night);
//                init.setTemp_max(temp_max);
//                init.setTemp_min(temp_min);
//            } else {
//                init.setFeels_like_morn(null);
//                init.setFeels_like_day(null);
//                init.setFeels_like_eve(null);
//                init.setFeels_like_night(null);
//                init.setTemp_max(null);
//                init.setTemp_min(null);
//            }
//
//
////            Log.d(TAG, "executed transaction : saveAmbientTempToDatabase " + currentTime);
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
////                Log.d(TAG, "AmbientTemp onSuccess:");
//                Log.d(TAG, "executed transaction : insertWeather " + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "insertWeather transaction failed: ", error);
//
//            }
//        });
//        realm.close();
//
//    }
//
//    public static void updateWeather(Context context, Date currentTime, String weather, Date sunrise,
//                                     Date sunset, double feels_like_morn, double feels_like_day,
//                                     double feels_like_eve, double feels_like_night, double temp_max,
//                                     double temp_min, boolean isWeather, boolean isExternalTemp, boolean isSun) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            // Sort chronologically? because realm is lazily searched there is no
//            // guarantee that it will return the last entry inputted
//            // TODO Check that this returns the correct result
//            RealmQuery<Weather> query = r.where(Weather.class).equalTo("dateTime", currentTime);
//            Weather result = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//            if (result == null) {
//                Log.d(TAG, "isThereAnEntryToday: ERROR");
//            } else {
//                Log.d(TAG, "updatingWeather");
//                result.setDateTime(currentTime);
//
//                if (isWeather) {
//                    result.setWeather(weather);
//                } else {
//                    result.setWeather(null);
//
//                }
//
//
//                if (isSun) {
//                    result.setSunRise(sunrise);
//                    result.setSunSet(sunset);
//                } else {
//                    result.setSunRise(null);
//                    result.setSunSet(null);
//                }
//
//                if (isExternalTemp) {
//                    result.setFeels_like_morn(feels_like_morn);
//                    result.setFeels_like_day(feels_like_day);
//                    result.setFeels_like_eve(feels_like_eve);
//                    result.setFeels_like_night(feels_like_night);
//                    result.setTemp_max(temp_max);
//                    result.setTemp_min(temp_min);
//                } else {
//                    result.setFeels_like_morn(null);
//                    result.setFeels_like_day(null);
//                    result.setFeels_like_eve(null);
//                    result.setFeels_like_night(null);
//                    result.setTemp_max(null);
//                    result.setTemp_min(null);
//                }
//
//                r.insertOrUpdate(result);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Date ct = Calendar.getInstance().getTime();
//                Log.d(TAG, "executed transaction : updateWeather " + ct);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "updateWeather transaction failed: ", error);
//
//            }
//        });
//
//
//        realm.close();
//    }


//    public static void initialiseIntelligentAgent(Context context, String passcode, int study_duration_days) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        realm.executeTransactionAsync(r -> {
//            Intelligent_Agent init = r.createObject(Intelligent_Agent.class, new ObjectId());
//
//            String Analysis = "";
//            String Advice = "";
//            String Gender = "";
//            String Interaction = "";
//            String Output = "";
//
//            if (passcode.contains(MACHINE_LEARNING)) {
//                Analysis = MACHINE_LEARNING;
//            } else if (passcode.contains(TRADITIONAL_PROGRAMMING)) {
//                Analysis = TRADITIONAL_PROGRAMMING;
//            }
//
//            if (passcode.contains(NO_JUSTIFICATION)) {
//                Advice = NO_JUSTIFICATION;
//            } else if (passcode.contains(WITH_JUSTIFICATION)) {
//                Advice = WITH_JUSTIFICATION;
//            }
//
//            if (passcode.contains(FEMALE)) {
//                Gender = FEMALE;
//            } else if (passcode.contains(MALE)) {
//                Gender = MALE;
//            }
//
//
//            if (passcode.contains(HIGH)) {
//                Interaction = HIGH;
//            } else if (passcode.contains(LOW)) {
//                Interaction = LOW;
//            }
//
//            if (passcode.contains(SPEECH)) {
//                Output = SPEECH;
//            } else if (passcode.contains(TEXT)) {
//                Output = TEXT;
//            }
//
//            Date currentTime = Calendar.getInstance().getTime();
//
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(currentTime);
//            cal.add(Calendar.DATE, study_duration_days);
//
//            Date end_date = cal.getTime();
//
//            init.setDate_Initialised(currentTime);
//            init.setEnd_Date(end_date);
//            init.setAnalysis(Analysis);
//            init.setAdvice(Advice);
//            init.setGender(Gender);
//            init.setInteraction(Interaction);
//            init.setOutput(Output);
//
//
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Date currentTime = Calendar.getInstance().getTime();
//                Log.d(TAG, "executed transaction : initialiseIntelligentAgent" + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "initialiseIntelligentAgent transaction failed: ", error);
//            }
//        });
//        realm.close();
//    }
//
//    public static void intelligentAgentEntry(Context context, String passcode) {
//
//
//        if (!isIntelligentAgentInitialised(context)) {
//            initialiseIntelligentAgent(context, passcode, R.integer.study_period_length_days);
//        } else {
//            Log.d(TAG, "intelligentAgentEntry: IA already initialised");
//        }
//    }
//
//    public static boolean isIntelligentAgentInitialised(Context context) {
//
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        Intelligent_Agent query = realm.where(Intelligent_Agent.class).findFirst();
//        Log.d(TAG, "isIntelligentAgentInitialised: query " + query);
//        return query != null;
//
//    }
//
//    public static void intelligentAgentSetGender(Context context, String gender) {
//
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//
//            Intelligent_Agent result = r.where(Intelligent_Agent.class).findFirst();
//
//            if (result == null) {
//                Log.d(TAG, "intelligentAgentSetGender: ERROR");
//            } else {
//
//                result.setGender(gender);
//                r.insertOrUpdate(result);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
////                Log.d(TAG, "updateSteps onSuccess:");
//                Date ct = Calendar.getInstance().getTime();
//                Log.d(TAG, "executed transaction : intelligentAgentSetGender " + gender + " " + ct);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "updateStepToday transaction failed: ", error);
//
//            }
//        });
//
//
//        realm.close();
//
//    }


//    public static void insertQuestionnaire(Context context, String[] questions, String[] answers, Date currentTime) {
//
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        realm.executeTransactionAsync(r -> {
//            Questionnaire init = r.createObject(Questionnaire.class, new ObjectId());
//            init.setDateTime(currentTime);
//            for (int i = 0; i < questions.length; i++) {
//                Question question = r.createObject(Question.class, new ObjectId());
//                question.setAnswer(answers[i]);
//                question.setQuestion(questions[i]);
//                init.getQuestion().add(question);
//
//            }
//
//
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
////                Log.d(TAG, "AmbientTemp onSuccess:");
//                Date currentTime = Calendar.getInstance().getTime();
//                Log.d(TAG, "executed transaction : insertQuestionaire" + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "insertQuestionaire transaction failed: ", error);
//
//            }
//        });
//        realm.close();
//
//    }
//
//    public static boolean isQuestionaireAnswered(Context context) {
//
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        Questionnaire query = realm.where(Questionnaire.class).findFirst();
//        Log.d(TAG, "isQuestionaireAnswered: query " + query);
//        return query != null;
//
//    }
//
//    public static Questionnaire getQuestionnaire(Context context) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//        RealmQuery<Questionnaire> tasksQuery = realm.where(Questionnaire.class);
////        realm.close();
//
//        return tasksQuery.findFirst();
//    }

//    public static Intelligent_Agent getIntelligentAgent(Context context) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//        RealmQuery<Intelligent_Agent> tasksQuery = realm.where(Intelligent_Agent.class);
////        realm.close();
//
//        return tasksQuery.findFirst();
//    }

//    public static void insertDataTypeUsageData(Context context, Date currentTime, String data_type, boolean status) {
//
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        realm.executeTransactionAsync(r -> {
//            DataTypeUsageData init = r.createObject(DataTypeUsageData.class, new ObjectId());
//            init.setDateTime(currentTime);
//            init.setData_type(data_type);
//            init.setStatus(status);
//
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
////                Log.d(TAG, "AmbientTemp onSuccess:");
//                Date currentTime = Calendar.getInstance().getTime();
////                Log.d(TAG, "executed transaction : insertDataTypeUsageData " + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "insertDataTypeUsageData transaction failed: ", error);
//
//            }
//        });
//        realm.close();
//    }

//    public static void screentimeEntry(Context context, Date currentTime, int interval_type, ArrayList<AppData> appData) {
//
//        if (isScreentimeEntryToday(context, currentTime)) {
//            updateScreentimeToday(context, currentTime, interval_type, appData);
//        } else {
//            insertScreentime(context, currentTime, interval_type, appData);
//        }
//    }
//
//    public static boolean isScreentimeEntryToday(Context context, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//        RealmQuery<Screentime> query = realm.where(Screentime.class).lessThan("dateTime", currentTime);
//        Screentime task = query.sort("dateTime", Sort.DESCENDING).findFirst();
//        if (task == null) {
//            Log.d(TAG, "isScreentimeEntryToday: false");
//            return false;
//        } else
//            return task.getDateTime().getDate() == currentTime.getDate() && task.getDateTime().getMonth() == currentTime.getMonth() &&
//                    task.getDateTime().getYear() == currentTime.getYear();
//    }
//
//    public static void updateScreentimeToday(Context context, Date currentTime, int interval_type, ArrayList<AppData> appData) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            // Sort chronologically? because realm is lazily searched there is no
//            // guarantee that it will return the last entry inputted
//            RealmQuery<Screentime> query = r.where(Screentime.class).lessThan("dateTime", currentTime);
//            Screentime result = query.sort("dateTime", Sort.DESCENDING).findFirst();
//
//            if (result == null) {
//                Log.d(TAG, "updateScreentimeToday: ERROR");
//            } else {
//                result.setDateTime(currentTime);
//                result.setInterval_type(interval_type);
//                boolean packageInAppDataAlready;
//                for (AppData app : appData) {
//                    packageInAppDataAlready = false;
//                    for (AppData ad : result.getAppData()) {
//
//                        if (ad.getPackageName().equals(app.getPackageName())) {
//                            ad.setTotalTimeInForeground(app.getTotalTimeInForeground());
//                            ad.setTotalTimeVisible(app.getTotalTimeVisible());
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                ad.setTotalTimeForegroundServiceUsed(app.getTotalTimeForegroundServiceUsed());
//                                ad.setTotalTimeVisible(app.getTotalTimeVisible());
//
//                            }
//
//                            packageInAppDataAlready = true;
//                            break;
//                        }
//                    }
//                    if (!packageInAppDataAlready) {
//                        AppData applicationData = r.createObject(AppData.class, new ObjectId());
//
//                        applicationData.setPackageName(app.getPackageName());
//                        applicationData.setTotalTimeInForeground(app.getTotalTimeInForeground());
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                            applicationData.setTotalTimeVisible(app.getTotalTimeVisible());
//                            applicationData.setTotalTimeForegroundServiceUsed(app.getTotalTimeForegroundServiceUsed());
//                        } else {
//                            applicationData.setTotalTimeVisible(null);
//                            applicationData.setTotalTimeForegroundServiceUsed(null);
//                        }
//
//
//                        result.getAppData().add(applicationData);
//
//                    }
//                }
//
//                r.insertOrUpdate(result);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
////                Log.d(TAG, "updateSteps onSuccess:");
//                Log.d(TAG, "executed transaction : updateScreentimeToday" + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "updateScreentimeToday transaction failed: ", error);
//
//            }
//        });
//
//        realm.close();
//    }
//
//    private static void insertScreentime(Context context, Date currentTime, int interval_type, ArrayList<AppData> appData) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(r -> {
//            Screentime init = r.createObject(Screentime.class, new ObjectId());
//            init.setDateTime(currentTime);
//            init.setInterval_type(interval_type);
//
//            for (AppData app : appData) {
//                AppData applicationData = r.createObject(AppData.class, new ObjectId());
//
//                applicationData.setPackageName(app.getPackageName());
//                applicationData.setTotalTimeInForeground(app.getTotalTimeInForeground());
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    applicationData.setTotalTimeVisible(app.getTotalTimeVisible());
//                    applicationData.setTotalTimeForegroundServiceUsed(app.getTotalTimeForegroundServiceUsed());
//                } else {
//                    applicationData.setTotalTimeVisible(null);
//                    applicationData.setTotalTimeForegroundServiceUsed(null);
//                }
//
//                init.getAppData().add(applicationData);
//            }
//
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Log.d(TAG, "executed transaction : insertScreentime" + currentTime);
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "insertScreentime transaction failed: ", error);
//
//            }
//        });
//        realm.close();
//
//    }

//    public static boolean isUserInformationInitialised(Context context) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//        User_Information query = realm.where(User_Information.class).findFirst();
//        Log.d(TAG, "isUserInformationInitialised: query " + query);
//        return query != null;
//    }
//
//
//    public static void userInformationEntry(Context context, String name, Integer age, String gender) {
//        if (!isUserInformationInitialised(context)) {
//            initialiseUserInformation(context, name, age, gender);
//        } else {
//            Log.d(TAG, "userInformationEntry: User Information already entered");
//        }
//    }
//
//    public static void initialiseUserInformation(Context context, String name, Integer age, String gender) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();
//
//
//        realm.executeTransactionAsync(r -> {
//            User_Information init = r.createObject(User_Information.class, new ObjectId());
//
//
//            init.setName(name);
//            init.setAge(age);
//            init.setGender(gender);
//
//
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Date currentTime = Calendar.getInstance().getTime();
//                Log.d(TAG, "executed transaction : initialiseUserInformation" + currentTime);
//
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                Log.e(TAG, "initialiseUserInformation transaction failed: ", error);
//            }
//        });
//        realm.close();
//    }
}
