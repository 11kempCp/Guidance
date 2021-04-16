package com.example.guidance.gsonTemplates;

import com.example.guidance.realm.model.DataTypeUsageData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Conor K on 10/04/2021.
 */
public class DataTypeUsageDataSerializer implements JsonSerializer<DataTypeUsageData> {


    @Override
    public JsonElement serialize(DataTypeUsageData src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dateTime", String.valueOf(src.getDateTime()));
        jsonObject.addProperty("data_type", src.getData_type());
        jsonObject.addProperty("status", src.isStatus());

        return jsonObject;    }
}
