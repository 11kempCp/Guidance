package com.example.guidance.realm;

import com.example.guidance.model.currentWeather.WeatherCity;
import com.example.guidance.model.onecallWeather.onecallWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Conor K on 04/03/2021.
 */
public interface WeatherInterface {

    @GET("onecall?")
    Call<onecallWeather> getDailyWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("APPID") String key);

    @GET("onecall?")
    Call<onecallWeather> getDailyWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("exclude") String exclude, @Query("APPID") String key);

    @GET("onecall?")
    Call<onecallWeather> getDailyWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("exclude") String exclude, @Query("APPID") String key, @Query("units") String units);


    @GET("weather?")
    Call<WeatherCity> getCurrentWeather(@Query("q") String city, @Query("APPID") String key);
}
