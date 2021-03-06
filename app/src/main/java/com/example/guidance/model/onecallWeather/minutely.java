package com.example.guidance.model.onecallWeather;

/**
 * Created by Conor K on 05/03/2021.
 */
public class minutely {

    int dt;
    int precipitation;


    public minutely(int dt, int precipitation) {
        this.dt = dt;
        this.precipitation = precipitation;
    }

    public int getDt() {
        return dt;
    }

    public int getPrecipitation() {
        return precipitation;
    }
}
