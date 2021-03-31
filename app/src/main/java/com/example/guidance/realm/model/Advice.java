package com.example.guidance.realm.model;

import com.example.guidance.Util.AdviceJustification;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 30/03/2021.
 */
public class Advice extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTimeAdviceGiven;
    private String adviceType;
    private Date dateTimeAdviceFor;
    private Float steps;
    private AppData screentime;
    private Float socialness;
    private Float mood;
    private AdviceUsageData adviceUsageData;
    private Justification justification;

    public Advice() {
    }

    public Advice(ObjectId _id, Date dateTimeAdviceGiven, String adviceType, Date dateTimeAdviceFor, Float steps, AppData screentime, Float socialness, Float mood, AdviceUsageData adviceUsageData, Justification justification) {
        this._id = _id;
        this.dateTimeAdviceGiven = dateTimeAdviceGiven;
        this.adviceType = adviceType;
        this.dateTimeAdviceFor = dateTimeAdviceFor;
        this.steps = steps;
        this.screentime = screentime;
        this.socialness = socialness;
        this.mood = mood;
        this.adviceUsageData = adviceUsageData;
        this.justification = justification;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Date getDateTimeAdviceGiven() {
        return dateTimeAdviceGiven;
    }

    public void setDateTimeAdviceGiven(Date dateTimeAdviceGiven) {
        this.dateTimeAdviceGiven = dateTimeAdviceGiven;
    }

    public String getAdviceType() {
        return adviceType;
    }

    public void setAdviceType(String adviceType) {
        this.adviceType = adviceType;
    }

    public Date getDateTimeAdviceFor() {
        return dateTimeAdviceFor;
    }

    public void setDateTimeAdviceFor(Date dateTimeAdviceFor) {
        this.dateTimeAdviceFor = dateTimeAdviceFor;
    }

    public Float getSteps() {
        return steps;
    }

    public void setSteps(Float steps) {
        this.steps = steps;
    }

    public AppData getScreentime() {
        return screentime;
    }

    public void setScreentime(AppData screentime) {
        this.screentime = screentime;
    }

    public Float getSocialness() {
        return socialness;
    }

    public void setSocialness(Float socialness) {
        this.socialness = socialness;
    }

    public Float getMood() {
        return mood;
    }

    public void setMood(Float mood) {
        this.mood = mood;
    }

    public AdviceUsageData getAdviceUsageData() {
        return adviceUsageData;
    }

    public void setAdviceUsageData(AdviceUsageData adviceUsageData) {
        this.adviceUsageData = adviceUsageData;
    }

    public Justification getJustification() {
        return justification;
    }

    public void setJustification(Justification justification) {
        this.justification = justification;
    }
}
