package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 31/03/2021.
 */
public class Justification extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private Step justificationStep;
    private RealmList<AppData> justificationScreentime;
    private RealmList<Location> justificationLocation;
    private RealmList<Socialness> justificationSocialness;
    private RealmList<Mood> justificationMood;


    public Justification() {
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Step getJustificationStep() {
        return justificationStep;
    }

    public void setJustificationStep(Step justificationStep) {
        this.justificationStep = justificationStep;
    }

    public RealmList<AppData> getJustificationScreentime() {
        return justificationScreentime;
    }

    public void setJustificationScreentime(RealmList<AppData> justificationScreentime) {
        this.justificationScreentime = justificationScreentime;
    }

    public RealmList<Location> getJustificationLocation() {
        return justificationLocation;
    }

    public void setJustificationLocation(RealmList<Location> justificationLocation) {
        this.justificationLocation = justificationLocation;
    }

    public RealmList<Socialness> getJustificationSocialness() {
        return justificationSocialness;
    }

    public void setJustificationSocialness(RealmList<Socialness> justificationSocialness) {
        this.justificationSocialness = justificationSocialness;
    }

    public RealmList<Mood> getJustificationMood() {
        return justificationMood;
    }

    public void setJustificationMood(RealmList<Mood> justificationMood) {
        this.justificationMood = justificationMood;
    }
}
