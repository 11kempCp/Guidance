package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 07/03/2021.
 */
public class Intelligent_Agent extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Date Date_Initialised;
    private Date End_Date;
    private String Analysis;
    private String Advice;
    private String Gender;
    private String Interaction;
    private String Output;
    private boolean StudyStatus;
    private String AccessToken;
    private int count;


    public Intelligent_Agent(){

    }


    public Intelligent_Agent(ObjectId _id, Date date_Initialised, Date end_Date, String analysis, String advice, String gender, String interaction, String output, boolean studyStatus, String accessToken, int count) {
        this._id = _id;
        Date_Initialised = date_Initialised;
        End_Date = end_Date;
        Analysis = analysis;
        Advice = advice;
        Gender = gender;
        Interaction = interaction;
        Output = output;
        StudyStatus = studyStatus;
        AccessToken = accessToken;
        this.count = count;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Date getDate_Initialised() {
        return Date_Initialised;
    }

    public void setDate_Initialised(Date date_Initialised) {
        Date_Initialised = date_Initialised;
    }

    public Date getEnd_Date() {
        return End_Date;
    }

    public void setEnd_Date(Date end_Date) {
        End_Date = end_Date;
    }

    public String getAnalysis() {
        return Analysis;
    }

    public void setAnalysis(String analysis) {
        Analysis = analysis;
    }

    public String getAdvice() {
        return Advice;
    }

    public void setAdvice(String advice) {
        Advice = advice;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getInteraction() {
        return Interaction;
    }

    public void setInteraction(String interaction) {
        Interaction = interaction;
    }

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }

    public boolean isStudyStatus() {
        return StudyStatus;
    }

    public void setStudyStatus(boolean studyStatus) {
        StudyStatus = studyStatus;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
