package com.example.guidance.gsonTemplates;

import com.example.guidance.realm.model.AdviceUsageData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Conor K on 11/04/2021.
 */
public class AdviceUsageDataSerializer implements JsonSerializer<AdviceUsageData> {


    @Override
    public JsonElement serialize(AdviceUsageData src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dateTimeAdviceGiven", String.valueOf(src.getDateTimeAdviceGiven()));
        jsonObject.addProperty("adviceType", String.valueOf(src.getAdviceType()));
        jsonObject.addProperty("dateTimeAdviceFor", String.valueOf(src.getDateTimeAdviceFor()));
        jsonObject.addProperty("adviceTaken", String.valueOf(src.getAdviceTaken()));


        return jsonObject;
    }
}
