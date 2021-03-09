package com.example.guidance.realm.model.onecallWeather;

import com.example.guidance.realm.model.currentWeather.weather;

import java.util.ArrayList;

/**
 * Created by Conor K on 05/03/2021.
 */
public class current {

    int dt;
    int sunrise;
    int sunset;
    double temp;
    double feels_like;
    int pressure;
    int humidity;
    double dw_point;
    double uvi;
    int clouds;
    int visibility;
    double wind_speed;
    int wind_deg;
    ArrayList<weather> weather;

    public current(int dt, int sunrise, int sunset, double temp, double feels_like, int pressure, int humidity, double dw_point, int uvi, int clouds, int visibility, double wind_speed, int wind_deg, ArrayList<weather> weather) {
        this.dt = dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.feels_like = feels_like;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dw_point = dw_point;
        this.uvi = uvi;
        this.clouds = clouds;
        this.visibility = visibility;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.weather = weather;
    }

    public int getDt() {
        return dt;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public double getTemp() {
        return temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getDw_point() {
        return dw_point;
    }

    public double getUvi() {
        return uvi;
    }

    public int getClouds() {
        return clouds;
    }

    public int getVisibility() {
        return visibility;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public int getWind_deg() {
        return wind_deg;
    }

    public ArrayList<weather> getWeather() {
        return weather;
    }
}
