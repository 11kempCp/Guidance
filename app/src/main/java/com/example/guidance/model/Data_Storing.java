package com.example.guidance.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 10/02/2021.
 */
//todo rename class
    // Realm schema version 2
public class Data_Storing extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private boolean steps;
    private boolean distance_traveled;
    private boolean location;
    private boolean device_temp;
    private boolean screentime;
    private boolean sleep_tracking;
    private boolean weather;
    private boolean external_temp;
    private boolean sun;
    private boolean socialness;
    private boolean mood;

    public Data_Storing(){

    }

    public Data_Storing(ObjectId _id, boolean steps, boolean distance_traveled, boolean location, boolean device_temp, boolean screentime, boolean sleep_tracking, boolean weather, boolean external_temp, boolean sun, boolean socialness, boolean mood) {
        this._id = _id;
        this.steps = steps;
        this.distance_traveled = distance_traveled;
        this.location = location;
        this.device_temp = device_temp;
        this.screentime = screentime;
        this.sleep_tracking = sleep_tracking;
        this.weather = weather;
        this.external_temp = external_temp;
        this.sun = sun;
        this.socialness = socialness;
        this.mood = mood;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public boolean isSteps() {
        return steps;
    }

    public void setSteps(boolean steps) {
        this.steps = steps;
    }

    public boolean isDistance_traveled() {
        return distance_traveled;
    }

    public void setDistance_traveled(boolean distance_traveled) {
        this.distance_traveled = distance_traveled;
    }

    public boolean isLocation() {
        return location;
    }

    public void setLocation(boolean location) {
        this.location = location;
    }

    public boolean isDevice_temp() {
        return device_temp;
    }

    public void setDevice_temp(boolean device_temp) {
        this.device_temp = device_temp;
    }

    public boolean isScreentime() {
        return screentime;
    }

    public void setScreentime(boolean screentime) {
        this.screentime = screentime;
    }

    public boolean isSleep_tracking() {
        return sleep_tracking;
    }

    public void setSleep_tracking(boolean sleep_tracking) {
        this.sleep_tracking = sleep_tracking;
    }

    public boolean isWeather() {
        return weather;
    }

    public void setWeather(boolean weather) {
        this.weather = weather;
    }

    public boolean isExternal_temp() {
        return external_temp;
    }

    public void setExternal_temp(boolean external_temp) {
        this.external_temp = external_temp;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public boolean isSocialness() {
        return socialness;
    }

    public void setSocialness(boolean socialness) {
        this.socialness = socialness;
    }

    public boolean isMood() {
        return mood;
    }

    public void setMood(boolean mood) {
        this.mood = mood;
    }



}
