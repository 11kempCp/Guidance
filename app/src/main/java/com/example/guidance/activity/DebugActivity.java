package com.example.guidance.activity;

import android.annotation.SuppressLint;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.guidance.R;
import com.example.guidance.jobServices.LocationJobService;
import com.example.guidance.model.Ambient_Temperature;
import com.example.guidance.model.Data_Storing;
import com.example.guidance.model.Location;
import com.example.guidance.model.Mood;
import com.example.guidance.model.Socialness;
import com.example.guidance.model.Step;
import com.example.guidance.services.LocationService;
import com.example.guidance.services.StepsService;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static com.example.guidance.scheduler.Util.LOCATION;
import static com.example.guidance.scheduler.Util.checkPermissionsAndSchedule;
import static com.example.guidance.scheduler.Util.scheduledUnscheduledJobs;
import static com.example.guidance.scheduler.Util.unscheduledJobs;
import static com.example.guidance.scheduler.Util.utilList;

public class DebugActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DebugActivity";
    static Realm realm;
    DrawerLayout drawer;
    TextView displayTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        realm = Realm.getDefaultInstance();


        displayTextView = findViewById(R.id.textViewDisplay);
        displayTextView.setMovementMethod(new ScrollingMovementMethod());



        drawer = findViewById(R.id.drawer_layout_debug_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    public void delete(View view) {
        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Ambient_Temperature");
            r.delete(Ambient_Temperature.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Step");
            r.delete(Step.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Data_Storing");
            r.delete(Data_Storing.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Data_Storing");
            r.delete(Location.class);
        });

        Toast.makeText(this, "Deleted Everything In Realm", Toast.LENGTH_SHORT).show();
    }

    public void display(View view) {
        RealmQuery<Ambient_Temperature> ambient_temperatureRealmQuery = realm.where(Ambient_Temperature.class);
        RealmResults<Ambient_Temperature> ambient_temp = ambient_temperatureRealmQuery.findAll();
        Log.d(TAG, "displayAmbient " + ambient_temp.size() + " ambient Temp full list: " + ambient_temp);


        RealmQuery<Step> stepRealmQuery = realm.where(Step.class);
        RealmResults<Step> step = stepRealmQuery.findAll();
        Log.d(TAG, "displaySteps " + step.size() + " steps full list: " + step);

        RealmQuery<Data_Storing> dataStoringQuery = realm.where(Data_Storing.class);
        RealmResults<Data_Storing> data = dataStoringQuery.findAll();
        Log.d(TAG, "displayData " + data.size() + " data full list: " + data);

        RealmQuery<Mood> moodRealmQuery = realm.where(Mood.class);
        RealmResults<Mood> mood = moodRealmQuery.findAll();
        Log.d(TAG, "displayMood " + mood.size() + " mood full list: " + mood);

        RealmQuery<Socialness> socialnessRealmQuery = realm.where(Socialness.class);
        RealmResults<Socialness> socialness = socialnessRealmQuery.findAll();
        Log.d(TAG, "displaySocialness " + socialness.size() + " socialness full list: " + socialness);


//        Toast.makeText(this, "displayAmbient " + ambient_temp.size() + " ambient Temp full list: " + ambient_temp, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "displaySteps " + step.size() + " steps full list: " + step, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "displayMood " + mood.size() + " mood full list: " + mood, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "displaySocialness " + socialness.size() + " socialness full list: " + socialness, Toast.LENGTH_SHORT).show();
    }


    public void jobRunning(View view) {

        List<Integer> test = unscheduledJobs(this);
        Toast.makeText(this, "Jobs " + test + " Not scheduled", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Jobs " + test + " Not scheduled");
    }


    public void start(View view) {
        scheduledUnscheduledJobs(this);
    }


    public void stop(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        for (int t : utilList) {
            scheduler.cancel(t);
        }

        Log.d(TAG, "All Jobs Cancelled");
        Toast.makeText(this, "All Jobs Cancelled", Toast.LENGTH_SHORT).show();
    }

    public void getSteps(View view) {
        Toast.makeText(this, "Steps " + StepsService.getmSteps(), Toast.LENGTH_SHORT).show();
    }

    public void resetSteps(View view) {

        StepsService.resetSensor();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
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

    public  void displayLocation(View view) {


        RealmQuery<Location> locationRealmQuery = realm.where(Location.class);
        RealmResults<Location> temp = locationRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();

        Log.d(TAG, "displayAmbient " + temp.size() + " ambient Temp full list: " + temp);

        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for(Location t : temp){
            displayString.append(" ").append(t).append("\n");
        }
        displayTextView.setText(displayString);


    }

    public void startLocationJobService(View view) {

        PackageManager packageManager = this.getPackageManager();


//        if (data.isLocation()) {
            Log.d(TAG, "scheduledUnscheduledJobs: " + LOCATION);
            checkPermissionsAndSchedule(this,
                    LOCATION,
                    LocationJobService.class,
                    this.getResources().getInteger(R.integer.default_time),
                    packageManager,
                    ACCESS_COARSE_LOCATION,
                    "none");

//        }


    }

    public void startLocationService(View view) {

//        Intent notificationIntent = new Intent(this, LocationService.class);
        Intent notificationIntent = new Intent(this, LocationService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            startForegroundService(notificationIntent);
        }else {
            Log.d(TAG, "startLocationService: error");
        }
    }


    public void displaySocialness(View view) {
        RealmQuery<Socialness> socialnessRealmQuery = realm.where(Socialness.class);
        RealmResults<Socialness> socialness = socialnessRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();
        Log.d(TAG, "displaySocialness " + socialness.size() + " socialness full list: " + socialness);

        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for(Socialness t : socialness){
            displayString.append(" ").append(t).append("\n");

        }
        displayTextView.setText(displayString);



    }

    public void displayMood(View view) {
        RealmQuery<Mood> moodRealmQuery = realm.where(Mood.class);
//        RealmResults<Mood> mood = moodRealmQuery.findAll();
        RealmResults<Mood> mood = moodRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();

        Log.d(TAG, "displayMood " + mood.size() + " mood full list: " + mood);
        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for(Mood t : mood){
            displayString.append(" ").append(t).append("\n");


        }
        displayTextView.setText(displayString);
    }

    public void displayAmbientTemp(View view) {
        RealmQuery<Ambient_Temperature> ambient_temperatureRealmQuery = realm.where(Ambient_Temperature.class);
//        RealmResults<Ambient_Temperature> ambient_temp = ambient_temperatureRealmQuery.findAll();
        RealmResults<Ambient_Temperature> temp = ambient_temperatureRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();

        Log.d(TAG, "displayAmbient " + temp.size() + " ambient Temp full list: " + temp);


        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for(Ambient_Temperature t : temp){
            displayString.append(" ").append(t).append("\n");
        }
        displayTextView.setText(displayString);

    }

    public void displaySteps(View view) {
        RealmQuery<Step> stepRealmQuery = realm.where(Step.class);
//        RealmResults<Step> step = stepRealmQuery.findAll();
        RealmResults<Step> step = stepRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();

        Log.d(TAG, "displaySteps " + step.size() + " steps full list: " + step);

        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for(Step t : step){
            displayString.append(" ").append(t).append("\n");
        }
        displayTextView.setText(displayString.toString());
    }

    public void clearText(View view) {

        displayTextView.setText("");
    }
}