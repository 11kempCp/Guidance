package com.example.guidance.gsonTemplates;

import com.example.guidance.realm.model.RankingUsageData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Conor K on 11/04/2021.
 */
public class RankingUsageDataSerializer implements JsonSerializer<RankingUsageData> {
    @Override
    public JsonElement serialize(RankingUsageData src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dateTime", String.valueOf(src.getDateTime()));
        jsonObject.addProperty("steps", src.getSteps());
        jsonObject.addProperty("location", src.getLocation());
        jsonObject.addProperty("screentime", src.getScreentime());
        jsonObject.addProperty("socialness", src.getSocialness());
        jsonObject.addProperty("mood", src.getMood());
        jsonObject.addProperty("idealStepCount", src.getIdealStepCount());
        jsonObject.addProperty("screentimeLimit", src.getScreentimeLimit());


        return jsonObject;
    }
}
