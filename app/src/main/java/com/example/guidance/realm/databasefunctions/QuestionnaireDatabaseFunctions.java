package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.Question;
import com.example.guidance.realm.model.Questionnaire;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

/**
 * Created by Conor K on 20/03/2021.
 */
public class QuestionnaireDatabaseFunctions {

    private static final String TAG = "QuestionnaireDatabaseFunctions";


    public static void insertQuestionnaire(Context context, String[] questions, String[] answers, Date currentTime) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            Questionnaire init = r.createObject(Questionnaire.class, new ObjectId());
            init.setDateTime(currentTime);
            for (int i = 0; i < questions.length; i++) {
                Question question = r.createObject(Question.class, new ObjectId());
                question.setAnswer(answers[i]);
                question.setQuestion(questions[i]);
                init.getQuestion().add(question);

            }


        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : insertQuestionaire" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertQuestionaire transaction failed: ", error);

            }
        });
        realm.close();

    }

    public static boolean isQuestionaireAnswered(Context context) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        Questionnaire query = realm.where(Questionnaire.class).findFirst();
        Log.d(TAG, "isQuestionaireAnswered: query " + query);
        return query != null;

    }

    public static Questionnaire getQuestionnaire(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Questionnaire> tasksQuery = realm.where(Questionnaire.class);
//        realm.close();

        return tasksQuery.findFirst();
    }

}
