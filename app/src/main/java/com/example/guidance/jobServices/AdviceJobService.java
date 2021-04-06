package com.example.guidance.jobServices;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.guidance.R;
import com.example.guidance.Util.AdviceJustification;
import com.example.guidance.activity.AdviceActivity;
import com.example.guidance.app.App;
import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Mood;
import com.example.guidance.realm.model.Ranking;
import com.example.guidance.realm.model.Screentime;
import com.example.guidance.realm.model.Socialness;
import com.example.guidance.realm.model.Step;
import com.example.guidance.realm.model.User_Information;
import com.example.guidance.realm.model.Weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.RealmResults;

import static com.example.guidance.Util.Advice.LocationAdvice.getLocationAdvice;
import static com.example.guidance.Util.Advice.MoodAdvice.getMoodAdvice;
import static com.example.guidance.Util.Advice.ScreentimeAdvice.getScreentimeAdvice;
import static com.example.guidance.Util.Advice.SocialnessAdvice.getSocialnessAdvice;
import static com.example.guidance.Util.Advice.StepAdvice.getStepAdvice;
import static com.example.guidance.Util.IA.HIGH;
import static com.example.guidance.Util.IA.LOW;
import static com.example.guidance.Util.IA.noJustification;
import static com.example.guidance.Util.IA.withJustification;
import static com.example.guidance.Util.Util.ADVICE;
import static com.example.guidance.Util.Util.changeDayBy;
import static com.example.guidance.Util.Util.isSameDate;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.convertLocationToRealmList;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.convertMoodToRealmList;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.convertSocialnessToRealmList;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.getInteractionAmountForDate;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.insertAdvice;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.LocationDatabaseFunctions.getLocationOverPreviousDays;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.getMoodOverPreviousDays;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.getRanking;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.getRankingList;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.location;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.mood;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.noAdvice;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.screentime;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.socialness;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.step;
import static com.example.guidance.realm.databasefunctions.ScreentimeDatabaseFunctions.getScreentimePreviousDay;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.getSocialnessOverPreviousDays;
import static com.example.guidance.realm.databasefunctions.StepsDatabaseFunctions.getStepPreviousDay;
import static com.example.guidance.realm.databasefunctions.UserInformationDatabaseFunctions.getUserInformation;
import static com.example.guidance.realm.databasefunctions.WeatherDatabaseFunctions.getNextAvailableClearDay;
import static com.example.guidance.realm.databasefunctions.WeatherDatabaseFunctions.getNextAvailableReasonableTempDay;

/**
 * Created by Conor K on 20/03/2021.
 */
public class AdviceJobService extends JobService {

    private static final String TAG = "AdviceJobService";

    private static int idealStepcount = 3000; //steps
    private static int idealScreentimeUsage = 60; //minutes
    private static final int idealThresholdDistance = 50; //meters
    private static final double idealMood = 2.5; //average of more than this
    private static final double idealSocialness = 2.5; //average of more than this


    private final AdviceJustification adviceJustification = new AdviceJustification();

    private static final int days = 3;


    LinkedHashMap<String, Boolean> rankingLinkedHashMap = new LinkedHashMap<>();
    Date currentTime = Calendar.getInstance().getTime();


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");


        if (currentTime.getHours() >= 12 && currentTime.getHours() <= 20) {
            Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
            Log.d(TAG, "getInteractionAmountForDate: " + getInteractionAmountForDate(this, currentTime));

            //Both if statements result in the same end result, this is due to a limitation with the
            //group size that will be available. Therefore the Intelligent Agent will
            // only provide advice once per day
            if (intelligent_agent.getInteraction().equals(HIGH)) {
                Log.d(TAG, "onStartJob: high");

                if (getInteractionAmountForDate(this, currentTime) < 1) {
                    determineAdvice(currentTime);
                }else{
                    Log.d(TAG, "Interaction Limit reached");
                }
            } else if (intelligent_agent.getInteraction().equals(LOW)) {
                Log.d(TAG, "onStartJob: low");
                if (getInteractionAmountForDate(this, currentTime) < 1) {
                    determineAdvice(currentTime);
                }else{
                    Log.d(TAG, "Interaction Limit reached");
                }
            }
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }


    public void determineAdvice(Date currentTime) {


        Ranking ranking = getRanking(this);
        Data_Type dataType = getDataType(this);

        if (ranking.getIdealStepCount() != null) {
            idealStepcount = ranking.getIdealStepCount();
        }

        if (ranking.getScreentimeLimit() != null) {
            idealScreentimeUsage = ranking.getScreentimeLimit();
        }


        ArrayList<String> rankingList = getRankingList(this, 5);

        for (String rank : rankingList) {

            if (rank.equals(step)) {
                rankingLinkedHashMap.put(step, false);
            }

            if (rank.equals(screentime)) {
                rankingLinkedHashMap.put(screentime, false);
            }

            if (rank.equals(location)) {
                rankingLinkedHashMap.put(location, false);
            }

            if (rank.equals(socialness)) {
                rankingLinkedHashMap.put(socialness, false);
            }
            if (rank.equals(mood)) {
                rankingLinkedHashMap.put(mood, false);
            }
        }

        if (dataType.isLocation()) {

            RealmResults<Location> temp = adviceLocation();
            if (temp != null) {
                adviceJustification.setJustificationLocation(temp);
            } else {
                rankingLinkedHashMap.put(location, false);
            }

        }

        if (dataType.isScreentime()) {

            AppData[] temp = adviceScreentime();
            if (temp != null) {
                adviceJustification.setJustificationScreentime(temp);
            } else {
                rankingLinkedHashMap.put(screentime, false);
            }

        }

        if (dataType.isSteps()) {

            Step temp = adviceStep();
            if (temp != null) {
                adviceJustification.setJustificationStep(temp);
            } else {
                rankingLinkedHashMap.put(step, false);
            }

        }

        if (dataType.isSocialness()) {
            RealmResults<Socialness> temp = adviceSocialness();
            if (temp != null) {
                adviceJustification.setJustificationSocialness(temp);
            } else {
                rankingLinkedHashMap.put(socialness, false);
            }

        }

        if (dataType.isMood()) {
            RealmResults<Mood> temp = adviceMood();
            if (temp != null) {
                adviceJustification.setJustificationMood(temp);
            } else {
                rankingLinkedHashMap.put(mood, false);
            }

        }

        int i = 0;
        int noAdviceAvailable = 0;
        for (String f : rankingLinkedHashMap.keySet()) {
            i++;
            Log.d(TAG, i + " Entry " + f);

            if (rankingLinkedHashMap.get(f)) {
                Log.d(TAG, "ranking " + rankingLinkedHashMap);

                Log.d(TAG, "AEWS " + "Advice " + f + " as " + rankingLinkedHashMap.get(f) + " " + adviceJustification);

                giveAdvice(f, adviceJustification);


                break;
            } else {
                noAdviceAvailable++;
                Log.d(TAG, "No Advice for " + f);
            }
        }

        if (noAdviceAvailable == rankingLinkedHashMap.size()) {
            Log.d(TAG, "No Advice Available");
            Toast.makeText(this, "No Advice Available", Toast.LENGTH_SHORT).show();
            insertAdvice(this, currentTime, noAdvice, null, null, null,
                    null, null, null, null, null, null,
                    null, null);

        }

    }


    public RealmResults<Location> adviceLocation() {
        Log.d(TAG, "adviceLocation: ");
        Date currentTime = Calendar.getInstance().getTime();

        RealmResults<Location> previousDaysLocations = getLocationOverPreviousDays(this, currentTime, days);

        if (previousDaysLocations == null) {
            //insufficient data
            Log.d(TAG, "adviceLocation: insufficient data ");


            return null;
        }

        Log.d(TAG, "adviceLocation: " + previousDaysLocations + " " + previousDaysLocations.size());

//        Location previous_location = null;
        android.location.Location previous_location = new android.location.Location("");
        android.location.Location new_location = new android.location.Location("");

        boolean first = true;
        Date skipThisDay = null;

        //todo likely incorrect? triggers to early? needs to look over all locations in a day before
        // setting rankingLinkedHashMap.put(location, true);
        for (Location loc : previousDaysLocations) {

            if (skipThisDay != null) {
                if (isSameDate(loc.getDateTime(), skipThisDay)) {
                    first = true;
                    continue;
                }
            }

            if (first) {
                first = false;
            } else {


                new_location.setLatitude(loc.getLatitude());
                new_location.setLongitude(loc.getLongitude());
                float distanceInMeters = previous_location.distanceTo(new_location);
//                Log.d(TAG, "adviceLocation: distanceInMeters " + distanceInMeters);


                if (!(distanceInMeters <= idealThresholdDistance)) {
                    rankingLinkedHashMap.put(location, true);
                    skipThisDay = loc.getDateTime();
                }


            }

            previous_location.setLatitude(loc.getLatitude());
            previous_location.setLongitude(loc.getLongitude());
        }


        if (rankingLinkedHashMap.get(location)) {
            return previousDaysLocations;
        }

        return null;
    }

    public AppData[] adviceScreentime() {

        Date currentTime = Calendar.getInstance().getTime();
        Screentime screentimePreviousDay = getScreentimePreviousDay(this, currentTime, 1);

        if (screentimePreviousDay == null) {
            //insufficient data
            Log.d(TAG, "adviceScreentime: insufficient data ");
            return null;
        }

        AppData[] appDataList;
        Screentime most_visible_screentime;
        boolean sort = false;

        Log.d(TAG, "adviceScreentime: " + screentimePreviousDay)
        ;
        Log.d(TAG, "adviceScreentime date: " + screentimePreviousDay.getDateTime());
        most_visible_screentime = screentimePreviousDay;
        if (screentimePreviousDay.getAppData().size() > 6) {
            appDataList = new AppData[5];
        } else {
            appDataList = new AppData[screentimePreviousDay.getAppData().size()];
        }

        for (AppData appData : screentimePreviousDay.getAppData()) {

            if (!sort) {
                for (int i = 0; i <= appDataList.length; i++) {
                    if (appDataList[i] == null) {
                        appDataList[i] = appData;
                        Log.d(TAG, "adviceScreentime: " + Arrays.toString(appDataList));
                        if (i == appDataList.length - 1) {
                            sort = true;
                            break;
                        }
                        break;
                    }
                }
            } else {
                appDataList = sortList(appDataList);

                if (appDataList[appDataList.length - 1].getTotalTimeInForeground() < appData.getTotalTimeInForeground()) {
                    appDataList[appDataList.length - 1] = appData;
                }
            }
        }

        int i = 0;
        for (AppData appData : appDataList) {
            i++;
            long TotalTimeInForeground = TimeUnit.MILLISECONDS.toMinutes(appData.getTotalTimeInForeground());
            long TotalTimeVisible = TimeUnit.MILLISECONDS.toMinutes(appData.getTotalTimeVisible());
            long TotalTimeForegroundServiceUsed = TimeUnit.MILLISECONDS.toMinutes(appData.getTotalTimeForegroundServiceUsed());
            Log.d(TAG, i + " + adviceScreentime: date " + most_visible_screentime.getDateTime() + " " + appData.getPackageName() + " TotalTimeInForeground " + TotalTimeInForeground + " TotalTimeVisible " + TotalTimeVisible + " TotalTimeForegroundServiceUsed " + TotalTimeForegroundServiceUsed);
        }

        if (idealScreentimeUsage <= TimeUnit.MILLISECONDS.toMinutes(appDataList[0].getTotalTimeInForeground())) {
            rankingLinkedHashMap.put(screentime, true);
            return appDataList;

        }

        return null;

    }

    public AppData[] sortList(AppData[] appDataList) {
        List<AppData> dataList = new ArrayList<>(Arrays.asList(appDataList));

//        Log.d(TAG, "THIS : " + dataList);

        Collections.sort(dataList, (o1, o2) -> {
//                    Log.d(TAG, "compare: " + dataList);
                    return o1.getTotalTimeInForeground().compareTo(o2.getTotalTimeInForeground());
                }
        );

        Log.d(TAG, "sorted : " + dataList);
        return dataList.toArray(new AppData[0]);
    }


    public Step adviceStep() {
        Date currentTime = Calendar.getInstance().getTime();
        float stepCountPreviousDay;

        Step stepPreviousDay = getStepPreviousDay(this, currentTime, 1);

        if (stepPreviousDay == null) {
            //insufficient data
            Log.d(TAG, "adviceStep: insufficient data ");

            return null;
        }

        stepCountPreviousDay = stepPreviousDay.getStepCount();

        if (stepCountPreviousDay < idealStepcount) {

            rankingLinkedHashMap.put(step, true);
            return stepPreviousDay;

        }


        return null;
    }

    public RealmResults<Socialness> adviceSocialness() {
        Date currentTime = Calendar.getInstance().getTime();
        float socialnessTotalOverPreviousDays = 0;

        RealmResults<Socialness> socialnessOverPreviousDays = getSocialnessOverPreviousDays(this, currentTime, days);

        if (socialnessOverPreviousDays == null) {
            //insufficient data
            Log.d(TAG, "adviceSocialness: insufficient data ");

            return null;
        }

        int soocialnessSize = 0;
        for (Socialness social : socialnessOverPreviousDays) {
            soocialnessSize++;
            socialnessTotalOverPreviousDays += social.getRating();
        }

        float average = (socialnessTotalOverPreviousDays / soocialnessSize);

        if (idealSocialness > average) {
            rankingLinkedHashMap.put(socialness, true);
            return socialnessOverPreviousDays;

        }


        return null;

    }

    public RealmResults<Mood> adviceMood() {
        Date currentTime = Calendar.getInstance().getTime();
        float moodTotalOverPreviousDays = 0;

        RealmResults<Mood> moodOverPreviousDays = getMoodOverPreviousDays(this, currentTime, days);

        if (moodOverPreviousDays == null) {
            //insufficient data

            Log.d(TAG, "adviceMood: insufficient data ");

            return null;
        }
        int moodSize = 0;
        for (Mood mood : moodOverPreviousDays) {
            moodSize++;
            moodTotalOverPreviousDays += mood.getRating();
        }
        float average = (moodTotalOverPreviousDays / moodSize);
        if (idealMood > average) {
            rankingLinkedHashMap.put(mood, true);
            return moodOverPreviousDays;

        }


        return null;

    }


    public void giveAdvice(String adviceKey, AdviceJustification adviceJustification) {

        String title = "Advice";
        String text = "";
        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
        User_Information user_information = getUserInformation(this);
        Date currentTime = Calendar.getInstance().getTime();
        Data_Type dataType = getDataType(this);
        Weather weather = null;

        if (dataType.isWeather()) {
            weather = getNextAvailableClearDay(this, currentTime);
        }

        if (!dataType.isWeather() && dataType.isExternal_temp()) {
            weather = getNextAvailableReasonableTempDay(this, currentTime);
        }


        String name = "";
        boolean nameExists = false;

        if (user_information.getName() != null) {
            //gets the name
            name = user_information.getName();
            nameExists = true;
        }

        if (adviceKey.equals(step)) {
            //if the user has provided their name to the IA and the there is a clear day in the realm database
            if (nameExists && weather != null) {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {
                    text = getStepAdvice(0, days, adviceJustification.getJustificationStep().getStepCount(), name, weather.getDateTime(), weather.getWeather(), weather.getFeels_like_day(), weather.getSunRise(), weather.getSunSet());
                    insertAdvice(this, currentTime, step, weather.getDateTime(), text, adviceJustification.getJustificationStep().getStepCount(), null, null, null,
                            Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), adviceJustification.getJustificationLocation(), adviceJustification.getJustificationSocialness(), adviceJustification.getJustificationMood());
                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getStepAdvice(0, name);
                    insertAdvice(this, currentTime, step, weather.getDateTime(), text, adviceJustification.getJustificationStep().getStepCount(), null, null, null,
                            Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), adviceJustification.getJustificationLocation(), adviceJustification.getJustificationSocialness(), adviceJustification.getJustificationMood());
                }
                //if the user has provided their name to the IA
            } else if (nameExists) {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {
                    text = getStepAdvice(0, days, adviceJustification.getJustificationStep().getStepCount(), name);

                    insertAdvice(this, currentTime, step, changeDayBy(currentTime, 1), text, adviceJustification.getJustificationStep().getStepCount(), null, null, null,
                            Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), adviceJustification.getJustificationLocation(), adviceJustification.getJustificationSocialness(), adviceJustification.getJustificationMood());

                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getStepAdvice(0, name);

                    insertAdvice(this, currentTime, step, changeDayBy(currentTime, 1), text, adviceJustification.getJustificationStep().getStepCount(), null, null, null,
                            Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), adviceJustification.getJustificationLocation(), adviceJustification.getJustificationSocialness(), adviceJustification.getJustificationMood());

                }
            } else {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {
                    text = getStepAdvice(0, days, adviceJustification.getJustificationStep().getStepCount());

                    insertAdvice(this, currentTime, step, changeDayBy(currentTime, 1), text, adviceJustification.getJustificationStep().getStepCount(), null, null, null,
                            Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), adviceJustification.getJustificationLocation(), adviceJustification.getJustificationSocialness(), adviceJustification.getJustificationMood());

                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getStepAdvice(0);

                    insertAdvice(this, currentTime, step, changeDayBy(currentTime, 1), text, adviceJustification.getJustificationStep().getStepCount(), null, null, null,
                            Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), adviceJustification.getJustificationLocation(), adviceJustification.getJustificationSocialness(), adviceJustification.getJustificationMood());

                }
            }


        }


        if (adviceKey.equals(screentime)) {
            AppData[] screentimeAppdata = adviceJustification.getJustificationScreentime();

            //if the user has provided their name to the IA
            if (nameExists) {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {

                    text = getScreentimeAdvice(1, screentimeAppdata[0].getPackageName(), TimeUnit.MILLISECONDS.toMinutes(screentimeAppdata[0].getTotalTimeInForeground()), name);

                    insertAdvice(this, currentTime, screentime, changeDayBy(currentTime, 1), text, null, screentimeAppdata[0], null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));

                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getScreentimeAdvice(1, screentimeAppdata[0].getPackageName(), name);

                    insertAdvice(this, currentTime, screentime, changeDayBy(currentTime, 1), text, null, screentimeAppdata[0], null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                }
            } else {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {
                    text = getScreentimeAdvice(1, screentimeAppdata[0].getPackageName(), TimeUnit.MILLISECONDS.toMinutes(screentimeAppdata[0].getTotalTimeInForeground()));

                    insertAdvice(this, currentTime, screentime, changeDayBy(currentTime, 1), text, null, screentimeAppdata[0], null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getScreentimeAdvice(1, screentimeAppdata[0].getPackageName());

                    insertAdvice(this, currentTime, screentime, changeDayBy(currentTime, 1), text, null, screentimeAppdata[0], null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                }
            }

        }


        if (adviceKey.equals(location)) {
            Log.d(TAG, "giveAdvice: " + location);
            //if the user has provided their name to the IA
            if (nameExists && weather != null) {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {
                    //todo location days is not the same (potentially)
                    text = getLocationAdvice(0, days, name, weather.getDateTime(), weather.getWeather(), weather.getFeels_like_day(), weather.getSunRise(), weather.getSunSet());
                    insertAdvice(this, currentTime, location, weather.getDateTime(), text, null, null, null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    //todo give a date with which to go out for
                    text = getLocationAdvice(0, name, weather.getDateTime(), weather.getWeather(), weather.getFeels_like_day(), weather.getSunRise(), weather.getSunSet());
                    insertAdvice(this, currentTime, location, changeDayBy(currentTime, 1), text, null, null, null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                }
            } else if (nameExists) {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {
                    //todo location days is not the same (potentially)
                    text = getLocationAdvice(0, days, name);

                    insertAdvice(this, currentTime, location, changeDayBy(currentTime, 1), text, null, null, null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));

                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getLocationAdvice(0, name);
                    insertAdvice(this, currentTime, location, changeDayBy(currentTime, 1), text, null, null, null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                }
            } else {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {
                    //todo location days is not the same (potentially)
                    text = getLocationAdvice(0, days);

                    insertAdvice(this, currentTime, location, changeDayBy(currentTime, 1), text, null, null, null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getLocationAdvice(0);

                    insertAdvice(this, currentTime, location, changeDayBy(currentTime, 1), text, null, null, null, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                }
            }

        }


        if (adviceKey.equals(socialness)) {
//            int averageSocialness = ;
            float socialnessTotalOverPreviousDays = 0;

            RealmResults<Socialness> socialnessOverPreviousDays = adviceJustification.getJustificationSocialness();

            int soocialnessSize = 0;
            for (Socialness social : socialnessOverPreviousDays) {
                soocialnessSize++;
                socialnessTotalOverPreviousDays += social.getRating();
            }

            float averageSocialness = (socialnessTotalOverPreviousDays / soocialnessSize);


            //if the user has provided their name to the IA
            if (nameExists) {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {

                    text = getSocialnessAdvice(0, soocialnessSize, averageSocialness, name);

                    insertAdvice(this, currentTime, socialness, changeDayBy(currentTime, 1), text, null, null, averageSocialness, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getSocialnessAdvice(0, soocialnessSize, name);

                    insertAdvice(this, currentTime, socialness, changeDayBy(currentTime, 1), text, null, null, averageSocialness, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                }
            } else {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {
                    text = getSocialnessAdvice(0, soocialnessSize, averageSocialness);
                    insertAdvice(this, currentTime, socialness, changeDayBy(currentTime, 1), text, null, null, averageSocialness, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));

                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getSocialnessAdvice(0, soocialnessSize);
                    insertAdvice(this, currentTime, socialness, changeDayBy(currentTime, 1), text, null, null, averageSocialness, null, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));
                }
            }

        }


        if (adviceKey.equals(mood)) {


            float moodTotalOverPreviousDays = 0;

            RealmResults<Mood> moodOverPreviousDays = adviceJustification.getJustificationMood();
            int moodSize = 0;

            for (Mood mood : moodOverPreviousDays) {
                moodSize++;
                moodTotalOverPreviousDays += mood.getRating();
            }

            float averageMood = (moodTotalOverPreviousDays / moodSize);


            //if the user has provided their name to the IA
            if (nameExists) {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {

                    text = getMoodAdvice(0, moodSize, averageMood, name);
                    insertAdvice(this, currentTime, mood, changeDayBy(currentTime, 1), text, null, null, null, averageMood, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));

                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getMoodAdvice(0, moodSize, name);
                    insertAdvice(this, currentTime, mood, changeDayBy(currentTime, 1), text, null, null, null, averageMood, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));

                }
            } else {
                //if the user is in the WITH_JUSTIFICATION group
                if (withJustification(intelligent_agent)) {
                    text = getMoodAdvice(0, moodSize, averageMood);
                    insertAdvice(this, currentTime, mood, changeDayBy(currentTime, 1), text, null, null, null, averageMood, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));


                    //if the user is in the NO_JUSTIFICATION group
                } else if (noJustification(intelligent_agent)) {
                    text = getMoodAdvice(0, moodSize);
                    insertAdvice(this, currentTime, mood, changeDayBy(currentTime, 1), text, null, null, null, averageMood, Collections.singletonList(adviceJustification.getJustificationStep()), adviceJustification.getJustificationScreentime(), convertLocationToRealmList(this, adviceJustification.getJustificationLocation()),
                            convertSocialnessToRealmList(this, adviceJustification.getJustificationSocialness()), convertMoodToRealmList(this, adviceJustification.getJustificationMood()));

                }
            }

        }

        Log.d(TAG, "giveAdvice: displayNotification ");

        //Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, AdviceActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL_ID);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(text);

        builder.setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigTextStyle)
//                .setContentText(text)
                .setSmallIcon(R.drawable.ic_advice)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(ADVICE, builder.build());


    }


}
