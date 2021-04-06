package com.example.guidance.realm.model.advicemodel;

import com.example.guidance.realm.model.Step;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 06/04/2021.
 */
public class A_Step extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private float stepCount;

    public A_Step(){

    }

    public A_Step(ObjectId _id, Date dateTime, float stepCount) {
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

    public Step newStepObject(Step step){

        Step newStepObject = new Step();
        newStepObject.set_id(new ObjectId());
        newStepObject.setDateTime(step.getDateTime());
        newStepObject.setStepCount(step.getStepCount());

        return newStepObject;
    }
}
