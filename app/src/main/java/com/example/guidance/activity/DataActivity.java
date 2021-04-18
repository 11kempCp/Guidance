package com.example.guidance.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import static com.example.guidance.Util.Util.navigationViewAdviceRanking;
import static com.example.guidance.Util.Util.navigationViewDailyQuestion;
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
import static com.example.guidance.realm.databasefunctions.LocationDatabaseFunctions.insertLocation;
import static com.example.guidance.realm.databasefunctions.LocationDatabaseFunctions.isLocation;
import static com.example.guidance.realm.databasefunctions.WeatherDatabaseFunctions.isExistingExternalTempNull;
import static com.example.guidance.realm.databasefunctions.WeatherDatabaseFunctions.isExistingSunNull;
import static com.example.guidance.realm.databasefunctions.WeatherDatabaseFunctions.isExistingWeatherNull;
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
            SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


            //if there is no step counter sensor on the device then the viability of the
            //steps switch will be set to GONE
            if (isStepsSensor(mSensorManager)) {
                steps.setVisibility(View.VISIBLE);

                //if the permissions required for the stepsJobService is not given then the switch
                //will be set to false
                if (isPermsSteps(this)) {
                    steps.setChecked(data_type.isSteps());
                } else {
                    steps.setChecked(false);
                }
            } else {
                steps.setVisibility(View.GONE);
            }


//            distance_traveled.setChecked(data_type.isDistance_traveled());

            //if the permissions required for the locationJobService is not given then the switch
            //will be set to false
            if (isPermsLocation(this)) {
                location.setChecked(data_type.isLocation());
            } else {
                location.setChecked(false);
            }

            //if there is no ambient temperature sensor on the device then the viability of the
            //ambient temperature switch will be set to GONE
            if (isAmbientTemperatureSensor(mSensorManager)) {
                ambient_temp.setVisibility(View.VISIBLE);
                ambient_temp.setChecked(data_type.isAmbient_temp());
            } else {
                ambient_temp.setVisibility(View.GONE);
            }


            //if the permissions required for the screentimeJobService is not given then the switch
            //will be set to false
            if (isPermsUsageStats(this)) {
                screentime.setChecked(data_type.isScreentime());
            } else {
                screentime.setChecked(false);
            }

//            sleep_tracking.setChecked(data_type.isSleep_tracking());


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
            case R.id.nav_user_information:
                myIntent = new Intent(this, UserInformationActivity.class);
                startActivity(myIntent);
        }


        return true;
    }


    //function relating to the steps data type
    public void switchSteps(View view) {
        Date currentTime = Calendar.getInstance().getTime();


        if (allSwitchesFalse()) {
            navigationViewAdviceRanking(this,navigationView, false);
        }


        if (steps.isChecked()) {
            navigationViewAdviceRanking(this,navigationView, true);

            //requests the permissions relevant to the Steps job
            requestPermsSteps(this, DataActivity.this);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //if the Steps service is running then this stops it
                if (Util.isMyServiceRunning(StepsService.class)) {
                    Intent serviceIntent = new Intent(this, StepsService.class);
                    stopService(serviceIntent);
                }
            }

            //unscheduled and removes the notification for the STEPS job
            unscheduledJob(this, STEPS);
            stopBackgroundNotification(STEPS);
        }

        //updates the Data_Type class to reflect the status of the steps switch
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

        //inserts a DataTypeUsageData object into the realm
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

    //function relating to the location data type
    public void switchLocation(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (allSwitchesFalse()) {
            navigationViewAdviceRanking(this,navigationView, false);
        }

        if (location.isChecked()) {

            navigationViewAdviceRanking(this,navigationView, true);

            //requests the permissions relevant to the location job
            requestPermsFineLocation(this, DataActivity.this);
        } else {
            //unscheduled and removes the notification for the LOCATION job
            unscheduledJob(this, LOCATION);
            stopBackgroundNotification(LOCATION);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //if the location service is running then this stops it
                if (Util.isMyServiceRunning(LocationService.class)) {
                    Intent serviceIntent = new Intent(this, LocationService.class);
                    stopService(serviceIntent);
                }
            }

        }

        //updates the Data_Type class to reflect the status of the location switch
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
        //inserts a DataTypeUsageData object into the realm
        insertDataTypeUsageData(this, currentTime, String.valueOf(location.getText()), location.isChecked());
    }

    public void switchAmbientTemp(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (allSwitchesFalse()) {
            navigationViewAdviceRanking(this,navigationView, false);
        }

        if (!ambient_temp.isChecked()) {
            unscheduledJob(this, AMBIENT_TEMP);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(AmbientTempService.class)) {
                    Intent serviceIntent = new Intent(this, AmbientTempService.class);
                    stopService(serviceIntent);
                }
            }


        } else {
            navigationViewAdviceRanking(this,navigationView, true);

        }

        //updates the Data_Type class to reflect the status of the ambient Temp switch
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

        //inserts a DataTypeUsageData object into the realm
        insertDataTypeUsageData(this, currentTime, String.valueOf(ambient_temp.getText()), ambient_temp.isChecked());
    }

    public void switchScreentime(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (allSwitchesFalse()) {
            navigationViewAdviceRanking(this,navigationView, false);
        }


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

            navigationViewAdviceRanking(this,navigationView, true);

        }

        //updates the Data_Type class to reflect the status of the screentime switch
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

        //inserts a DataTypeUsageData object into the realm
        insertDataTypeUsageData(this, currentTime, String.valueOf(screentime.getText()), screentime.isChecked());
    }

    double longitude;
    double latitude;

    public void displayAlertDialog(Switch switchName, View view) {
        Context context = this;
        AlertDialog alert = new AlertDialog.Builder(this).create();

        alert.setTitle("Location");
        alert.setMessage("A location is required in order for this functionality to operate. Your location will only be stored on the device and will not be uploaded at the end of the study.");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);


        alert.setButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d(TAG, "onClick: enter " + input.getText().toString());


                boolean loc = createLocation(context, input.getText().toString());
//                boolean loc = createLocation(context, "Cardiff");

                if (loc) {
                    Date currentTime = Calendar.getInstance().getTime();
                    insertLocation(context, currentTime, Util.truncate(latitude), Util.truncate(longitude));
//                    switchWeather(view);

                    if(!isExistingWeatherWeek(context, currentTime)){
                        if (!scheduleWeather(context)) {
                            switchName.setChecked(false);
                            Toast.makeText(context, "Job scheduling failed please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }


                    alert.dismiss();

                } else {
                    Toast.makeText(context, "Location not recognised, please enter another location", Toast.LENGTH_SHORT).show();
                    switchName.setChecked(false);
                }


            }
        });

        alert.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                Log.d(TAG, "onClick: Cancel");

                switchName.setChecked(false);
                alert.dismiss();
            }
        });

        alert.show();
    }

    public boolean createLocation(Context context, String locationName) {

        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses;

            addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                Log.d(TAG, "createLocation: " + Util.truncate(latitude) + " , " + Util.truncate(longitude));


                return true;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    public void switchWeather(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (allSwitchesFalse()) {
            navigationViewAdviceRanking(this,navigationView, false);
        }

        if (!weather.isChecked() && !external_temp.isChecked() && !sun.isChecked()) {
            unscheduledJob(this, WEATHER);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(WeatherService.class)) {
                    Intent serviceIntent = new Intent(this, WeatherService.class);
                    stopService(serviceIntent);
                }
            }

        } else {

            if (!isLocation(this)) {
                displayAlertDialog(weather, view);
            } else {
                if (!isExistingWeatherWeek(this, currentTime)) {
                    if (!scheduleWeather(this)) {
                        weather.setChecked(false);
                        Toast.makeText(this, "Job scheduling failed please try again.", Toast.LENGTH_SHORT).show();
                    }

                }else if(isExistingWeatherNull(this, currentTime)){
                    if (!scheduleWeather(this)) {
                        weather.setChecked(false);
                        Toast.makeText(this, "Job scheduling failed please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }

        //updates the Data_Type class to reflect the status of the weather switch
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

        //inserts a DataTypeUsageData object into the realm
        insertDataTypeUsageData(this, currentTime, String.valueOf(weather.getText()), weather.isChecked());
    }

    public void switchExternalTemp(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (allSwitchesFalse()) {
            navigationViewAdviceRanking(this,navigationView, false);
        }

        if (!weather.isChecked() && !external_temp.isChecked() && !sun.isChecked()) {
            unscheduledJob(this, WEATHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(WeatherService.class)) {
                    Intent serviceIntent = new Intent(this, WeatherService.class);
                    stopService(serviceIntent);
                }
            }
        } else {

            if (!isLocation(this)) {
                displayAlertDialog(external_temp, view);
            } else {
                if (!isExistingWeatherWeek(this, currentTime)) {
                    if (!scheduleWeather(this)) {
                        external_temp.setChecked(false);
                        Toast.makeText(this, "Job scheduling failed please try again.", Toast.LENGTH_SHORT).show();
                    }
                }else if(isExistingExternalTempNull(this, currentTime)){
                    if (!scheduleWeather(this)) {
                        external_temp.setChecked(false);
                        Toast.makeText(this, "Job scheduling failed please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }


        }

        //updates the Data_Type class to reflect the status of the external temp switch
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

        //inserts a DataTypeUsageData object into the realm
        insertDataTypeUsageData(this, currentTime, String.valueOf(external_temp.getText()), external_temp.isChecked());
    }

    public void switchSun(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (allSwitchesFalse()) {
            navigationViewAdviceRanking(this,navigationView, false);
        }

        if (!weather.isChecked() && !external_temp.isChecked() && !sun.isChecked()) {
            unscheduledJob(this, WEATHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Util.isMyServiceRunning(WeatherService.class)) {
                    Intent serviceIntent = new Intent(this, WeatherService.class);
                    stopService(serviceIntent);
                }
            }
        } else {
            if (!isLocation(this)) {
                displayAlertDialog(sun, view);
            } else {
                if (!isExistingWeatherWeek(this, currentTime)) {
                    if (!scheduleWeather(this)) {
                        sun.setChecked(false);
                        Toast.makeText(this, "Job scheduling failed please try again.", Toast.LENGTH_SHORT).show();
                    }
                }else if(isExistingSunNull(this, currentTime)){
                    if (!scheduleWeather(this)) {
                        sun.setChecked(false);
                        Toast.makeText(this, "Job scheduling failed please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        //updates the Data_Type class to reflect the status of the sun switch
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

        //inserts a DataTypeUsageData object into the realm
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

        if (allSwitchesFalse()) {
            navigationViewAdviceRanking(this,navigationView, false);
        }

        //if both socialness and mood are disabled then the DAILY_QUESTION job service will be unscheduled
        // and the Daily Question Activity button on the navigation menu made unavailable
        //If either socialness or mood (or both) are enabled then the Daily Question Activity button on the navigation menu made available
        if (!socialness.isChecked() && !mood.isChecked()) {
            unscheduledJob(this, DAILY_QUESTION);
            stopBackgroundNotification(DAILY_QUESTION);

            navigationViewDailyQuestion(this,navigationView, false);
        } else {
            navigationViewAdviceRanking(this,navigationView, true);
            navigationViewDailyQuestion(this,navigationView, true);

            scheduleDailyQuestions(this);
        }

        //updates the Data_Type class to reflect the status of the socialness switch
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

        //inserts a DataTypeUsageData object into the realm
        insertDataTypeUsageData(this, currentTime, String.valueOf(socialness.getText()), socialness.isChecked());

    }

    public void switchMood(View view) {
        Date currentTime = Calendar.getInstance().getTime();

        if (allSwitchesFalse()) {
            navigationViewAdviceRanking(this, navigationView, false);
        }

        //if both socialness and mood are disabled then the DAILY_QUESTION job service will be unscheduled
        // and the Daily Question Activity button on the navigation menu made unavailable
        //If either socialness or mood (or both) are enabled then the Daily Question Activity button on the navigation menu made available

        if (!socialness.isChecked() && !mood.isChecked()) {
            unscheduledJob(this, DAILY_QUESTION);
            stopBackgroundNotification(DAILY_QUESTION);
            navigationViewDailyQuestion(this,navigationView, false);
        } else {
            navigationViewAdviceRanking(this,navigationView, true);
            navigationViewDailyQuestion(this,navigationView, true);

            scheduleDailyQuestions(this);
        }

        //updates the Data_Type class to reflect the status of the mood switch
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

        //inserts a DataTypeUsageData object into the realm
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


    //returns if there is a ambient temperature sensor on the device
    public boolean isAmbientTemperatureSensor(SensorManager mSensorManager) {
        return mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null;
    }


    //returns if there is a step counter sensor on the device
    public boolean isStepsSensor(SensorManager mSensorManager) {
        return mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null;
    }

    public boolean allSwitchesFalse() {
        return !steps.isChecked() &&
                !location.isChecked() &&
                !ambient_temp.isChecked() &&
                !screentime.isChecked() &&
                !socialness.isChecked() &&
                !mood.isChecked();
    }

}