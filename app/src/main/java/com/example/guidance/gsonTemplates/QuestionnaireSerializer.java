package com.example.guidance.gsonTemplates;

import com.example.guidance.realm.model.Question;
import com.example.guidance.realm.model.Questionnaire;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Conor K on 11/04/2021.
 */
public class QuestionnaireSerializer implements JsonSerializer<Questionnaire> {
    @Override
    public JsonElement serialize(Questionnaire src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dateTime", String.valueOf(src.getDateTime()));
        jsonObject.addProperty("dateTime", String.valueOf(src.getDateTime()));
//        jsonObject.addProperty("question", (src.getQuestion()));

        int i = 0;
        for(Question question: src.getQuestion()){
            i++;
            jsonObject.addProperty("question_" + i,question.getQuestion());
            jsonObject.addProperty("answer_"+ i,question.getAnswer());

        }

        return jsonObject;
    }
}
