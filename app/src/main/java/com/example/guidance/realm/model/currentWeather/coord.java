package com.example.guidance.realm.model.currentWeather;

public class coord {
      double lon;
      double lat;

    public coord(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public  double getLon() {
        return lon;
    }

    public  double getLat() {
        return lat;
    }
}
