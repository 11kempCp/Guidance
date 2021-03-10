package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 10/03/2021.
 */
public class DataTypeUsageData extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private String data_type;
    private boolean status;

    public DataTypeUsageData() {
    }

    public DataTypeUsageData(ObjectId _id, Date dateTime, String data_type, boolean status) {
        this._id = _id;
        this.dateTime = dateTime;
        this.data_type = data_type;
        this.status = status;
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

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
