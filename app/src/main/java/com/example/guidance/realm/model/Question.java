package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 09/03/2021.
 */
public class Question extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private String question;
    private String answer;

    public Question(){

    }


    public Question(ObjectId _id, String question, String answer) {
        this._id = _id;
        this.question = question;
        this.answer = answer;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
