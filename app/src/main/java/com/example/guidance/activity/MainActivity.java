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
import com.example.guidance.Util.Util;
import com.example.guidance.realm.model.Advice;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.example.guidance.Util.IA.NO_JUSTIFICATION;
import static com.example.guidance.Util.IA.WITH_JUSTIFICATION;
import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.getAdviceForAfterDate;
import static com.example.guidance.realm.databasefunctions.AdviceDatabaseFunctions.getAdviceOnDate;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.isIntelligentAgentInitialised;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.isQuestionaireAnswered;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.noAdvice;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static Realm realm;

    TextView currentAdvice, currentGraph;
    private DrawerLayout drawer;
    private boolean questionaire;

    //Tag
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
        Data_Type dataType = getDataType(this);
        //sets the activityTheme to the gender of the intelligent agent, this is done before the onCreate
        //so that the user does not see a flash of one colour as it changes to the other
        Util.setActivityTheme(intelligent_agent, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isIntelligentAgentInitialised(this)) {
            Intent intent = new Intent(this, PasscodeActivity.class);
            startActivity(intent);
        }


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
        //hides the navigation items that shouldn't be shown
        navigationViewVisibility(navigationView, intelligent_agent, dataType);
        setSupportActionBar(toolbar);

        //sets the toolbar color to gender of the intelligent agent
        Util.setToolbarColor(intelligent_agent, toolbar, getResources());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Date currentTime = Calendar.getInstance().getTime();

        if (isIntelligentAgentInitialised(this)) {
            RealmResults<Advice> advice = getAdviceOnDate(this, currentTime);
            Log.d(TAG, "onCreate: adviceSize " + advice.size());
            if(advice.isEmpty() || advice.get(0).getAdviceType().equals(noAdvice)){
                currentAdvice.setText(getResources().getString(R.string.no_advice_for_today));
            }else{

                currentAdvice.setText(Objects.requireNonNull(advice.get(0)).getAdvice());
            }

            if(intelligent_agent.getAdvice().equals(WITH_JUSTIFICATION)){
                currentGraph.setVisibility(View.VISIBLE);
            }else if (intelligent_agent.getAdvice().equals(NO_JUSTIFICATION)){
                currentGraph.setVisibility(View.INVISIBLE);

            }

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
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("onPauseServiceReceiver");
//        broadcastIntent.setClass(this, onPauseServiceReceiver.class);
//        this.sendBroadcast(broadcastIntent);

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
    protected void onResume() {
        if (isIntelligentAgentInitialised(this) && !isQuestionaireAnswered(this) && !questionaire) {
            questionaire = true;
            Intent intent = new Intent(this, QuestionaireActivity.class);
            startActivity(intent);
        }

        super.onResume();
    }
}