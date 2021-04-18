package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.guidance.realm.model.DataTypeUsageData;
import com.example.guidance.realm.model.Data_Type;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Conor K on 20/03/2021.
 */
public class DataTypeDatabaseFunctions {

    private static final String TAG = "DataTypeDatabaseFunctions";


    public static Data_Type getDataType(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Data_Type> tasksQuery = realm.where(Data_Type.class);
//        realm.close();

        return tasksQuery.findFirst();
    }

    public static void initialiseDataType(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            // Instantiate the class using the factory function.
            Data_Type init = r.createObject(Data_Type.class, new ObjectId());
            // Configure the instance.
            init.setSteps(false);
            init.setDistance_traveled(false);
            init.setLocation(false);
            init.setAmbient_temp(false);
            init.setScreentime(false);
            init.setSleep_tracking(false);
            init.setWeather(false);
            init.setExternal_temp(false);
            init.setSun(false);
            init.setSocialness(false);
            init.setMood(false);
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "DataStoring onSuccess:");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "onError: ", error);
            }
        });
        realm.close();
    }

    public static boolean isDataTypeInitialised(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        Data_Type tasksQuery = realm.where(Data_Type.class).findFirst();

//        realm.close();

        boolean t = tasksQuery != null;
        Log.d(TAG, "isDataStoringInitialised: " + t);
        return tasksQuery != null;
    }

    public static void insertDataTypeUsageData(Context context, Date currentTime, String data_type, boolean status) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            DataTypeUsageData init = r.createObject(DataTypeUsageData.class, new ObjectId());
            init.setDateTime(currentTime);
            init.setData_type(data_type);
            init.setStatus(status);

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "AmbientTemp onSuccess:");
                Date currentTime = Calendar.getInstance().getTime();
//                Log.d(TAG, "executed transaction : insertDataTypeUsageData " + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertDataTypeUsageData transaction failed: ", error);

            }
        });
        realm.close();
    }

    public static RealmResults<DataTypeUsageData> getAllUsageData(Realm realm){


        RealmQuery<DataTypeUsageData> tasksQuery = realm.where(DataTypeUsageData.class);
//        realm.close();

        return tasksQuery.findAll();

    }


    public static boolean isAllDataType(Data_Type dataType){
        if(dataType==null){
            return false;
        }

        return dataType.isSteps() && dataType.isLocation() && dataType.isAmbient_temp() && dataType.isScreentime()
                && dataType.isSleep_tracking() && dataType.isSocialness() && dataType.isMood();

    }
}
