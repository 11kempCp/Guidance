package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 30/03/2021.
 */
public class AdviceUsageData extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTimeAdviceGiven;
    private String adviceType;
    private Date dateTimeAdviceFor;
    private Boolean adviceTaken;


    public AdviceUsageData() {
    }

    public AdviceUsageData(ObjectId _id, Date dateTimeAdviceGiven, String adviceType, Date dateTimeAdviceFor, Boolean adviceTaken) {
        this._id = _id;
        this.dateTimeAdviceGiven = dateTimeAdviceGiven;
        this.adviceType = adviceType;
        this.dateTimeAdviceFor = dateTimeAdviceFor;
        this.adviceTaken = adviceTaken;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Date getDateTimeAdviceGiven() {
        return dateTimeAdviceGiven;
    }

    public void setDateTimeAdviceGiven(Date dateTimeAdviceGiven) {
        this.dateTimeAdviceGiven = dateTimeAdviceGiven;
    }

    public String getAdviceType() {
        return adviceType;
    }

    public void setAdviceType(String adviceType) {
        this.adviceType = adviceType;
    }

    public Date getDateTimeAdviceFor() {
        return dateTimeAdviceFor;
    }

    public void setDateTimeAdviceFor(Date dateTimeAdviceFor) {
        this.dateTimeAdviceFor = dateTimeAdviceFor;
    }

    public Boolean getAdviceTaken() {
        return adviceTaken;
    }

    public void setAdviceTaken(Boolean adviceTaken) {
        this.adviceTaken = adviceTaken;
    }
}
