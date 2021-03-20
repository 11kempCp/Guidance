package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.Ambient_Temperature;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Conor K on 20/03/2021.
 */
public class AmbientTempDatabaseFunctions {

    private static final String TAG = "AmbientTempDatabaseFunctions";


    public static void insertAmbientTemp(Context context, float sensorValue, Date currentTime) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            Ambient_Temperature init = r.createObject(Ambient_Temperature.class, new ObjectId());
            init.setAmbientTemp(sensorValue);
            init.setDateTime(currentTime);
//            Log.d(TAG, "executed transaction : saveAmbientTempToDatabase " + currentTime);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "AmbientTemp onSuccess:");
                Log.d(TAG, "executed transaction : saveAmbientTempToDatabase" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "saveAmbientTempToDatabase transaction failed: ", error);

            }
        });
        realm.close();

    }
}
