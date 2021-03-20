package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.User_Information;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Conor K on 20/03/2021.
 */
public class UserInformationDatabaseFunctions {

    private static final String TAG = "UserInformationDatabaseFunctions";


    public static boolean isUserInformationInitialised(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        User_Information query = realm.where(User_Information.class).findFirst();
        Log.d(TAG, "isUserInformationInitialised: query " + query);
        return query != null;
    }


    public static void userInformationEntry(Context context, String name, Integer age, String gender) {
        if (!isUserInformationInitialised(context)) {
            initialiseUserInformation(context, name, age, gender);
        } else {
            Log.d(TAG, "userInformationEntry: User Information already entered");
        }
    }

    public static void initialiseUserInformation(Context context, String name, Integer age, String gender) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            User_Information init = r.createObject(User_Information.class, new ObjectId());


            init.setName(name);
            init.setAge(age);
            init.setGender(gender);


        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : initialiseUserInformation" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "initialiseUserInformation transaction failed: ", error);
            }
        });
        realm.close();
    }

}
