package com.example.guidance.realm;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.example.guidance.model.Ambient_Temperature;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static androidx.core.content.ContextCompat.getSystemService;

/**
 * Created by Conor K on 17/02/2021.
 */
public class DatabaseFunctions {


    private static final String TAG = "DatabaseFunctions";

    public static void saveAmbientTempToDatabase(Context context, float sensorValue, Date currentTime) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            Ambient_Temperature init = r.createObject(Ambient_Temperature.class, new ObjectId());
            init.setAmbientTemp(sensorValue);
            init.setDateTime(currentTime);
            Log.d(TAG, "executed transaction : saveAmbientTempToDatabase " + currentTime);
        });
        realm.close();

    }

    public static void saveStepsCounterToDatabase(Context context, float sensorValue, Date currentTime) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            Ambient_Temperature init = r.createObject(Ambient_Temperature.class, new ObjectId());
            init.setAmbientTemp(sensorValue);
            init.setDateTime(currentTime);
            Log.d(TAG, "executed transaction : saveStepsCounterToDatabase" + currentTime);
        });
        realm.close();

    }


}
