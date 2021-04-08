package com.example.guidance.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.guidance.R;
import com.example.guidance.ServiceReceiver.onPauseServiceReceiver;
import com.example.guidance.Util.Util;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.example.guidance.services.AmbientTempService;
import com.example.guidance.services.LocationService;
import com.example.guidance.services.StepsService;
import com.example.guidance.services.WeatherService;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

import static com.example.guidance.Util.Util.AMBIENT_TEMP;
import static com.example.guidance.Util.Util.DAILY_QUESTION;
import static com.example.guidance.Util.Util.LOCATION;
import static com.example.guidance.Util.Util.SCREENTIME;
import static com.example.guidance.Util.Util.STEPS;
import static com.example.guidance.Util.Util.WEATHER;
import static com.example.guidance.Util.Util.isPermsLocation;
import static com.example.guidance.Util.Util.isPermsSteps;
import static com.example.guidance.Util.Util.isPermsUsageStats;
import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.Util.Util.requestPermsFineLocation;
import static com.example.guidance.Util.Util.requestPermsSteps;
import static com.example.guidance.Util.Util.scheduleDailyQuestions;
import static com.example.guidance.Util.Util.scheduleScreentime;
import static com.example.guidance.Util.Util.scheduleWeather;
import static com.example.guidance.Util.Util.stopBackgroundNotification;
import static com.example.guidance.Util.Util.unscheduledJob;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.initialiseDataType;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.insertDataTypeUsageData;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.WeatherDatabaseFunctions.isExistingWeatherWeek;

public class DataActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static Realm realm;

    //Tag
    private static final String TAG = "DataActivity";

    private DrawerLayout drawer;

    Switch steps;
//    Switch distance_traveled;
    Switch location;
    Switch ambient_temp;
    Switch screentime;
//    Switch sleep_tracking;
    Switch weather;
    Switch external_temp;
    Switch sun;
    Switch socialness;
    Switch mood;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
        Data_Type dataType = getDataType(this);

        //sets the activityTheme to the gender of the intelligent agent, this is done before the onCreate
        //so that the user does not see a flash of one colour as it changes to the other
        Util.setActivityTheme(intelligent_agent, this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        drawer = findViewById(R.id.drawer_layout_data_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //hides the navigation items that shouldn't be shown
        navigationViewVisibility(navigationView, intelligent_agent, dataType);

        setSupportActionBar(toolbar);

        //sets the toolbar color to gender of the intelligent agent
        Util.setToolbarColor(intelligent_agent, toolbar, getResources());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        steps = findViewById(R.id.switchSteps);
//        distance_traveled = findViewById(R.id.switchDistanceTraveled);
        location = findViewById(R.id.switchLocation);
        ambient_temp = findViewById(R.id.switchAmbientTemp);
        screentime = findViewById(R.id.switchScreentime);
//        sleep_tracking = findViewById(R.id.switchSleepTracking);
        weather = findViewById(R.id.switchWeather);
        external_temp = findViewById(R.id.switchExternalTemp);
        sun = findViewById(R.id.switchSun);
        socialness = findViewById(R.id.switchSocialness);
        mood = findViewById(R.id.switchMood);

        realm = Realm.getDefaultInstance();
        Data_Type data_type = getDataType(this);

        if (data_type != null) {
            if (isPermsSteps(this)) {
                steps.setChecked(data_type.isSteps());
            } else {
                steps.setChecked(false);
            }


//            distance_traveled.setChecked(data_type.isDistance_traveled());

            if (isPermsLocation(this)) {
                location.setChecked(data_type.isLocation());
            } else {
                location.setChecked(false);
            }

            ambient_temp.setChecked(data_type.isAmbient_temp());

            if (isPermsUsageStats(this)) {
                screentime.setChecked(data_type.isScreentime());
            } else {
                screentime.setChecked(false);
            }

//            sleep_tracking.setChecked(data_type.isSleep_tracking());

            //todo if internet access not given?
            weather.setChecked(data_type.isWeather());
            external_temp.setChecked(data_type.isExternal_temp());
            sun.setChecked(data_type.isSun());

            socialness.setChecked(data_type.isSocialness());
            mood.setChecked(data_type.isMood());
        } else {
            initialiseDataType(this);
        }


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_menu:
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_advice:
                myIntent = new Intent(this, AdviceActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_data:
                myIntent = new Intent(this, DataActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_justification:
                myIntent = new Intent(this, JustificationActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_debug:
                myIntent = new Intent(this, DebugActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_daily_question:
                myIntent = new Intent(this, DailyQuestionActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_ranking:
                myIntent = new Intent(this, RankingActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_intelligent_agent_properties:
                myIntent = new Intent(this, IntelligentAgentPropertiesActivity.class);
                startActivity(myIntent);
                break;
        }


        return true;
    }

    public void switchSteps(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (steps.isChecked()) {
            requestPermsSteps(this, DataActivity.this);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(StepsService.class)) {
                    Intent serviceIntent = new Intent(this, StepsService.class);
                    stopService(serviceIntent);
                }
            }

            unscheduledJob(this, STEPS);
            stopBackgroundNotification(STEPS);
        }

        realm.executeTransactionAsync(r -> {
            // Get a data to update.
            Data_Type data = r.where(Data_Type.class).findFirst();
            // Update properties on the instance.
            // This change is saved to the realm.
            if (data != null) {
                data.setSteps(steps.isChecked());
            }
            Log.i(TAG, "Updated Data_Storing: steps " + steps.isChecked());
        });

        insertDataTypeUsageData(this, currentTime, String.valueOf(steps.getText()), steps.isChecked());
    }

    //Unused
    public void switchDistanceTraveled(View view) {
        Date currentTime = Calendar.getInstance().getTime();

//            if (distance_traveled.isChecked()) {
//            } else {
//                unscheduledJob(this, DISTANCE_TRAVELED);
//                stopBackgroundNotification(DISTANCE_TRAVELED);
//
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                if (Util.isMyServiceRunning(DistanceTraveledService.class)) {
//                    Intent serviceIntent = new Intent(this, DistanceTraveledService.class);
//                    stopService(serviceIntent);
//                }
//            }

//        realm.executeTransactionAsync(r -> {
//            // Get the Data_Storing class to update.
//            Data_Type data = r.where(Data_Type.class).findFirst();
//            // Update properties on the instance.
//            // This change is saved to the realm.
//            if (data != null) {
//                data.setDistance_traveled(distance_traveled.isChecked());
//            }
//            Log.i(TAG, "Updated Data_Storing: distance_traveled " + distance_traveled.isChecked());
//
//        });
//
//        insertDataTypeUsageData(this, currentTime, String.valueOf(distance_traveled.getText()), distance_traveled.isChecked());
    }

    public void switchLocation(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (location.isChecked()) {
            requestPermsFineLocation(this, DataActivity.this);
        } else {
            unscheduledJob(this, LOCATION);
            stopBackgroundNotification(LOCATION);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(LocationService.class)) {
                    Intent serviceIntent = new Intent(this, LocationService.class);
                    stopService(serviceIntent);
                }
            }

        }

        realm.executeTransactionAsync(r -> {
            // Get the Data_Storing class to update.
            Data_Type data = r.where(Data_Type.class).findFirst();
            // Update properties on the instance.
            // This change is saved to the realm.
            if (data != null) {
                data.setLocation(location.isChecked());
            }
            Log.i(TAG, "Updated Data_Storing: location " + location.isChecked());

        });
        insertDataTypeUsageData(this, currentTime, String.valueOf(location.getText()), location.isChecked());
    }

    public void switchAmbientTemp(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (!ambient_temp.isChecked()) {
            unscheduledJob(this, AMBIENT_TEMP);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(AmbientTempService.class)) {
                    Intent serviceIntent = new Intent(this, AmbientTempService.class);
                    stopService(serviceIntent);
                }
            }


        }

        realm.executeTransactionAsync(r -> {
            // Get the Data_Storing class to update.
            Data_Type data = r.where(Data_Type.class).findFirst();
            // Update properties on the instance.
            // This change is saved to the realm.
            if (data != null) {
                data.setAmbient_temp(ambient_temp.isChecked());
            }
            Log.i(TAG, "Updated Data_Storing: device_temp " + ambient_temp.isChecked());

        });

        insertDataTypeUsageData(this, currentTime, String.valueOf(ambient_temp.getText()), ambient_temp.isChecked());
    }

    public void switchScreentime(View view) {
        Date currentTime = Calendar.getInstance().getTime();


        if (!screentime.isChecked()) {
            Log.d(TAG, "switchScreentime: unscheduledJob SCREENTIME");
            unscheduledJob(this, SCREENTIME);
//                stopBackgroundNotification(SCREENTIME);
        } else {
            if (!isPermsUsageStats(this)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            } else {
                scheduleScreentime(this);
            }
        }

        realm.executeTransactionAsync(r -> {
            // Get the Data_Storing class to update.
            Data_Type data = r.where(Data_Type.class).findFirst();
            // Update properties on the instance.
            // This change is saved to the realm.
            if (data != null) {
                data.setScreentime(screentime.isChecked());
            }
            Log.i(TAG, "Updated Data_Storing: screentime " + screentime.isChecked());

        });

        insertDataTypeUsageData(this, currentTime, String.valueOf(screentime.getText()), screentime.isChecked());
    }

    public void switchWeather(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (!weather.isChecked() && !external_temp.isChecked() && !sun.isChecked()) {
            unscheduledJob(this, WEATHER);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(WeatherService.class)) {
                    Intent serviceIntent = new Intent(this, WeatherService.class);
                    stopService(serviceIntent);
                }
            }

        } else {
            if (!isExistingWeatherWeek(this, currentTime)) {
                scheduleWeather(this);
            }
        }

        realm.executeTransactionAsync(r -> {
            // Get the Data_Storing class to update.
            Data_Type data = r.where(Data_Type.class).findFirst();
            // Update properties on the instance.
            // This change is saved to the realm.
            if (data != null) {
                data.setWeather(weather.isChecked());
            }
            Log.i(TAG, "Updated Data_Storing: weather " + weather.isChecked());

        });

        insertDataTypeUsageData(this, currentTime, String.valueOf(weather.getText()), weather.isChecked());
    }

    public void switchExternalTemp(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (!weather.isChecked() && !external_temp.isChecked() && !sun.isChecked()) {
            unscheduledJob(this, WEATHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(WeatherService.class)) {
                    Intent serviceIntent = new Intent(this, WeatherService.class);
                    stopService(serviceIntent);
                }
            }
        } else {
            if (!isExistingWeatherWeek(this, currentTime)) {
                scheduleWeather(this);
            }
        }

        realm.executeTransactionAsync(r -> {
            // Get the Data_Storing class to update.
            Data_Type data = r.where(Data_Type.class).findFirst();
            // Update properties on the instance.
            // This change is saved to the realm.
            if (data != null) {
                data.setExternal_temp(external_temp.isChecked());
            }
            Log.i(TAG, "Updated Data_Storing: external_temp " + external_temp.isChecked());

        });

        insertDataTypeUsageData(this, currentTime, String.valueOf(external_temp.getText()), external_temp.isChecked());
    }

    public void switchSun(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (!weather.isChecked() && !external_temp.isChecked() && !sun.isChecked()) {
            unscheduledJob(this, WEATHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(WeatherService.class)) {
                    Intent serviceIntent = new Intent(this, WeatherService.class);
                    stopService(serviceIntent);
                }
            }
        } else {
            if (!isExistingWeatherWeek(this, currentTime)) {
                scheduleWeather(this);
            }
        }

        realm.executeTransactionAsync(r -> {
            // Get the Data_Storing class to update.
            Data_Type data = r.where(Data_Type.class).findFirst();
            // Update properties on the instance.
            // This change is saved to the realm.
            if (data != null) {
                data.setSun(sun.isChecked());
            }
            Log.i(TAG, "Updated Data_Storing: sun " + sun.isChecked());

        });

        insertDataTypeUsageData(this, currentTime, String.valueOf(sun.getText()), sun.isChecked());
    }


    //unused
    public void switchSleepTracking(View view) {
        Date currentTime = Calendar.getInstance().getTime();


//            if(!sleep_tracking.isChecked()){
//                unscheduledJob(this,SLEEP_TRACKING);
//                stopBackgroundNotification(SLEEP_TRACKING);
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                if (Util.isMyServiceRunning(SleepTrackingService.class)) {
//                    Intent serviceIntent = new Intent(this, SleepTrackingService.class);
//                    stopService(serviceIntent);
//                }
//            }

//        realm.executeTransactionAsync(r -> {
//            // Get the Data_Storing class to update.
//            Data_Type data = r.where(Data_Type.class).findFirst();
//            // Update properties on the instance.
//            // This change is saved to the realm.
//            if (data != null) {
//                data.setSleep_tracking(sleep_tracking.isChecked());
//            }
//            Log.i(TAG, "Updated Data_Storing: sleep_tracking " + sleep_tracking.isChecked());
//
//        });
//
//        insertDataTypeUsageData(this, currentTime, String.valueOf(sleep_tracking.getText()), sleep_tracking.isChecked());
    }


    public void switchSocialness(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (!socialness.isChecked() && !mood.isChecked()) {
            unscheduledJob(this, DAILY_QUESTION);
            stopBackgroundNotification(DAILY_QUESTION);
            navigationView.getMenu().findItem(R.id.nav_daily_question).setVisible(false);
        } else {
            navigationView.getMenu().findItem(R.id.nav_daily_question).setVisible(true);
            scheduleDailyQuestions(this);
        }

        realm.executeTransactionAsync(r -> {
            // Get the Data_Storing class to update.
            Data_Type data = r.where(Data_Type.class).findFirst();
            // Update properties on the instance.
            // This change is saved to the realm.
            if (data != null) {
                data.setSocialness(socialness.isChecked());
            }
            Log.i(TAG, "Updated Data_Storing: socialness " + socialness.isChecked());

        });

        insertDataTypeUsageData(this, currentTime, String.valueOf(socialness.getText()), socialness.isChecked());

    }

    public void switchMood(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (!socialness.isChecked() && !mood.isChecked()) {
            unscheduledJob(this, DAILY_QUESTION);
            stopBackgroundNotification(DAILY_QUESTION);
            navigationView.getMenu().findItem(R.id.nav_daily_question).setVisible(false);
        } else {
            navigationView.getMenu().findItem(R.id.nav_daily_question).setVisible(true);
            scheduleDailyQuestions(this);
        }

        realm.executeTransactionAsync(r -> {
            // Get the Data_Storing class to update.
            Data_Type data = r.where(Data_Type.class).findFirst();
            // Update properties on the instance.
            // This change is saved to the realm.
            if (data != null) {
                data.setMood(mood.isChecked());
            }
            Log.i(TAG, "Updated Data_Storing: mood " + mood.isChecked());

        });
        insertDataTypeUsageData(this, currentTime, String.valueOf(mood.getText()), mood.isChecked());
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause:");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("onPauseServiceReceiver");
        broadcastIntent.setClass(this, onPauseServiceReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onPause();
    }


}