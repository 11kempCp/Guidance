package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 24/03/2021.
 */
public class RankingUsageData extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private Integer steps;
    private Integer location;
    private Integer screentime;
    private Integer socialness;
    private Integer mood;
    private Integer idealStepCount;
    private Integer screentimeLimit;

    public RankingUsageData() {
    }

    public RankingUsageData(ObjectId _id, Integer steps, Integer location, Integer screentime, Integer socialness, Integer mood, Integer idealStepCount, Integer screentimeLimit) {
        this._id = _id;
        this.steps = steps;
        this.location = location;
        this.screentime = screentime;
        this.socialness = socialness;
        this.mood = mood;
        this.idealStepCount = idealStepCount;
        this.screentimeLimit = screentimeLimit;
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

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getScreentime() {
        return screentime;
    }

    public void setScreentime(Integer screentime) {
        this.screentime = screentime;
    }

    public Integer getSocialness() {
        return socialness;
    }

    public void setSocialness(Integer socialness) {
        this.socialness = socialness;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public Integer getIdealStepCount() {
        return idealStepCount;
    }

    public void setIdealStepCount(Integer idealStepCount) {
        this.idealStepCount = idealStepCount;
    }

    public Integer getScreentimeLimit() {
        return screentimeLimit;
    }

    public void setScreentimeLimit(Integer screentimeLimit) {
        this.screentimeLimit = screentimeLimit;
    }
}
