package com.example.guidance.gsonTemplates;

import com.example.guidance.realm.model.Data_Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Conor K on 11/04/2021.
 */
public class DataTypeSerializer implements JsonSerializer<Data_Type> {
    @Override
    public JsonElement serialize(Data_Type src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("steps", String.valueOf(src.isSteps()));
//        jsonObject.addProperty("distance_traveled", String.valueOf(src.isDistance_traveled()));
        jsonObject.addProperty("location", String.valueOf(src.isLocation()));
        jsonObject.addProperty("ambient_temp", String.valueOf(src.isAmbient_temp()));
        jsonObject.addProperty("screentime", String.valueOf(src.isScreentime()));
//        jsonObject.addProperty("sleep_tracking", String.valueOf(src.isSleep_tracking()));
        jsonObject.addProperty("weather", String.valueOf(src.isWeather()));
        jsonObject.addProperty("external_temp", String.valueOf(src.isExternal_temp()));
        jsonObject.addProperty("sun", String.valueOf(src.isSun()));
        jsonObject.addProperty("socialness", String.valueOf(src.isSocialness()));
        jsonObject.addProperty("mood", String.valueOf(src.isMood()));


        return jsonObject;
    }
}

