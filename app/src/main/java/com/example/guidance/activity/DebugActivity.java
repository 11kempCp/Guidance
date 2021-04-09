package com.example.guidance.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.guidance.jobServices.ScreentimeJobService;
import com.example.guidance.jobServices.WeatherJobService;
import com.example.guidance.realm.model.Advice;
import com.example.guidance.realm.model.Ambient_Temperature;
import com.example.guidance.realm.model.AppData;
import com.example.guidance.realm.model.DataTypeUsageData;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.example.guidance.realm.model.Location;
import com.example.guidance.realm.model.Mood;
import com.example.guidance.realm.model.Question;
import com.example.guidance.realm.model.Questionnaire;
import com.example.guidance.realm.model.Screentime;
import com.example.guidance.realm.model.Socialness;
import com.example.guidance.realm.model.Step;
import com.example.guidance.realm.model.Weather;
import com.example.guidance.services.StepsService;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.guidance.Util.IA.FEMALE;
import static com.example.guidance.Util.IA.MALE;
import static com.example.guidance.Util.Util.ADVICE;
import static com.example.guidance.Util.Util.LOCATION;
import static com.example.guidance.Util.Util.SCREENTIME;
import static com.example.guidance.Util.Util.WEATHER;
import static com.example.guidance.Util.Util.getUnscheduledJobs;
import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.Util.Util.scheduleAdvice;
import static com.example.guidance.Util.Util.scheduleJob;
import static com.example.guidance.Util.Util.scheduleLocation;
import static com.example.guidance.Util.Util.scheduledUnscheduledJobs;
import static com.example.guidance.Util.Util.utilList;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.intelligentAgentEntry;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.intelligentAgentSetGender;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.isIntelligentAgentInitialised;

public class DebugActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DebugActivity";
    static Realm realm;
    DrawerLayout drawer;
    TextView displayTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
        Data_Type dataType = getDataType(this);
        //sets the activityTheme to the gender of the intelligent agent, this is done before the onCreate
        //so that the user does not see a flash of one colour as it changes to the other
        Util.setActivityTheme(intelligent_agent, this);


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

        //hides the navigation items that shouldn't be shown
        navigationViewVisibility(navigationView, intelligent_agent, dataType);

        setSupportActionBar(toolbar);

        //sets the toolbar color to gender of the intelligent agent
        Util.setToolbarColor(intelligent_agent, toolbar, getResources());


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
            r.delete(Data_Type.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Location");
            r.delete(Location.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Weather");
            r.delete(Weather.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Intelligent_Agent");
            r.delete(Intelligent_Agent.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Socialness");
            r.delete(Socialness.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Mood");
            r.delete(Mood.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Questionaire");
            r.delete(Questionnaire.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Screentime");
            r.delete(Screentime.class);
        });

        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted Screentime");
            r.delete(Advice.class);
        });



        Toast.makeText(this, "Deleted Everything In Realm", Toast.LENGTH_SHORT).show();
    }

    public void deleteAdvice(View view){
        realm.executeTransactionAsync(r -> {
            Log.d(TAG, "deleted advice");
            r.delete(Advice.class);
        });

    }


    public void jobRunning(View view) {

        List<Integer> test = getUnscheduledJobs(this);
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
        Toast.makeText(this, "Steps " + StepsService.getSteps(), Toast.LENGTH_SHORT).show();
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

    public void displayLocation(View view) {


        RealmQuery<Location> locationRealmQuery = realm.where(Location.class);
        RealmResults<Location> temp = locationRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();

        Log.d(TAG, "displayLocation " + temp.size() + " location full list: " + temp);

        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        StringBuilder displayString2 = new StringBuilder();
        for (Location t : temp) {
            displayString.append(" ").append(t).append("\n");
            displayString2.append(t.getDateTime()).append(" ").append(t.getLatitude()).append(" , ").append(t.getLongitude()).append("\n");
            displayString.append(" ").append("\n");
        }
        Log.d(TAG, "displayLocation: " + displayString2);
        displayTextView.setText(displayString);


    }

    public void startLocationJobService(View view) {

        Log.d(TAG, "scheduledUnscheduledJobs: " + LOCATION);
        scheduleLocation(this);


    }

    public void startAdviceJobService(View view) {

        PackageManager packageManager = this.getPackageManager();

        Log.d(TAG, "scheduledUnscheduledJobs: " + ADVICE);

        scheduleAdvice(this);

    }

    public void startWeatherJobService(View view) {

        Log.d(TAG, "scheduledUnscheduledJobs: " + WEATHER);
        scheduleJob(this, WeatherJobService.class, WEATHER, this.getResources().getInteger(R.integer.weather), JobInfo.NETWORK_TYPE_UNMETERED);

    }

    public void startScreentimeJobService(View view) {

        Log.d(TAG, "scheduledUnscheduledJobs: " + SCREENTIME);
        scheduleJob(this, ScreentimeJobService.class, SCREENTIME, this.getResources().getInteger(R.integer.default_time));

    }


    public void initialiseIA(View view) {


        if (!isIntelligentAgentInitialised(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter Passcode");


            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);

            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> intelligentAgentEntry(DebugActivity.this, input.getText().toString()));
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        } else {
            Log.d(TAG, "intelligentAgentEntry: IA already initialised");
        }


    }


    public void displaySocialness(View view) {
        RealmQuery<Socialness> socialnessRealmQuery = realm.where(Socialness.class);
        RealmResults<Socialness> socialness = socialnessRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();
        Log.d(TAG, "displaySocialness " + socialness.size() + " socialness full list: " + socialness);

        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for (Socialness t : socialness) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");
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
        for (Mood t : mood) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");

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
        for (Ambient_Temperature t : temp) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");
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
        for (Step t : step) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");

        }
        displayTextView.setText(displayString.toString());
    }

    public void displayWeather(View view) {
        RealmQuery<Weather> weatherDayRealmQuery = realm.where(Weather.class);
//        RealmResults<Step> step = stepRealmQuery.findAll();
        RealmResults<Weather> weather = weatherDayRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();

        Log.d(TAG, "displayWeather " + weather.size() + " weather full list: " + weather);

        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for (Weather t : weather) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");
        }
        displayTextView.setText(displayString.toString());
    }

    public void clearText(View view) {

        displayTextView.setText("");
    }

    public void displayIA(View view) {

        RealmQuery<Intelligent_Agent> intelligent_agentRealmQuery = realm.where(Intelligent_Agent.class);
//        RealmResults<Step> step = stepRealmQuery.findAll();
        RealmResults<Intelligent_Agent> ia = intelligent_agentRealmQuery.findAll();

        Log.d(TAG, "displayIA " + ia.size() + " intelligent agent full list: " + ia);


        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for (Intelligent_Agent t : ia) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");
        }

        displayTextView.setText(displayString.toString());


    }


    public void displayQuestionnaire(View view) {

        RealmQuery<Questionnaire> questionaireRealmQuery = realm.where(Questionnaire.class);
//        RealmResults<Step> step = stepRealmQuery.findAll();
        RealmResults<Questionnaire> questionnaire = questionaireRealmQuery.findAll();

        Log.d(TAG, "displayIA " + questionnaire.size() + " intelligent agent full list: " + questionnaire);


        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for (Questionnaire quest : questionnaire) {
            displayString.append(" ").append(quest).append("\n");
            displayString.append(" ").append("\n");
            for (Question q : quest.getQuestion()) {
                displayString.append(" ").append(q.getQuestion()).append(" ").append(q.getAnswer()).append("\n");
                displayString.append(" ").append("\n");

            }
        }

        displayTextView.setText(displayString.toString());


    }

    public void displayDataTypeUsageData(View view) {
        RealmQuery<DataTypeUsageData> dataTypeUsageDataRealmQuery = realm.where(DataTypeUsageData.class);
//        RealmResults<Step> step = stepRealmQuery.findAll();
        RealmResults<DataTypeUsageData> dataTypeUsageData = dataTypeUsageDataRealmQuery.sort("dateTime", Sort.DESCENDING).findAll();

        Log.d(TAG, "displayDataTypeUsageData " + dataTypeUsageData.size() + " DataTypeUsageData full list: " + dataTypeUsageData);

        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for (DataTypeUsageData t : dataTypeUsageData) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");
        }
        displayTextView.setText(displayString.toString());
    }


    public void displayAdvice(View view){
        RealmQuery<Advice> adviceRealmQuery = realm.where(Advice.class);
//        RealmResults<Mood> mood = moodRealmQuery.findAll();
        RealmResults<Advice> advice = adviceRealmQuery.sort("dateTimeAdviceGiven", Sort.DESCENDING).findAll();

        Log.d(TAG, "displayAdvice " + advice.size() + " advice full list: " + advice);
        this.displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for (Advice t : advice) {
            displayString.append(" ").append(t).append("\n");
            displayString.append(" ").append("\n");

        }
        this.displayTextView.setText(displayString);
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


    public void toggleGender(View view) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);


        if (intelligent_agent != null) {

            String gender = intelligent_agent.getGender();
            String DEFAULT = "DEFAULT";
            if (gender.equals(DEFAULT)) {
                intelligentAgentSetGender(this, MALE);
            } else if (gender.equals(MALE)) {
                intelligentAgentSetGender(this, FEMALE);
            } else {
                intelligentAgentSetGender(this, "DEFAULT");
            }
        }

    }

    public void displayScreentime(View view) {

        RealmQuery<Screentime> screentimeRealmQuery = realm.where(Screentime.class);
//        RealmResults<Step> step = stepRealmQuery.findAll();
        RealmResults<Screentime> screentime = screentimeRealmQuery.findAll();

        Log.d(TAG, "displayScreentime " + screentime.size() + " screentime full list: " + screentime);


        displayTextView.setText("");
        StringBuilder displayString = new StringBuilder();
        for (Screentime st : screentime) {
            displayString.append(" ").append(st).append("\n");
            displayString.append(" ").append("\n");
            Log.d(TAG, "Screentime " + st);

            for (AppData appData : st.getAppData()) {
                Log.d(TAG, "appData " + appData);
                displayString.append(" ").append(appData.getPackageName()).append(" ").append(appData.getTotalTimeInForeground()).append(" ").append(appData.getTotalTimeVisible()).append(" ").append(appData.getTotalTimeForegroundServiceUsed()).append("\n");
//                displayString.append(" ").append("\n");
            }
            Log.d(TAG, "                                   ");
        }

        displayTextView.setText(displayString.toString());


    }

}