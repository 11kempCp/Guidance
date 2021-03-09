package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 14/02/2021.
 */
public class Ambient_Temperature extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private float ambientTemp;

    public Ambient_Temperature(){

    }

    public Ambient_Temperature(ObjectId _id, Date dateTime, int ambientTemp) {
        this._id = _id;
        this.dateTime = dateTime;
        this.ambientTemp = ambientTemp;
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

    public float getAmbientTemp() {
        return ambientTemp;
    }

    public void setAmbientTemp(float ambientTemp) {
        this.ambientTemp = ambientTemp;
    }
}
