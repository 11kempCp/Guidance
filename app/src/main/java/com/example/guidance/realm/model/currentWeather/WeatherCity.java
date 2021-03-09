package com.example.guidance.realm.model.currentWeather;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Conor K on 04/03/2021.
 */
public class WeatherCity {

    String TAG = "WeatherMain";

    private final coord coord;
    private final ArrayList<weather> weather;
    private final String base;
    private final main main;
    private final int visibility;
    private final wind wind;
    private final clouds clouds;
    private final long dt;
    private final sys sys;
    private final int timezone;
    private final int id;
    private final String name;
    private final int cod;

    public WeatherCity(com.example.guidance.realm.model.currentWeather.coord coord, ArrayList<com.example.guidance.realm.model.currentWeather.weather> weather, String base, com.example.guidance.realm.model.currentWeather.main main, int visibility, com.example.guidance.realm.model.currentWeather.wind wind, com.example.guidance.realm.model.currentWeather.clouds clouds, long dt, com.example.guidance.realm.model.currentWeather.sys sys, int timezone, int id, String name, int cod) {
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.timezone = timezone;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }

    public com.example.guidance.realm.model.currentWeather.coord getCoord() {
        return coord;
    }

    public ArrayList<com.example.guidance.realm.model.currentWeather.weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public com.example.guidance.realm.model.currentWeather.main getMain() {
        return main;
    }

    public int getVisibility() {
        return visibility;
    }

    public com.example.guidance.realm.model.currentWeather.wind getWind() {
        return wind;
    }

    public com.example.guidance.realm.model.currentWeather.clouds getClouds() {
        return clouds;
    }

    public long getDt() {
        return dt;
    }

    public com.example.guidance.realm.model.currentWeather.sys getSys() {
        return sys;
    }

    public int getTimezone() {
        return timezone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }


    public void printWeatherCity(WeatherCity test) {


        Log.d(TAG, test.getCoord().getLat() + " " + test.getCoord().getLon());

        ArrayList<com.example.guidance.realm.model.currentWeather.weather> blank = test.getWeather();

        for (weather t : blank) {
            Log.d(TAG, t.getId() + " " + t.getMain() + " " + t.getDescription() + " " + t.getIcon());
        }


//        Log.d(TAG, test.getWeatherTest().get(0).getId() + " " + test.getWeatherTest().get(0).getMain() + " " + test.getWeatherTest().get(0).getDescription() + " " + test.getWeatherTest().get(0).getIcon());
        Log.d(TAG, test.getBase());
        Log.d(TAG, test.getMain().getTemp() + " " + test.getMain().getFeels_like() + " " + test.getMain().getTemp_min() + " " + test.getMain().getTemp_max() + " " + test.getMain().getPressure() + " " + test.getMain().getHumidity());
        Log.d(TAG, String.valueOf(test.getVisibility()));
        Log.d(TAG, test.getWind().getDeg() + " " + test.getWind().getSpeed());
        Log.d(TAG, String.valueOf(test.getClouds().getAll()));
        Log.d(TAG, String.valueOf(test.getDt()));
        Log.d(TAG, test.getSys().getType() + " " + test.getSys().getId() + " " + test.getSys().getCountry() + " " + test.getSys().getSunrise() + " " + test.getSys().getSunset());
        Log.d(TAG, String.valueOf(test.getTimezone()));
        Log.d(TAG, String.valueOf(test.getId()));
        Log.d(TAG, test.getName());
        Log.d(TAG, String.valueOf(test.getCod()));


    }
}




