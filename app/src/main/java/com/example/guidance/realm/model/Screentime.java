package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 15/03/2021.
 */
public class Screentime extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private int interval_type;
    private RealmList<AppData> appData;

    public Screentime(){}


    public Screentime(ObjectId _id, Date dateTime, int interval_type, RealmList<AppData> appData) {
        this._id = _id;
        this.dateTime = dateTime;
        this.interval_type = interval_type;
        this.appData = appData;
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

    public int getInterval_type() {
        return interval_type;
    }

    public void setInterval_type(int interval_type) {
        this.interval_type = interval_type;
    }

    public RealmList<AppData> getAppData() {
        return appData;
    }

    public void setAppData(RealmList<AppData> appData) {
        this.appData = appData;
    }
}
