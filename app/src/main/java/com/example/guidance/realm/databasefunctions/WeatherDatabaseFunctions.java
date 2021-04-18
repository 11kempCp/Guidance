package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Weather;

import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;

/**
 * Created by Conor K on 20/03/2021.
 */
public class WeatherDatabaseFunctions {

    private static final String TAG = "WeatherDatabaseFunctions";

    private static final String[] notClearDayParameters = {"Thunderstorm","Drizzle","Rain","Snow","Atmosphere"};
    private static final String[] clearDayParameters = {"Clouds","Clear"};

    public static void weatherEntry(Context context, Date currentTime, String weather, Date sunrise,
                                    Date sunset, double feels_like_morn, double feels_like_day,
                                    double feels_like_eve, double feels_like_night, double temp_max,
                                    double temp_min) {

        Data_Type dataType = getDataType(context);

        if (!isExistingWeatherWeek(context, currentTime)) {
            insertWeather(context, currentTime, weather, sunrise, sunset,
                    feels_like_morn, feels_like_day, feels_like_eve,
                    feels_like_night, temp_max, temp_min, dataType.isWeather(), dataType.isExternal_temp(), dataType.isSun());
        } else {
            updateWeather(context, currentTime, weather, sunrise, sunset,
                    feels_like_morn, feels_like_day, feels_like_eve,
                    feels_like_night, temp_max, temp_min, dataType.isWeather(), dataType.isExternal_temp(), dataType.isSun());
        }

    }

    public static boolean isExistingWeatherToday(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        RealmQuery<Weather> query = realm.where(Weather.class).equalTo("dateTime", currentTime);
        Weather task = query.sort("dateTime", Sort.DESCENDING).findFirst();
//        Log.d(TAG, "isExistingWeather: " + task);

        Log.d(TAG, "isExistingWeatherToday: " + (task != null));
        return task != null;

    }

    public static boolean isExistingWeatherWeek(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.add(Calendar.DATE, 7);
        Date end_date = cal.getTime();

        RealmQuery<Weather> query = realm.where(Weather.class).between("dateTime", currentTime, end_date);

        Weather task = query.sort("dateTime", Sort.DESCENDING).findFirst();
//        Log.d(TAG, "isExistingWeather: " + task);


        Log.d(TAG, "isExistingWeatherWeek: " + (task != null));
        return task != null;
    }


    public static void insertWeather(Context context, Date currentTime, String weather, Date sunrise,
                                     Date sunset, double feels_like_morn, double feels_like_day,
                                     double feels_like_eve, double feels_like_night, double temp_max,
                                     double temp_min, boolean isWeather, boolean isExternalTemp, boolean isSun) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {

            Weather init = r.createObject(Weather.class, new ObjectId());

            init.setDateTime(currentTime);

            if (isWeather) {
                init.setWeather(weather);
            } else {
                init.setWeather(null);

            }


            if (isSun) {
                init.setSunRise(sunrise);
                init.setSunSet(sunset);
            } else {
                init.setSunRise(null);
                init.setSunSet(null);
            }

            if (isExternalTemp) {
                init.setFeels_like_morn(feels_like_morn);
                init.setFeels_like_day(feels_like_day);
                init.setFeels_like_eve(feels_like_eve);
                init.setFeels_like_night(feels_like_night);
                init.setTemp_max(temp_max);
                init.setTemp_min(temp_min);
            } else {
                init.setFeels_like_morn(null);
                init.setFeels_like_day(null);
                init.setFeels_like_eve(null);
                init.setFeels_like_night(null);
                init.setTemp_max(null);
                init.setTemp_min(null);
            }


//            Log.d(TAG, "executed transaction : saveAmbientTempToDatabase " + currentTime);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "AmbientTemp onSuccess:");
                Log.d(TAG, "executed transaction : insertWeather " + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertWeather transaction failed: ", error);

            }
        });
        realm.close();

    }

    public static void updateWeather(Context context, Date currentTime, String weather, Date sunrise,
                                     Date sunset, double feels_like_morn, double feels_like_day,
                                     double feels_like_eve, double feels_like_night, double temp_max,
                                     double temp_min, boolean isWeather, boolean isExternalTemp, boolean isSun) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            // Sort chronologically? because realm is lazily searched there is no
            // guarantee that it will return the last entry inputted

            RealmQuery<Weather> query = r.where(Weather.class).equalTo("dateTime", currentTime);
            Weather result = query.sort("dateTime", Sort.DESCENDING).findFirst();

            if (result == null) {
                Log.d(TAG, "isThereAnEntryToday: ERROR");
            } else {
                Log.d(TAG, "updatingWeather");
                result.setDateTime(currentTime);

                if (isWeather) {
                    result.setWeather(weather);
                } else {
                    result.setWeather(null);

                }


                if (isSun) {
                    result.setSunRise(sunrise);
                    result.setSunSet(sunset);
                } else {
                    result.setSunRise(null);
                    result.setSunSet(null);
                }

                if (isExternalTemp) {
                    result.setFeels_like_morn(feels_like_morn);
                    result.setFeels_like_day(feels_like_day);
                    result.setFeels_like_eve(feels_like_eve);
                    result.setFeels_like_night(feels_like_night);
                    result.setTemp_max(temp_max);
                    result.setTemp_min(temp_min);
                } else {
                    result.setFeels_like_morn(null);
                    result.setFeels_like_day(null);
                    result.setFeels_like_eve(null);
                    result.setFeels_like_night(null);
                    result.setTemp_max(null);
                    result.setTemp_min(null);
                }

                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Date ct = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : updateWeather " + ct);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateWeather transaction failed: ", error);

            }
        });


        realm.close();
    }

    public static Weather getNextAvailableClearDay(Context context, Date currentTime){
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
//        RealmQuery<Weather> tasksQuery = realm.where(Weather.class);
//        RealmQuery<Weather> query = realm.where(Weather.class).greaterThan("dateTime", currentTime).equalTo("weather", "Clear");
        RealmResults<Weather> query = realm.where(Weather.class).greaterThan("dateTime", currentTime).findAll();

        query.sort("dateTime", Sort.ASCENDING);

        for(Weather w: query){
            if(Arrays.asList(clearDayParameters).contains(w.getWeather())){
                return w;
            }
        }
        realm.close();

        return null;
    }
    public static Weather getNextAvailableReasonableTempDay(Context context, Date currentTime){
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Weather> query = realm.where(Weather.class).greaterThan("dateTime", currentTime).findAll();

        query.sort("dateTime", Sort.ASCENDING);

        for(Weather w: query){

            if(w.getTemp_min() >=0 && w.getTemp_max()<=30){
                return w;
            }

        }
        realm.close();

        return null;
    }


}
