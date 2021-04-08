package com.example.guidance.activity;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guidance.R;
import com.example.guidance.ServiceReceiver.onPauseServiceReceiver;
import com.example.guidance.Util.Util;
import com.example.guidance.Util.adapter.AdviceAdapter;
import com.example.guidance.realm.model.Advice;
import com.example.guidance.realm.model.AdviceUsageData;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Screentime;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.guidance.Util.IA.FEMALE;
import static com.example.guidance.Util.IA.MALE;
import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.Util.Util.scheduleAdvice;
import static com.example.guidance.Util.Util.scheduleAdviceFollowed;
import static com.example.guidance.Util.Util.scheduleLocation;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.getAdviceOnDate;

import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.getAllValidAdviceNotToday;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.LocationDatabaseFunctions.getLocationOverPreviousDays;

public class AdviceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Tag
    private static final String TAG = "AdviceActivity";

    private DrawerLayout drawer;


    static Realm realm;
    private TextView adviceEmpty, noAdvice, view;
    private RecyclerView recyclerViewAdvice, recyclerViewAdviceToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
        Data_Type dataType = getDataType(this);

        //sets the activityTheme to the gender of the intelligent agent, this is done before the onCreate
        //so that the user does not see a flash of one colour as it changes to the other
        Util.setActivityTheme(intelligent_agent, this);


        Date currentTime = Calendar.getInstance().getTime();
        //todo delete?
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        RealmResults<Advice> allValidAdvice = getAllValidAdviceNotToday(this,realm, currentTime);
        RealmResults<Advice> adviceToday = getAdviceOnDate(this,realm, currentTime);
//        RealmResults<Advice> allAdvice = getAllAdvice(realm, currentTime);

//        Log.d(TAG, "onCreate: allAdvice.size " + allAdvice.size() + " allAdvice "+ allAdvice);
//        for(Advice aasd : allAdvice){
//            Log.d(TAG, "onCreate: another " + aasd);
//        }
//
//
//        Log.d(TAG, "onCreate: allValidAdvice.size " + allValidAdvice.size());
//        Log.d(TAG, "onCreate: adviceToday.Size " + adviceToday.size());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);




//        view = findViewById(R.id.textView);
//        view.setMovementMethod(new ScrollingMovementMethod());
        recyclerViewAdvice = findViewById(R.id.recyclerViewAdvice);
        recyclerViewAdviceToday = findViewById(R.id.recyclerViewTodayAdvice);
        noAdvice = findViewById(R.id.textViewAdviceNoAdvice);
        adviceEmpty = findViewById(R.id.textViewAdviceEmptyAdvice);
        drawer = findViewById(R.id.drawer_layout_advice_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
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



        if (adviceToday.isEmpty()) {
            noAdvice.setVisibility(View.VISIBLE);
            recyclerViewAdviceToday.setVisibility(View.GONE);

        } else {

            noAdvice.setVisibility(View.GONE);
            recyclerViewAdviceToday.setVisibility(View.VISIBLE);



            if (intelligent_agent.getGender().equals(FEMALE)) {
                AdviceAdapter adviceAdapter = new AdviceAdapter(this, adviceToday, getResources(), getResources().getColor(R.color.femalePrimaryColour), getResources().getColor(R.color.color_before), getResources().getColor(R.color.color_after), true, currentTime);
                recyclerViewAdviceToday.setAdapter(adviceAdapter);
                recyclerViewAdviceToday.setLayoutManager(new LinearLayoutManager(this));
            } else if (intelligent_agent.getGender().equals(MALE)) {
                AdviceAdapter adviceAdapter = new AdviceAdapter(this, adviceToday, getResources(), getResources().getColor(R.color.malePrimaryColour), getResources().getColor(R.color.color_before), getResources().getColor(R.color.color_after), true, currentTime);
                recyclerViewAdviceToday.setAdapter(adviceAdapter);
                recyclerViewAdviceToday.setLayoutManager(new LinearLayoutManager(this));
            }


//            currentAdvice.setText(getResources().getString(R.string.no_advice));

        }

        if(allValidAdvice.isEmpty()){

            adviceEmpty.setVisibility(View.VISIBLE);
            recyclerViewAdvice.setVisibility(View.GONE);

        }else{

            Log.d(TAG, "onCreate: allValidAdvice" + allValidAdvice);
            adviceEmpty.setVisibility(View.GONE);
            recyclerViewAdvice.setVisibility(View.VISIBLE);

            AdviceAdapter adviceAdapter = new AdviceAdapter(this, allValidAdvice, getResources(), getResources().getColor(R.color.malePrimaryColour), getResources().getColor(R.color.color_before), getResources().getColor(R.color.color_after), false, currentTime);
            recyclerViewAdvice.setAdapter(adviceAdapter);
            recyclerViewAdvice.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause:");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("onPauseServiceReceiver");
        broadcastIntent.setClass(this, onPauseServiceReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onPause();
    }

    public void startAdviceJobService(View view) {

        scheduleAdvice(this);

    }

    public void startAdviceFollowedJobService(View view) {

        scheduleAdviceFollowed(this);

    }

    public void startLocationJobService(View view) {

        scheduleLocation(this);
    }


    public void displayLocationsss(View view) {
//        RealmQuery<Location> locationRealmQuery = realm.where(Location.class);
//        RealmResults<Location> temp = locationRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();
        Date currentTime = Calendar.getInstance().getTime();
        RealmResults<Location> temp = getLocationOverPreviousDays(this, currentTime, 3);

        Log.d(TAG, "displayLocation " + temp.size() + " location full list: " + temp);

        this.view.setText("");
        StringBuilder displayString = new StringBuilder();
        StringBuilder displayString2 = new StringBuilder();
        for (Location t : temp) {
            displayString.append(" ").append(t).append("\n");
            displayString2.append(t.getDateTime()).append(" ").append(t.getLatitude()).append(" , ").append(t.getLongitude()).append("\n");
            displayString.append(" ").append("\n");
        }
//        Log.d(TAG, "displayLocation: " + displayString2);
        this.view.setText(displayString);

    }

    public void deleteLocation(View view) {
        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Location");
            r.delete(Location.class);
        });
        Log.d(TAG, "deleteLocation: ");

    }


    public void deleteScreentime(View view) {
        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Screentime");
            r.delete(Screentime.class);
        });
        Log.d(TAG, "deleteScreentime: ");

    }

    public void displayDataType(View view) {
        Data_Type dataType = getDataType(this);

        Log.d(TAG, "displayDataType " + " DataTypeUsageData full list: " + dataType);

        this.view.setText("");

        String displayString = " " + dataType + "\n" + " " + "\n";
        this.view.setText(displayString);


    }

    public void displayAdvice(View view) {
        RealmQuery<Advice> adviceRealmQuery = realm.where(Advice.class);
//        RealmResults<Mood> mood = moodRealmQuery.findAll();
        RealmResults<Advice> advice = adviceRealmQuery.sort("dateTimeAdviceGiven", Sort.DESCENDING).findAll();

        Log.d(TAG, "displayAdvice " + advice.size() + " advice full list: " + advice);
        this.view.setText("");
        StringBuilder displayString = new StringBuilder();
        for (Advice t : advice) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");

        }
        this.view.setText(displayString);

    }

    public void displayAdviceUsageData(View view) {
        RealmQuery<AdviceUsageData> adviceUsageDataRealmQuery = realm.where(AdviceUsageData.class);
//        RealmResults<Mood> mood = moodRealmQuery.findAll();
        RealmResults<AdviceUsageData> adviceUsageData = adviceUsageDataRealmQuery.sort("dateTimeAdviceGiven", Sort.DESCENDING).findAll();

        Log.d(TAG, "displayAdviceUsageData " + adviceUsageData.size() + " adviceUsageData full list: " + adviceUsageData);
        this.view.setText("");
        StringBuilder displayString = new StringBuilder();
        for (AdviceUsageData t : adviceUsageData) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");

        }
        this.view.setText(displayString);

    }

    public void deleteAdvice(View view) {

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Screentime");
            r.delete(Advice.class);
        });
    }
}