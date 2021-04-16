package com.example.guidance.gsonTemplates;

import com.example.guidance.realm.model.DataTypeUsageData;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Conor K on 11/04/2021.
 */
public class IntelligentAgentSerializer implements JsonSerializer<Intelligent_Agent> {
    @Override
    public JsonElement serialize(Intelligent_Agent src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Date_Initialised", String.valueOf(src.getDate_Initialised()));
        jsonObject.addProperty("End_Date", String.valueOf(src.getEnd_Date()));
        jsonObject.addProperty("Analysis", src.getAnalysis());
        jsonObject.addProperty("Advice", src.getAdvice());
        jsonObject.addProperty("Gender", src.getGender());
        jsonObject.addProperty("Interaction", src.getInteraction());
        jsonObject.addProperty("Output", src.getOutput());

        return jsonObject;    }
}
