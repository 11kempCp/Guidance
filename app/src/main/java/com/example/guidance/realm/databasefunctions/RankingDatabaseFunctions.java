package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Ranking;
import com.example.guidance.realm.model.RankingUsageData;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;

/**
 * Created by Conor K on 24/03/2021.
 */
public class RankingDatabaseFunctions {

    private static final String TAG = "RankingDatabaseFunctions";

    public static final String step = "Step";
    public static final String screentime = "Screentime";
    public static final String location = "Location";
    public static final String socialness = "Socialness";
    public static final String mood = "Mood";

    public static Ranking getRanking(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Ranking> tasksQuery = realm.where(Ranking.class);
//        realm.close();

        return tasksQuery.findFirst();
    }

    public static ArrayList<String> getRankingList(Context context, int rankingListSize) {
        Ranking ranking = getRanking(context);
        Data_Type dataType = getDataType(context);

//        Log.d(TAG, "getRankingList:  getDataType " + dataType);
//        Log.d(TAG, "getRankingList:  getRanking " + ranking);


        LinkedHashMap<String, Integer> rankingList = new LinkedHashMap<>();


        if (dataType.isSteps()) {
            rankingList.put(step, ranking.getSteps());
        }

        if (dataType.isScreentime()) {
            rankingList.put(screentime, ranking.getScreentime());
        }
        if (dataType.isLocation()) {
            rankingList.put(location, ranking.getLocation());
        }
        if (dataType.isSocialness()) {
            rankingList.put(socialness, ranking.getSocialness());
        }
        if (dataType.isMood()) {
            rankingList.put(mood, ranking.getMood());
        }


//        HashMap<String, Integer> temp = sortbykey(rankingList);
//        Log.d(TAG, "getRankingList:  rankingListSize " + rankingListSize);
//        Log.d(TAG, "getRankingList: dataType.isSteps() " + dataType.isSteps());
//        Log.d(TAG, "getRankingList: dataType.isLocation() " + dataType.isLocation());
//        Log.d(TAG, "getRankingList: dataType.isScreentime() " + dataType.isScreentime());
//        Log.d(TAG, "getRankingList: dataType.isMood() " + dataType.isMood());
//        Log.d(TAG, "getRankingList: dataType.isSocialness() " + dataType.isSocialness());


//        ArrayList<String> result = new ArrayList<>(5);
        TreeMap<String, Integer> sorted = new TreeMap<>(rankingList);
        String[] result = new String[rankingListSize];
        int reduce = 0;

        if (dataType.isSteps()) {
            if (sorted.get(step) != null && result[sorted.get(step)] == null) {
                result[sorted.get(step)] = step;
            }

        }

        if (dataType.isSocialness()) {
            if (sorted.get(socialness) != null &&  result[sorted.get(socialness)] == null) {
                result[sorted.get(socialness)] = socialness;
            }

        }

        if (dataType.isLocation()) {
            if (sorted.get(location) != null && result[sorted.get(location)] == null) {
                result[sorted.get(location)] = location;
            }

        }

        if (dataType.isScreentime()) {
            if (sorted.get(screentime) != null &&  result[sorted.get(screentime)] == null) {
                result[sorted.get(screentime)] = screentime;
            }

        }

        if (dataType.isMood()) {
            if (sorted.get(mood) != null &&  result[sorted.get(mood)] == null) {
                result[sorted.get(mood)] = mood;
            }
        }


        /// fills out the remaining spaces

        if (dataType.isScreentime()) {
            if (sorted.get(screentime) == null) {
                result[getFirstAvailableSpace(result)] = screentime;

            }
        }

        if (dataType.isSteps()) {
            if (sorted.get(step) == null) {
                result[getFirstAvailableSpace(result)] = step;

            }
        }


        if (dataType.isSocialness()) {
            if (sorted.get(socialness) == null) {
                result[getFirstAvailableSpace(result)] = socialness;

            }
        }


        if (dataType.isLocation()) {
            if (sorted.get(location) == null) {
                result[getFirstAvailableSpace(result)] = location;

            }
        }


        if (dataType.isMood()) {
            if (sorted.get(mood) == null) {
                result[getFirstAvailableSpace(result)] = mood;

            }
        }

        ArrayList<String> removeNull = new ArrayList<>();
        for(String res: result){
            if(res!=null){
                removeNull.add(res);
            }
        }

//        Log.d(TAG, "getRankingList:  result " + Arrays.toString(result));
//        Log.d(TAG, "getRankingList:  test " + test);
        return removeNull;

    }

    public static int getFirstAvailableSpace(String[] result) {
        int i = 0;
        for (String pos : result) {
            if (pos == null) {
//                Log.d(TAG, "getFirstAvailableSpace: " + i);
                return i;
            }
            i++;
        }
        return -1;
    }

    public static void initialiseRanking(Context context, Integer steps, Integer location, Integer screentime, Integer socialness, Integer mood, Integer idealStepCount, Integer screentimeLimit) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {
            // Instantiate the class using the factory function.
            Ranking init = r.createObject(Ranking.class, new ObjectId());
            // Configure the instance.
            init.setSteps(steps);
            init.setLocation(location);
            init.setScreentime(screentime);
            init.setSocialness(socialness);
            init.setMood(mood);
            init.setIdealStepCount(idealStepCount);
            init.setScreentimeLimit(screentimeLimit);
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

    public static boolean isRankingInitialised(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        Ranking tasksQuery = realm.where(Ranking.class).findFirst();

//        realm.close();

        boolean t = tasksQuery != null;
        Log.d(TAG, "isRankingInitialised: " + t);
        return tasksQuery != null;
    }

    public static void insertRankingUsageData(Context context, Date currentTime, Integer steps, Integer location, Integer screentime, Integer socialness, Integer mood, Integer idealStepCount, Integer screentimeLimit) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            RankingUsageData init = r.createObject(RankingUsageData.class, new ObjectId());
            init.setDateTime(currentTime);
            init.setSteps(steps);
            init.setLocation(location);
            init.setScreentime(screentime);
            init.setSocialness(socialness);
            init.setMood(mood);
            init.setIdealStepCount(idealStepCount);
            init.setScreentimeLimit(screentimeLimit);

        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "AmbientTemp onSuccess:");
                Date currentTime = Calendar.getInstance().getTime();
//                Log.d(TAG, "executed transaction : insertRankingUsageData " + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertRankingUsageData transaction failed: ", error);

            }
        });
        realm.close();
    }

    public static void updateRanking(Context context, Integer steps, Integer location, Integer screentime, Integer socialness, Integer mood, Integer idealStepCount, Integer screentimeLimit) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(r -> {

            Ranking result = r.where(Ranking.class).findFirst();

            if (result == null) {
                Log.d(TAG, "intelligentAgentSetGender: ERROR");
            } else {

                result.setSteps(steps);
                result.setLocation(location);
                result.setScreentime(screentime);
                result.setSocialness(socialness);
                result.setMood(mood);
                result.setIdealStepCount(idealStepCount);
                result.setScreentimeLimit(screentimeLimit);


                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "updateSteps onSuccess:");
                Date ct = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : updateRanking " + ct);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateRanking transaction failed: ", error);

            }
        });


        realm.close();

    }


}
