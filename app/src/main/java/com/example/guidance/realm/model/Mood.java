package com.example.guidance.realm.model;

import com.example.guidance.realm.model.advicemodel.A_Mood;
import com.example.guidance.realm.model.advicemodel.A_Socialness;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 23/02/2021.
 */
public class Mood extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private int rating;


    public Mood(){

    }

    public Mood(ObjectId _id, Date dateTime, int rating) {
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

    public A_Mood convertToAdviceFormat(){

        A_Mood newMoodObject = new A_Mood();
        newMoodObject.set_id(new ObjectId());
        newMoodObject.setDateTime(dateTime);
        newMoodObject.setRating(rating);

        return newMoodObject;
    }
}
