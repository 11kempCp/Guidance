package com.example.guidance.realm.databasefunctions;

import android.content.Context;
import android.util.Log;

import com.example.guidance.realm.model.Advice;
import com.example.guidance.realm.model.AdviceUsageData;
import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.Justification;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Mood;
import com.example.guidance.realm.model.Socialness;
import com.example.guidance.realm.model.Step;
import com.example.guidance.realm.model.advicemodel.A_AppData;
import com.example.guidance.realm.model.advicemodel.A_Location;
import com.example.guidance.realm.model.advicemodel.A_Mood;
import com.example.guidance.realm.model.advicemodel.A_Socialness;
import com.example.guidance.realm.model.advicemodel.A_Step;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.guidance.Util.Util.changeDayEndOfDay;
import static com.example.guidance.Util.Util.changeDayStartOfDay;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.noAdvice;

/**
 * Created by Conor K on 30/03/2021.
 */
public class AdviceDatabaseFunctions {

    private static final String TAG = "AdviceDatabaseFunctions";

    public static RealmResults<Advice> getAllAdvice( Realm realm, Date currentTime) {


        Date yesterday = changeDayEndOfDay(currentTime, -1);
        Date tomorrow = changeDayStartOfDay(currentTime, 1);

        Log.d(TAG, "getAllAdvice: tomorrows " + tomorrow + " yesterday " + yesterday);

        RealmQuery<Advice> query = realm.where(Advice.class);
        RealmResults<Advice> result = query.sort("dateTimeAdviceFor", Sort.DESCENDING).findAll();

        realm.close();


        return result;

    }

    public static RealmResults<Advice> getAllValidAdviceNotToday(Context context, Realm realm, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();

        Date yesterday = changeDayEndOfDay(currentTime, -1);
        Date tomorrow = changeDayStartOfDay(currentTime, 1);




        //todo filter out values for the current date
        RealmQuery<Advice> query = realm.where(Advice.class).notEqualTo("adviceType", noAdvice);
        RealmResults<Advice> result = query.sort("dateTimeAdviceFor", Sort.DESCENDING).lessThanOrEqualTo("dateTimeAdviceFor", yesterday).or().greaterThanOrEqualTo("dateTimeAdviceFor", tomorrow).findAll();

        realm.close();


        return result;

    }

    public static RealmResults<Advice> getAdviceOnDate(Context context, Realm realm, Date currentTime) {
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//        Realm realm = Realm.getDefaultInstance();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentTime);
        cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE), 0, 0, 0);
        Date beginningOfDay = cal1.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentTime);
        cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DATE), 23, 59, 59);
        Date endOfDay = cal2.getTime();


//        RealmQuery<Step> query = realm.where(Step.class).lessThan("dateTime", currentTime);
        RealmQuery<Advice> query = realm.where(Advice.class).between("dateTimeAdviceFor", beginningOfDay, endOfDay).notEqualTo("adviceType", noAdvice);
        RealmResults<Advice> results = query.sort("dateTimeAdviceFor", Sort.DESCENDING).findAll();

        Log.d(TAG, "getAdviceOnDate: results " + results.size() + " " + results);
        return results;
    }

    public static RealmResults<Advice> getAdviceForAfterDate(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        RealmQuery<Advice> query = realm.where(Advice.class).greaterThan("dateTimeAdviceFor", currentTime);
        return query.sort("dateTimeAdviceGiven", Sort.DESCENDING).findAll();

    }

    public static void insertAdvice(Context context, Date dateTimeAdviceGiven, String adviceType
            , Date dateTimeAdviceFor, String advice, Float stepsCount, AppData screentime,
                                    Float socialness, Float mood, List<Step> stepRealmList, AppData[] appDataRealmList,
                                    List<Location> locationRealmList, List<Socialness> socialnessRealmList,
                                    List<Mood> moodRealmList) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        List<A_Step> stepRealmList2 = new ArrayList<>();
        List<A_AppData> appDataRealmList2 = new ArrayList<>();
        List<A_Location> locationRealmList2 = new ArrayList<>();
        List<A_Socialness> socialnessRealmList2 = new ArrayList<>();
        List<A_Mood> moodRealmList2 = new ArrayList<>();


        if (stepRealmList != null) {
//                just.setJustificationStep(step);

            for (Step st : stepRealmList) {
//                    just.getJustificationScreentime().add(a);
                if (st != null) {

//                        A_Step ff = st.convertToAdviceFormat();
                    stepRealmList2.add(st.convertToAdviceFormat());

                }
            }
        }

        if (appDataRealmList != null) {
            for (AppData a : appDataRealmList) {
//                    just.getJustificationScreentime().add(a);

                if (a != null) {
                    appDataRealmList2.add(a.convertToAdviceFormat());
                }
            }

        }

        if (locationRealmList != null) {

            for (Location l : locationRealmList) {
                if (l != null) {
                    locationRealmList2.add(l.convertToAdviceFormat());
                }
            }
        }

        if (socialnessRealmList != null) {

            for (Socialness s : socialnessRealmList) {
                if (s != null) {
                    socialnessRealmList2.add(s.convertToAdviceFormat());
                }

            }
        }

        if (moodRealmList != null) {

            for (Mood m : moodRealmList) {
                if (m != null) {
                    moodRealmList2.add(m.convertToAdviceFormat());
                }

            }
        }

        realm.executeTransactionAsync(r -> {
            Advice init = r.createObject(Advice.class, new ObjectId());
            init.setDateTimeAdviceGiven(dateTimeAdviceGiven);
            init.setAdviceType(adviceType);


            init.setDateTimeAdviceFor(dateTimeAdviceFor);
            init.setAdvice(advice);
//            init.setSteps(stepsCount);
//            init.setScreentime(screentime);
//            init.setSocialness(socialness);
//            init.setMood(mood);

            AdviceUsageData adviceUsageData = r.createObject(AdviceUsageData.class, new ObjectId());
            adviceUsageData.setDateTimeAdviceFor(dateTimeAdviceGiven);
            adviceUsageData.setAdviceType(adviceType);
            adviceUsageData.setDateTimeAdviceGiven(dateTimeAdviceFor);
            adviceUsageData.setAdviceTaken(null);
            init.setAdviceUsageData(adviceUsageData);

            Justification just = r.createObject(Justification.class, new ObjectId());
//            Justification just = new Justification();
            init.setJustification(just);

            if (stepRealmList2 != null) {
//                just.setJustificationStep(step);

                for (A_Step st : stepRealmList2) {
//                    just.getJustificationScreentime().add(a);
                    if (st != null) {

//                        A_Step ff = st.convertToAdviceFormat();
                        init.getJustification().getJustificationStep().add(st);

                    }
                }
            }

            if (appDataRealmList2 != null) {
                for (A_AppData a : appDataRealmList2) {
//                    just.getJustificationScreentime().add(a);

                    if (a != null) {
                        init.getJustification().getJustificationScreentime().add(a);
                    }
                }

            }

            if (locationRealmList2 != null) {

                for (A_Location l : locationRealmList2) {
                    if (l != null) {
                        init.getJustification().getJustificationLocation().add(l);
                    }
                }
            }

            if (socialnessRealmList2 != null) {

                for (A_Socialness s : socialnessRealmList2) {
                    if (s != null) {
                        init.getJustification().getJustificationSocialness().add(s);
                    }

                }
            }

            if (moodRealmList2 != null) {

                for (A_Mood m : moodRealmList2) {
                    if (m != null) {
                        init.getJustification().getJustificationMood().add(m);
                    }

                }
            }


        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : insertAdvice" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertAdvice transaction failed: ", error);

            }
        });
        realm.close();

    }


    public static void insertAdviceUsageData(Context context, Date dateTimeAdviceGiven, String adviceType
            , Date dateTimeAdviceFor, boolean adviceTaken) {

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            AdviceUsageData init = r.createObject(AdviceUsageData.class, new ObjectId());
            init.setDateTimeAdviceGiven(dateTimeAdviceGiven);
            init.setAdviceType(adviceType);
            init.setDateTimeAdviceFor(dateTimeAdviceFor);
            init.setAdviceTaken(adviceTaken);


        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : insertAdvice" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "insertAdvice transaction failed: ", error);

            }
        });
        realm.close();

    }

    public static void updateAdviceUsageData(Context context, Boolean adviceTaken, ObjectId objectId) {

        Log.d(TAG, "updateAdviceUsageData: ");

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();


        realm.executeTransactionAsync(r -> {
            RealmQuery<AdviceUsageData> query = r.where(AdviceUsageData.class).equalTo("_id", objectId);
            AdviceUsageData result = query.sort("dateTime", Sort.DESCENDING).findFirst();

            if (result == null) {
                Log.d(TAG, "updateAdviceUsageData: ERROR");
            } else {
                Log.d(TAG, "updateAdviceUsageData: adviceTaken " + adviceTaken);
                result.setAdviceTaken(adviceTaken);
                r.insertOrUpdate(result);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Log.d(TAG, "updateSteps onSuccess:");
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(TAG, "executed transaction : updateAdviceUsageData" + currentTime);

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG, "updateAdviceUsageData transaction failed: ", error);

            }
        });
        realm.close();

    }

    public static ArrayList<Advice> getUnresolvedAdvice(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Advice> tasksQuery = realm.where(Advice.class).findAll();
//        realm.close();
        ArrayList<Advice> adviceArrayList = new ArrayList<>();

        tasksQuery.sort("dateTimeAdviceGiven", Sort.ASCENDING);
        for (Advice advice : tasksQuery) {

            if (!advice.getAdviceType().equals(noAdvice) && advice.getAdviceUsageData().getAdviceTaken() == null) {
                adviceArrayList.add(advice);
            }

        }
        return adviceArrayList;
    }

    public static int getInteractionAmountForDate(Context context, Date currentTime) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentTime);
        cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE), 0, 0, 0);
        Date beginningOfDay = cal1.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(currentTime);
        cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DATE), 23, 59, 59);
        Date endOfDay = cal2.getTime();


//        RealmQuery<Step> query = realm.where(Step.class).lessThan("dateTime", currentTime);
        RealmQuery<Advice> query = realm.where(Advice.class).between("dateTimeAdviceGiven", beginningOfDay, endOfDay);
        return query.sort("dateTimeAdviceGiven", Sort.DESCENDING).findAll().size();
    }

    public static RealmResults<AdviceUsageData> getAdviceUsageData(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        return realm.where(AdviceUsageData.class).findAll();
    }

    public static List<Mood> convertMoodToRealmList(Context context, RealmResults<Mood> justificationMood) {

        if (justificationMood == null) {
            return null;
        }

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(justificationMood);

    }

    public static List<Location> convertLocationToRealmList(Context context, RealmResults<Location> justificationLocation) {

        if (justificationLocation == null) {
            return null;
        }

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(justificationLocation);

    }

    public static List<Socialness> convertSocialnessToRealmList(Context context, RealmResults<Socialness> justificationSocialness) {

        if (justificationSocialness == null) {
            return null;
        }


        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(justificationSocialness);

    }


}
