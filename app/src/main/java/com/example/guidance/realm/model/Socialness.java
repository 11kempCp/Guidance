package com.example.guidance.realm.model;

import com.example.guidance.realm.model.advicemodel.A_Socialness;
import com.example.guidance.realm.model.advicemodel.A_Step;

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
    private int rating;


    public Socialness(){

    }

    public Socialness(ObjectId _id, Date dateTime, int rating) {
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


    public A_Socialness convertToAdviceFormat(){

        A_Socialness newSocialnessObject = new A_Socialness();
        newSocialnessObject.set_id(new ObjectId());
        newSocialnessObject.setDateTime(dateTime);
        newSocialnessObject.setRating(rating);

        return newSocialnessObject;
    }
}
