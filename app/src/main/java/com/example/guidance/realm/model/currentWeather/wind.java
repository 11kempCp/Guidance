package com.example.guidance.realm.model.currentWeather;

/**
 * Created by Conor K on 04/03/2021.
 */
public class wind {

    double speed;
    int deg;

    public wind(double speed, int deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }
}
