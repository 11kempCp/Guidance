package com.example.guidance.model.onecallWeather;

import com.example.guidance.model.currentWeather.weather;

import java.util.ArrayList;

/**
 * Created by Conor K on 05/03/2021.
 */
public class daily {

   private final long dt;
   private final int sunrise;
   private final int sunset;
   private final temp temp;
   private final feels_like feels_like;
   private final int pressure;
   private final int humidity;
   private final double dw_point;
   private final double wind_speed;
   private final int wind_deg;
   private final ArrayList<weather> weather;
   private final int clouds;
   private final double pop;
   private final double uvi;

    public daily(int dt, int sunrise, int sunset, temp temp, feels_like feels_like, int pressure, int humidity, double dw_point, double wind_speed, int wind_deg, ArrayList<weather> weather, int clouds, double pop, double uvi) {
        this.dt = dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.feels_like = feels_like;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dw_point = dw_point;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.weather = weather;
        this.clouds = clouds;
        this.pop = pop;
        this.uvi = uvi;
    }

    public long getDt() {
        return dt;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public temp getTemp() {
        return temp;
    }

    public feels_like getFeels_like() {
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

    public double getWind_speed() {
        return wind_speed;
    }

    public int getWind_deg() {
        return wind_deg;
    }

    public ArrayList<weather> getWeather() {
        return weather;
    }

    public int getClouds() {
        return clouds;
    }

    public double getPop() {
        return pop;
    }

    public double getUvi() {
        return uvi;
    }
}
