package com.example.guidance.gsonTemplates;

import com.example.guidance.realm.model.DataTypeUsageData;
import com.example.guidance.realm.model.User_Information;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Conor K on 10/04/2021.
 */
public class UserInformationSerializer implements JsonSerializer<User_Information> {
    @Override
    public JsonElement serialize(User_Information src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("age", src.getAge());
        jsonObject.addProperty("gender", src.getGender());
        jsonObject.addProperty("userSpecifiedGender", src.getUserSpecifiedGender());


        return jsonObject;    }
}
