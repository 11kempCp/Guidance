package com.example.guidance.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.guidance.R;
import com.example.guidance.Util.Util;
import com.example.guidance.activity.MainActivity;
import com.example.guidance.app.App;
import com.example.guidance.realm.WeatherInterface;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.onecallWeather.daily;
import com.example.guidance.realm.model.onecallWeather.onecallWeather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.guidance.Util.Util.WEATHER;
import static com.example.guidance.realm.databasefunctions.LocationDatabaseFunctions.getMostRecentLocation;
import static com.example.guidance.realm.databasefunctions.WeatherDatabaseFunctions.weatherEntry;

/**
 * Created by Conor K on 04/03/2021.
 */
public class WeatherService extends Service {

    public static final String TAG = "WeatherService";
    int sID;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        //TODO Make better notification
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle(getString(R.string.weather))
                .setContentText(getString(R.string.notification_weather))
                .setSmallIcon(R.drawable.ic_weather)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(WEATHER, notification);
        sID = startId;


        //call to obtain weather information
        callWeather();


        return START_NOT_STICKY;
    }


    public void callWeather() {

        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        WeatherInterface weather = retrofit.create(WeatherInterface.class);

        String key = getString(R.string.openWeatherapikey);

        Log.d(TAG, "callWeather: " + key);

        Date currentTime = Calendar.getInstance().getTime();

        Location location = getMostRecentLocation(this, currentTime);
        //if is a location entered then it can be used in the openWeather api call
        if (location != null) {
            Call<onecallWeather> call = weather.getDailyWeather(location.getLatitude(), location.getLongitude(), "current,minutely,hourly,alerts", key, "metric");

            //openWeather api call
            call.enqueue(new Callback<onecallWeather>() {
                @Override
                public void onResponse(Call<onecallWeather> call, Response<onecallWeather> response) {
                    Log.d(TAG, "onResponse: ");

                    //if the call is unsuccessful
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "Code " + response.code());
                        return;
                    }

                    onecallWeather weather = response.body();

                    if(weather!=null){
//                        weather.printOneCallWeather(weather);

                        //gets the daily weather from the OneCallWeather object
                        ArrayList<daily> dailyArrayList = weather.getDaily();

                        //loops over each daily entry from openWeather api call
                        for(daily daily: dailyArrayList){
                            weatherEntry(WeatherService.this, Util.convertEpochToDate(daily.getDt()),daily.getWeather().get(0).getMain(),
                                    Util.convertEpochToDate(daily.getSunrise()),Util.convertEpochToDate(daily.getSunset()),
                                    daily.getFeels_like().getMorn(),daily.getFeels_like().getDay(),
                                    daily.getFeels_like().getEve(),daily.getFeels_like().getNight(),
                                    daily.getTemp().getMax(),daily.getTemp().getMin());

                        }
                    }

                    //stops the service and removes the notification
                    stopForeground(true);
                    stopSelfResult(sID);
                }

                @Override
                public void onFailure(Call<onecallWeather> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());

                    stopForeground(true);
                    stopSelfResult(sID);
                }
            });

        }else{
            Log.d(TAG, "No locations entered");
            stopForeground(true);
            stopSelfResult(sID);
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelfResult(sID);
    }




}
