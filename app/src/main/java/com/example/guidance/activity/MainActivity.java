package com.example.guidance.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guidance.model.Ambient_Temperature;
import com.example.guidance.model.Data_Storing;
import com.example.guidance.R;
import com.example.guidance.ServiceReciver.AmbientTempServiceReceiver;
import com.example.guidance.scheduler.Util;
import com.example.guidance.sensorservices.AmbientTempJobService;
import com.google.android.material.navigation.NavigationView;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);

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

    public void implementData_Storing(View view) {
        realm.executeTransactionAsync(r -> {
            // Instantiate the class using the factory function.
            Data_Storing init = r.createObject(Data_Storing.class, new ObjectId());
            // Configure the instance.
            init.setSteps(true);
            init.setDistance_traveled(true);
            init.setLocation(true);
            init.setDevice_temp(true);
            init.setScreentime(true);
            init.setSleep_tracking(true);
            init.setWeather(true);
            init.setExternal_temp(true);
            init.setSun(true);
            init.setSocialness(true);
            init.setMood(true);


            // Create a TurtleEnthusiast with a primary key.
//            ObjectId primaryKeyValue = new ObjectId();
//            TurtleEnthusiast turtleEnthusiast = r.createObject(TurtleEnthusiast.class, primaryKeyValue);
        });
        Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
    }

    public void readData_Storing(View view) {

//        RealmQuery<Data_Storing> tasksQuery = realm.where(Data_Storing.class);
//
//        Data_Storing test = tasksQuery.findFirst();
//
//        assert test != null;
//
//        Log.i(TAG, "isSteps " + test.isSteps());
//        Log.i(TAG, "isMood " + test.isMood());
//        Log.i(TAG, "isSocialness " + test.isSocialness());
//        Log.i(TAG, "isDevice_temp " + test.isDevice_temp());
//        Log.i(TAG, "isLocation " + test.isLocation());


    }

    public void deleteAmbient(View view) {
        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleteAmbient: deleted Ambient_Temperature");
            r.delete(Ambient_Temperature.class);
        });
    }

    public void displayAmbient(View view) {
        RealmQuery<Ambient_Temperature> tasksQuery = realm.where(Ambient_Temperature.class);

        RealmResults<Ambient_Temperature> test = tasksQuery.findAll();
        Log.d(TAG, "displayAmbient " + test.size() + " ambient Temp full list: " + test);

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
        }


        return true;
    }

    public void jobRunning(View view) {
        boolean test = isJobIdRunning(this, 123);
        Toast.makeText(this, "Result " + test, Toast.LENGTH_SHORT).show();
    }

    public static boolean isJobIdRunning(Context context, int JobId) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        for (JobInfo jobInfo : jobScheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JobId) {
                Log.d(TAG, "isJobIdRunning: " + JobId + true);
                return true;
            }
        }
        Log.d(TAG, "isJobIdRunning: " + JobId + false);
        return false;
    }

    Intent mServiceIntent;
    private AmbientTempJobService mYourService;

    public void start(View view) {

//        mYourService = new DeviceTempJobService();
//        mServiceIntent = new Intent(this, mYourService.getClass());
//        if (!isMyServiceRunning(mYourService.getClass())) {
//            startService(mServiceIntent);
//        }

        Util.scheduleJob(this);
//


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }


    public void stop(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job Cancelled");
    }


    @Override
    protected void onDestroy() {
//        stopService(mServiceIntent);
        Log.d(TAG, "onDestroy:");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(".ServiceReciver.AmbientTempServiceReceiver");
        broadcastIntent.setClass(this, AmbientTempServiceReceiver.class);
        this.sendBroadcast(broadcastIntent);

        super.onDestroy();
    }
}