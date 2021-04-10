package com.example.guidance.realm.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Conor K on 17/03/2021.
 */
public class User_Information extends RealmObject {

    @PrimaryKey
    private ObjectId _id;
    private String name;
    private Integer age;
    private String gender;
    private String userSpecifiedGender;

    public User_Information(){}

    public User_Information(ObjectId _id, String name, Integer age, String gender, String userSpecifiedGender) {
        this._id = _id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.userSpecifiedGender = userSpecifiedGender;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserSpecifiedGender() {
        return userSpecifiedGender;
    }

    public void setUserSpecifiedGender(String userSpecifiedGender) {
        this.userSpecifiedGender = userSpecifiedGender;
    }
}
