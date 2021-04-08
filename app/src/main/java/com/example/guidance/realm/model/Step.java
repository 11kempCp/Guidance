package com.example.guidance.realm.model;

import com.example.guidance.realm.model.advicemodel.A_Step;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 19/02/2021.
 */
public class Step extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private float stepCount;

    public Step(){

    }

    public Step(ObjectId _id, Date dateTime, float stepCount) {
        this._id = _id;
        this.dateTime = dateTime;
        this.stepCount = stepCount;
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

    public float getStepCount() {
        return stepCount;
    }

    public void setStepCount(float stepCount) {
        this.stepCount = stepCount;
    }

    public A_Step convertToAdviceFormat(){

        A_Step newStepObject = new A_Step();
        newStepObject.set_id(new ObjectId());
        newStepObject.setDateTime(dateTime);
        newStepObject.setStepCount(stepCount);

        return newStepObject;
    }
}
