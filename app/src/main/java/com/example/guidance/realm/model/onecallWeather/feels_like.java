package com.example.guidance.realm.model.onecallWeather;

/**
 * Created by Conor K on 05/03/2021.
 */
public class feels_like {


  private final  double day;
  private final  double night;
  private final  double eve;
  private final  double morn;

    public feels_like(double day, double night, double eve, double morn) {
        this.day = day;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    public double getDay() {
        return day;
    }

    public double getNight() {
        return night;
    }

    public double getEve() {
        return eve;
    }

    public double getMorn() {
        return morn;
    }
}
