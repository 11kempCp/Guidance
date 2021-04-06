package com.example.guidance.realm.model;

import com.example.guidance.realm.model.advicemodel.A_AppData;
import com.example.guidance.realm.model.advicemodel.A_Location;
import com.example.guidance.realm.model.advicemodel.A_Mood;
import com.example.guidance.realm.model.advicemodel.A_Socialness;
import com.example.guidance.realm.model.advicemodel.A_Step;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 31/03/2021.
 */
public class Justification extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private RealmList<A_Step> justificationStep;
    private RealmList<A_AppData> justificationScreentime;
    private RealmList<A_Location> justificationLocation;
    private RealmList<A_Socialness> justificationSocialness;
    private RealmList<A_Mood> justificationMood;


    public Justification() {
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public RealmList<A_Step> getJustificationStep() {
        return justificationStep;
    }

    public void setJustificationStep(RealmList<A_Step> justificationStep) {
        this.justificationStep = justificationStep;
    }

    public RealmList<A_AppData> getJustificationScreentime() {
        return justificationScreentime;
    }

    public void setJustificationScreentime(RealmList<A_AppData> justificationScreentime) {
        this.justificationScreentime = justificationScreentime;
    }

    public RealmList<A_Location> getJustificationLocation() {
        return justificationLocation;
    }

    public void setJustificationLocation(RealmList<A_Location> justificationLocation) {
        this.justificationLocation = justificationLocation;
    }

    public RealmList<A_Socialness> getJustificationSocialness() {
        return justificationSocialness;
    }

    public void setJustificationSocialness(RealmList<A_Socialness> justificationSocialness) {
        this.justificationSocialness = justificationSocialness;
    }

    public RealmList<A_Mood> getJustificationMood() {
        return justificationMood;
    }

    public void setJustificationMood(RealmList<A_Mood> justificationMood) {
        this.justificationMood = justificationMood;
    }
}
