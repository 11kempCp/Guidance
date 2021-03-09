package com.example.guidance.realm.model.onecallWeather;

import java.util.ArrayList;

/**
 * Created by Conor K on 05/03/2021.
 */
public class hourly {

    int dt;
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
    ArrayList<com.example.guidance.realm.model.currentWeather.weather> weather;
    double pop;

}
