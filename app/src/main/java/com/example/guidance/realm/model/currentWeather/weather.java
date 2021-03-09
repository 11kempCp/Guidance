package com.example.guidance.realm.model.currentWeather;

/**
 * Created by Conor K on 04/03/2021.
 */
public class weather {

    int id;
    String main;
    String description;
    String icon;

    public weather(int id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
