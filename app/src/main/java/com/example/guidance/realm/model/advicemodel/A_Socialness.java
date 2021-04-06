package com.example.guidance.realm.model.advicemodel;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 06/04/2021.
 */
public class A_Socialness extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private int rating;


    public A_Socialness(){

    }

    public A_Socialness(ObjectId _id, Date dateTime, int rating) {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
