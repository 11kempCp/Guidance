package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 04/03/2021.
 */
public class Weather extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private String weather;
    private Date sunRise;
    private Date sunSet;
    private double feels_like_morn;
    private double feels_like_day;
    private double feels_like_eve;
    private double feels_like_night;
    private double temp_min;
    private double temp_max;

    public Weather(){

    }

    public Weather(ObjectId _id, Date dateTime, String weather, Date sunRise, Date sunSet, double feels_like_morn, double feels_like_day, double feels_like_eve, double feels_like_night, double temp_min, double temp_max) {
        this._id = _id;
        this.dateTime = dateTime;
        this.weather = weather;
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.feels_like_morn = feels_like_morn;
        this.feels_like_day = feels_like_day;
        this.feels_like_eve = feels_like_eve;
        this.feels_like_night = feels_like_night;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
    }


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Date getSunRise() {
        return sunRise;
    }

    public void setSunRise(Date sunRise) {
        this.sunRise = sunRise;
    }

    public Date getSunSet() {
        return sunSet;
    }

    public void setSunSet(Date sunSet) {
        this.sunSet = sunSet;
    }

    public double getFeels_like_morn() {
        return feels_like_morn;
    }

    public void setFeels_like_morn(double feels_like_morn) {
        this.feels_like_morn = feels_like_morn;
    }

    public double getFeels_like_day() {
        return feels_like_day;
    }

    public void setFeels_like_day(double feels_like_day) {
        this.feels_like_day = feels_like_day;
    }

    public double getFeels_like_eve() {
        return feels_like_eve;
    }

    public void setFeels_like_eve(double feels_like_eve) {
        this.feels_like_eve = feels_like_eve;
    }

    public double getFeels_like_night() {
        return feels_like_night;
    }

    public void setFeels_like_night(double feels_like_night) {
        this.feels_like_night = feels_like_night;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }
}




