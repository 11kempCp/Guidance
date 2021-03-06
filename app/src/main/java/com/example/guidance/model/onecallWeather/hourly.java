package com.example.guidance.model.onecallWeather;

import com.example.guidance.model.currentWeather.weather;

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
    ArrayList<com.example.guidance.model.currentWeather.weather> weather;
    double pop;

}
