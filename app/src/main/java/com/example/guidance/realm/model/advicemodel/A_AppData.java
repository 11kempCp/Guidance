package com.example.guidance.realm.model.advicemodel;

import com.example.guidance.realm.model.AppData;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 06/04/2021.
 */
public class A_AppData extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private String packageName;
    private Long totalTimeInForeground;
    private Long totalTimeVisible;
    private Long totalTimeForegroundServiceUsed;

    public A_AppData(){
    }

    public A_AppData(ObjectId _id, String packageName, Long totalTimeInForeground, Long totalTimeVisible, Long totalTimeForegroundServiceUsed) {
        this._id = _id;
        this.packageName = packageName;
        this.totalTimeInForeground = totalTimeInForeground;
        this.totalTimeVisible = totalTimeVisible;
        this.totalTimeForegroundServiceUsed = totalTimeForegroundServiceUsed;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Long getTotalTimeInForeground() {
        return totalTimeInForeground;
    }

    public void setTotalTimeInForeground(Long totalTimeInForeground) {
        this.totalTimeInForeground = totalTimeInForeground;
    }

    public Long getTotalTimeVisible() {
        return totalTimeVisible;
    }

    public void setTotalTimeVisible(Long totalTimeVisible) {
        this.totalTimeVisible = totalTimeVisible;
    }

    public Long getTotalTimeForegroundServiceUsed() {
        return totalTimeForegroundServiceUsed;
    }

    public void setTotalTimeForegroundServiceUsed(Long totalTimeForegroundServiceUsed) {
        this.totalTimeForegroundServiceUsed = totalTimeForegroundServiceUsed;
    }
}
