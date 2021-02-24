package com.example.guidance.model;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 22/02/2021.
 */
public class Socialness extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private float rating;


    public Socialness(){

    }

    public Socialness(ObjectId _id, Date dateTime, float rating) {
        this._id = _id;
        this.dateTime = dateTime;
        this.rating = rating;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
