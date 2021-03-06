package com.example.guidance.model.currentWeather;

/**
 * Created by Conor K on 04/03/2021.
 */
public class sys {


    int type;
    int id;
    String country;
    int sunrise;
    int sunset;


    public sys(int type, int id, String country, int sunrise, int sunset) {
        this.type = type;
        this.id = id;
        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }
}
