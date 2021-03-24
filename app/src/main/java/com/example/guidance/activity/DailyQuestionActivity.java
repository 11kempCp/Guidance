package com.example.guidance.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.Util.Util.DAILY_QUESTION;
import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.getTodaysMoodEntry;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.isMoodEntryToday;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.moodEntry;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.getSocialnessDateRating;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.isSocialnessEntryDate;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.socialnessEntry;

public class DailyQuestionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DailyQuestionActivity";
    private DrawerLayout drawer;
    private RadioGroup radioGroupOne, radioGroupTwo;
    private TextView dailyQuestionOne, dailyQuestionTwo, clarificationOne, clarificationTwo;
    private RadioButton q1b1, q1b2, q1b3, q1b4, q2b1, q2b2, q2b3, q2b4;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
        Data_Type dataType = getDataType(this);
        //sets the activityTheme to the gender of the intelligent agent, this is done before the onCreate
        //so that the user does not see a flash of one colour as it changes to the other
        Util.setActivityTheme(intelligent_agent, this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_question);


        drawer = findViewById(R.id.drawer_layout_daily_question_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //hides the navigation items that shouldn't be shown
        navigationViewVisibility(navigationView,intelligent_agent, dataType);

        setSupportActionBar(toolbar);

        //sets the toolbar color to gender of the intelligent agent
        Util.setToolbarColor(intelligent_agent, toolbar, getResources());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //TODO selective toggling i.e. if the user only has mood tracking enabled then only the mood
        // question should be displayed
        dailyQuestionOne = findViewById(R.id.textViewDailyQuestionOne);
        dailyQuestionTwo = findViewById(R.id.textViewDailyQuestionTwo);
        clarificationOne = findViewById(R.id.textViewClarificationOne);
        clarificationTwo= findViewById(R.id.textViewClarificationTwo);

        radioGroupOne = findViewById(R.id.radioGroupDailyQuestionOne);
        radioGroupTwo = findViewById(R.id.radioGroupDailyQuestionTwo);

        dailyQuestionOne.setText(R.string.daily_question_one);
        dailyQuestionTwo.setText(R.string.daily_question_two);

        clarificationOne.setText(R.string.daily_question_one_clarification);
        clarificationTwo.setText(R.string.daily_question_two_clarification);


        q1b1 = findViewById(R.id.radioButton1QuestionOne);
        q1b2 = findViewById(R.id.radioButton2QuestionOne);
        q1b3 = findViewById(R.id.radioButton3QuestionOne);
        q1b4 = findViewById(R.id.radioButton4QuestionOne);

        q2b1 = findViewById(R.id.radioButton1QuestionTwo);
        q2b2 = findViewById(R.id.radioButton2QuestionTwo);
        q2b3 = findViewById(R.id.radioButton3QuestionTwo);
        q2b4 = findViewById(R.id.radioButton4QuestionTwo);

        Date ct = Calendar.getInstance().getTime();

        if(isMoodEntryToday(this, ct)){
            int value = getTodaysMoodEntry(this, ct);

            ((RadioButton)radioGroupTwo.getChildAt(value-1)).setChecked(true);
        }

        if(isSocialnessEntryDate(this, ct)){
            int value = getSocialnessDateRating(this, ct);

            ((RadioButton)radioGroupOne.getChildAt(value-1)).setChecked(true);
        }



        radioGroupOne.setOnCheckedChangeListener((group, checkedId) -> {
            Util.stopBackgroundNotification(DAILY_QUESTION);
            switch (checkedId){
                case R.id.radioButton1QuestionOne:
                    Date currentTime = Calendar.getInstance().getTime();
                    socialnessEntry(this, currentTime, Integer.parseInt(String.valueOf(q1b1.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.radioButton2QuestionOne:
                    currentTime = Calendar.getInstance().getTime();
                    socialnessEntry(this, currentTime, Integer.parseInt(String.valueOf(q1b2.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton3QuestionOne:
                    currentTime = Calendar.getInstance().getTime();
                    socialnessEntry(this, currentTime, Integer.parseInt(String.valueOf(q1b3.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton4QuestionOne:
                    currentTime = Calendar.getInstance().getTime();
                    socialnessEntry(this, currentTime, Integer.parseInt(String.valueOf(q1b4.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
            }
        });


        radioGroupTwo.setOnCheckedChangeListener((group, checkedId) -> {
            Util.stopBackgroundNotification(DAILY_QUESTION);
            switch (checkedId){
                case R.id.radioButton1QuestionTwo:
                    Date currentTime = Calendar.getInstance().getTime();
                    moodEntry(this, currentTime, Integer.parseInt(String.valueOf(q2b1.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton2QuestionTwo:
                    currentTime = Calendar.getInstance().getTime();
                    moodEntry(this, currentTime, Integer.parseInt(String.valueOf(q2b2.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton3QuestionTwo:
                    currentTime = Calendar.getInstance().getTime();
                    moodEntry(this, currentTime, Integer.parseInt(String.valueOf(q2b3.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton4QuestionTwo:
                    currentTime = Calendar.getInstance().getTime();
                    moodEntry(this, currentTime, Integer.parseInt(String.valueOf(q2b4.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
            }
        });
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

}