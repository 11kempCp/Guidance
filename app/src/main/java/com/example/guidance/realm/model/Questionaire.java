package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 09/03/2021.
 */
public class Questionaire extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date dateTime;
    private RealmList<Question> question;

    public Questionaire(){
    }

    public Questionaire(ObjectId _id, Date dateTime, RealmList<Question> question) {
        this._id = _id;
        this.dateTime = dateTime;
        this.question = question;
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

    public RealmList<Question> getQuestion() {
        return question;
    }

    public void setQuestion(RealmList<Question> question) {
        this.question = question;
    }
}
