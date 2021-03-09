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
import com.example.guidance.activity.MainActivity;
import com.example.guidance.app.App;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.onecallWeather.daily;
import com.example.guidance.realm.model.onecallWeather.onecallWeather;
import com.example.guidance.realm.WeatherInterface;
import com.example.guidance.Util.Util;
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

import static com.example.guidance.realm.DatabaseFunctions.getMostRecentLocation;
import static com.example.guidance.realm.DatabaseFunctions.weatherEntry;
import static com.example.guidance.Util.Util.WEATHER;

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

//        Call<Weather> test =  service.getNowWeather("London.uk", "04aad354e4131a7227796a9a38f10262");


//        https://api.openweathermap.org/data/2.5/onecall?lat=51.48&lon=-3.18&exclude=current,minutely,alerts,&appid=04aad354e4131a7227796a9a38f10262&units=metric


        WeatherInterface weather = retrofit.create(WeatherInterface.class);

        String key = getString(R.string.openWeatherapikey);

        Log.d(TAG, "callWeather: " + key);

        Date currentTime = Calendar.getInstance().getTime();

        Location c = getMostRecentLocation(this, currentTime);

        if (c != null) {
            Call<onecallWeather> call = weather.getDailyWeather(c.getLatitude(), c.getLongitude(), "current,minutely,hourly,alerts", key, "metric");


            call.enqueue(new Callback<onecallWeather>() {
                @Override
                public void onResponse(Call<onecallWeather> call, Response<onecallWeather> response) {
                    Log.d(TAG, "onResponse: ");

                    if (!response.isSuccessful()) {
                        Log.d(TAG, "Code " + response.code());
                        return;
                    }

                    onecallWeather weather = response.body();

                    assert weather != null;

                    weather.printOneCallWeather(weather);

                    ArrayList<daily> daily = weather.getDaily();

                    for(daily d: daily){


                        weatherEntry(WeatherService.this, Util.convertEpochToDate(d.getDt()),d.getWeather().get(0).getMain(),
                                Util.convertEpochToDate(d.getSunrise()),Util.convertEpochToDate(d.getSunset()),
                                d.getFeels_like().getMorn(),d.getFeels_like().getDay(),
                                d.getFeels_like().getEve(),d.getFeels_like().getNight(),
                                d.getTemp().getMax(),d.getTemp().getMin());


                    }







//                    ArrayList<hourly> hourly = weather.getHourly();

                    Log.d(TAG, "onResponse: " + response);


//                test.printWeatherObject(test);


//                Log.d(TAG, "onResponse: " + test);

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


//        Call<WeatherCity> call = weather.getCurrentWeather("London,uk", R.string.openWeatherapikey);
//        Call<WeatherCity> call = weather.getCurrentWeather("London,uk", key);


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
