package com.example.guidance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.guidance.R;
import com.example.guidance.model.Data_Storing;
import com.google.android.material.navigation.NavigationView;

import io.realm.Realm;
import io.realm.RealmQuery;

public class DataActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static Realm realm;

    //Tag
    private static final String TAG = "DataActivity";

    private DrawerLayout drawer;

    Switch steps, distance_traveled, location, device_temp, screentime, sleep_tracking, weather, external_temp, sun, socialness, mood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        drawer = findViewById(R.id.drawer_layout_data_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        steps = findViewById(R.id.switchSteps);
        distance_traveled = findViewById(R.id.switchDistanceTraveled);
        location = findViewById(R.id.switchLocation);
        device_temp = findViewById(R.id.switchDeviceTemp);
        screentime = findViewById(R.id.switchScreentime);
        sleep_tracking = findViewById(R.id.switchSleepTracking);
        weather = findViewById(R.id.switchWeather);
        external_temp = findViewById(R.id.switchExternalTemp);
        sun = findViewById(R.id.switchSun);
        socialness = findViewById(R.id.switchSocialness);
        mood = findViewById(R.id.switchMood);

        realm = Realm.getDefaultInstance();
        RealmQuery<Data_Storing> tasksQuery = realm.where(Data_Storing.class);
        Data_Storing test = tasksQuery.findFirst();
        assert test != null;


        steps.setChecked(test.isSteps());
        distance_traveled.setChecked(test.isDistance_traveled());
        location.setChecked(test.isLocation());
        device_temp.setChecked(test.isAmbient_temp());
        screentime.setChecked(test.isScreentime());
        sleep_tracking.setChecked(test.isSleep_tracking());
        weather.setChecked(test.isWeather());
        external_temp.setChecked(test.isExternal_temp());
        sun.setChecked(test.isSun());
        socialness.setChecked(test.isSocialness());
        mood.setChecked(test.isMood());

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
        }


        return true;
    }

    public void updateDatabase(View view) {

        if (view.getId() == R.id.switchSteps) {
            realm.executeTransactionAsync(r -> {
                // Get a data to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setSteps(steps.isChecked());
                Log.i(TAG, "Updated Data_Storing: steps " + steps.isChecked());
            });
        } else if (view.getId() == R.id.switchDistanceTraveled) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setDistance_traveled(distance_traveled.isChecked());
                Log.i(TAG, "Updated Data_Storing: distance_traveled " + distance_traveled.isChecked());

            });
        } else if (view.getId() == R.id.switchLocation) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setLocation(location.isChecked());
                Log.i(TAG, "Updated Data_Storing: location " + location.isChecked());

            });
        } else if (view.getId() == R.id.switchDeviceTemp) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setAmbient_temp(device_temp.isChecked());
                Log.i(TAG, "Updated Data_Storing: device_temp " + device_temp.isChecked());

            });
        } else if (view.getId() == R.id.switchScreentime) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setScreentime(screentime.isChecked());
                Log.i(TAG, "Updated Data_Storing: screentime " + screentime.isChecked());

            });
        } else if (view.getId() == R.id.switchSleepTracking) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setSleep_tracking(sleep_tracking.isChecked());
                Log.i(TAG, "Updated Data_Storing: sleep_tracking " + sleep_tracking.isChecked());

            });
        } else if (view.getId() == R.id.switchWeather) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setWeather(weather.isChecked());
                Log.i(TAG, "Updated Data_Storing: weather " + weather.isChecked());

            });
        } else if (view.getId() == R.id.switchExternalTemp) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setExternal_temp(external_temp.isChecked());
                Log.i(TAG, "Updated Data_Storing: external_temp " + external_temp.isChecked());

            });
        } else if (view.getId() == R.id.switchSun) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setSun(sun.isChecked());
                Log.i(TAG, "Updated Data_Storing: sun " + sun.isChecked());

            });
        } else if (view.getId() == R.id.switchSocialness) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setSocialness(socialness.isChecked());
                Log.i(TAG, "Updated Data_Storing: socialness " + socialness.isChecked());

            });
        } else if (view.getId() == R.id.switchMood) {
            realm.executeTransactionAsync(r -> {
                // Get the Data_Storing class to update.
                Data_Storing data = r.where(Data_Storing.class).findFirst();
                // Update properties on the instance.
                // This change is saved to the realm.
                data.setMood(mood.isChecked());
                Log.i(TAG, "Updated Data_Storing: mood " + mood.isChecked());

            });
        }

    }


}