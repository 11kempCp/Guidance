package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 15/03/2021.
 */
public class AppData extends RealmObject {


    @PrimaryKey
    private ObjectId _id;
    private String packageName;
    private long totalTimeInForeground;
    private long totalTimeVisible;
    private long totalTimeForegroundServiceUsed;

    public AppData(){
    }

    public AppData(ObjectId _id, String packageName, long totalTimeInForeground, long totalTimeVisible, long totalTimeForegroundServiceUsed) {
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

    public long getTotalTimeInForeground() {
        return totalTimeInForeground;
    }

    public void setTotalTimeInForeground(long totalTimeInForeground) {
        this.totalTimeInForeground = totalTimeInForeground;
    }

    public long getTotalTimeVisible() {
        return totalTimeVisible;
    }

    public void setTotalTimeVisible(long totalTimeVisible) {
        this.totalTimeVisible = totalTimeVisible;
    }

    public long getTotalTimeForegroundServiceUsed() {
        return totalTimeForegroundServiceUsed;
    }

    public void setTotalTimeForegroundServiceUsed(long totalTimeForegroundServiceUsed) {
        this.totalTimeForegroundServiceUsed = totalTimeForegroundServiceUsed;
    }
}
