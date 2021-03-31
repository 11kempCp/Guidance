package com.example.guidance.Util;

import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Mood;
import com.example.guidance.realm.model.Socialness;
import com.example.guidance.realm.model.Step;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 28/03/2021.
 */
public class AdviceJustification {


    private Step justificationStep;
    private AppData[] justificationScreentime;
    private RealmResults<Location> justificationLocation;
    private RealmResults<Socialness> justificationSocialness;
    private RealmResults<Mood> justificationMood;


    public Step getJustificationStep() {
        return justificationStep;
    }

    public void setJustificationStep(Step justificationStep) {
        this.justificationStep = justificationStep;
    }

    public AppData[] getJustificationScreentime() {
        return justificationScreentime;
    }

    public void setJustificationScreentime(AppData[] justificationScreentime) {
        this.justificationScreentime = justificationScreentime;
    }

    public RealmResults<Location> getJustificationLocation() {
        return justificationLocation;
    }

    public void setJustificationLocation(RealmResults<Location> justificationLocation) {
        this.justificationLocation = justificationLocation;
    }

    public RealmResults<Socialness> getJustificationSocialness() {
        return justificationSocialness;
    }

    public void setJustificationSocialness(RealmResults<Socialness> justificationSocialness) {
        this.justificationSocialness = justificationSocialness;
    }

    public RealmResults<Mood> getJustificationMood() {
        return justificationMood;
    }

    public void setJustificationMood(RealmResults<Mood> justificationMood) {
        this.justificationMood = justificationMood;
    }
}
