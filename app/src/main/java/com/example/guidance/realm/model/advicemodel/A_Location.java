package com.example.guidance.realm.model.advicemodel;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 06/04/2021.
 */
public class A_Location extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private double latitude;
    private double longitude;


    public A_Location(){

    }

    public A_Location(ObjectId _id, Date dateTime, double latitude, double longitude) {
        this._id = _id;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }
}
