package com.example.guidance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.guidance.R;
import com.example.guidance.ServiceReceiver.onPauseServiceReceiver;
import com.example.guidance.realm.DatabaseFunctions;
import com.google.android.material.navigation.NavigationView;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.guidance.realm.DatabaseFunctions.initialiseDataStoring;
import static com.example.guidance.scheduler.Util.requestPerms;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static Realm realm;

    TextView currentAdvice, currentGraph;
    private DrawerLayout drawer;

    //Tag
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO remove this implementation in favour of passcode implementation
        if (!DatabaseFunctions.isDataStoringInitialised(this)) {
            initialiseDataStoring(this);

        }

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);


        requestPerms(this, this);


        realm = Realm.getDefaultInstance();
        currentAdvice = findViewById(R.id.textViewCurrentAdvice);
        currentGraph = findViewById(R.id.textViewCurrentGraph);
        drawer = findViewById(R.id.drawer_layout_main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void goToAdviceActivity(View view) {
        Intent myIntent = new Intent(this, AdviceActivity.class);
        startActivity(myIntent);

    }

    public void goToJustificationActivity(View view) {
        Intent myIntent = new Intent(this, JustificationActivity.class);
        startActivity(myIntent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                //TODO change back to DailyQuestionActivity
                myIntent = new Intent(this, LocationTestingActivity.class);
                startActivity(myIntent);
                break;
        }

        return true;
    }

//    public void delete(View view) {
//        realm.executeTransactionAsync(r -> {
//            Log.d(TAG, "deleteAmbient: deleted Ambient_Temperature");
//            r.delete(Ambient_Temperature.class);
//        });
//
//        realm.executeTransactionAsync(r -> {
//            Log.d(TAG, "deleteAmbient: deleted Step");
//            r.delete(Step.class);
//        });
//
//        Toast.makeText(this, "Deleted Everything In Realm", Toast.LENGTH_SHORT).show();
//    }

//    public void display(View view) {
//        RealmQuery<Ambient_Temperature> ambient_temperatureRealmQuery = realm.where(Ambient_Temperature.class);
//        RealmResults<Ambient_Temperature> ambient_temp = ambient_temperatureRealmQuery.findAll();
//        Log.d(TAG, "displayAmbient " + ambient_temp.size() + " ambient Temp full list: " + ambient_temp);
//
//
//        RealmQuery<Step> stepRealmQuery = realm.where(Step.class);
//        RealmResults<Step> step = stepRealmQuery.findAll();
//        Log.d(TAG, "displaySteps " + step.size() + " steps full list: " + step);
//
//        RealmQuery<Data_Storing> dataStoringQuery = realm.where(Data_Storing.class);
//        RealmResults<Data_Storing> data = dataStoringQuery.findAll();
//        Log.d(TAG, "displayData " + data.size() + " data full list: " + data);
//
//
////        Toast.makeText(this, "displayAmbient " + ambient_temp.size() + " ambient Temp full list: " + ambient_temp, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "displaySteps " + step.size() + " steps full list: " + step, Toast.LENGTH_SHORT).show();
//    }


//    public void jobRunning(View view) {
//
//        List<Integer> test = unscheduledJobs(this);
//        Toast.makeText(this, "Jobs " + test + " Not scheduled", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "Jobs " + test + " Not scheduled");
//    }
//
//
//    public void start(View view) {
//        scheduledUnscheduledJobs(this);
//    }
//
//
//    public void stop(View view) {
//        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        for (int t : utilList) {
//            scheduler.cancel(t);
//        }
//
//        Log.d(TAG, "All Jobs Cancelled");
//        Toast.makeText(this, "All Jobs Cancelled", Toast.LENGTH_SHORT).show();
//    }


//    public void getSteps(View view) {
//        Toast.makeText(this, "Steps " + StepsService.getmSteps(), Toast.LENGTH_SHORT).show();
//    }
//
//    public void resetSteps(View view) {
//
//        StepsService.resetSensor();
//    }
}