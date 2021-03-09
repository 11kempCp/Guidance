package com.example.guidance.realm.model.onecallWeather;

/**
 * Created by Conor K on 05/03/2021.
 */
public class temp {


   private final double day;
   private final double min;
   private final double max;
   private final double night;
   private final double eve;
   private final double morn;


    public temp(double day, double min, double max, double night, double eve, double morn) {
        this.day = day;
        this.min = min;
        this.max = max;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    public double getDay() {
        return day;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
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
