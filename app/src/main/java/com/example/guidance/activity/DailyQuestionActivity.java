package com.example.guidance.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Objects;

import static com.example.guidance.Util.Util.DAILY_QUESTION;
import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.isIntelligentAgentInitialised;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.getMoodEntryDate;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.isMoodEntryToday;
import static com.example.guidance.realm.databasefunctions.MoodDatabaseFunctions.moodEntry;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.isQuestionaireAnswered;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.getSocialnessEntryDate;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.isSocialnessEntryDate;
import static com.example.guidance.realm.databasefunctions.SocialnessDatabaseFunctions.socialnessEntry;

public class DailyQuestionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DailyQuestionActivity";
    private DrawerLayout drawer;
    private RadioGroup radioGroupSocialness, radioGroupMood;
    private TextView socialnessDailyQuestion, moodDailyQuestion, socialnessClarification, moodClarification;
    private RadioButton socialnessButtonOne, socialnessButtonTwo, socialnessButtonThree, socialnessButtonFour, moodButtonOne, moodButtonTwo, moodButtonThree, moodButtonFour;
    NavigationView navigationView;
    boolean questionnaire = false;
    Toolbar toolbar;


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

        toolbar = findViewById(R.id.toolbar);
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


        socialnessDailyQuestion = findViewById(R.id.textViewDailyQuestionOne);
        socialnessClarification = findViewById(R.id.textViewClarificationOne);
        socialnessDailyQuestion.setText(R.string.daily_question_one);
        socialnessClarification.setText(R.string.daily_question_one_clarification);
        radioGroupSocialness = findViewById(R.id.radioGroupDailyQuestionOne);

        socialnessButtonOne = findViewById(R.id.radioButton1QuestionOne);
        socialnessButtonTwo = findViewById(R.id.radioButton2QuestionOne);
        socialnessButtonThree = findViewById(R.id.radioButton3QuestionOne);
        socialnessButtonFour = findViewById(R.id.radioButton4QuestionOne);


        moodDailyQuestion = findViewById(R.id.textViewDailyQuestionTwo);
        moodClarification = findViewById(R.id.textViewClarificationTwo);
        radioGroupMood = findViewById(R.id.radioGroupDailyQuestionTwo);
        moodDailyQuestion.setText(R.string.daily_question_two);
        moodClarification.setText(R.string.daily_question_two_clarification);

        moodButtonOne = findViewById(R.id.radioButton1QuestionTwo);
        moodButtonTwo = findViewById(R.id.radioButton2QuestionTwo);
        moodButtonThree = findViewById(R.id.radioButton3QuestionTwo);
        moodButtonFour = findViewById(R.id.radioButton4QuestionTwo);

        Date ct = Calendar.getInstance().getTime();
        setAllRadioButtonStatus(dataType, ct);

        radioGroupSocialness.setOnCheckedChangeListener((group, checkedId) -> {
            Util.stopBackgroundNotification(DAILY_QUESTION);
            switch (checkedId) {
                case R.id.radioButton1QuestionOne:
                    Date currentTime = Calendar.getInstance().getTime();
                    socialnessEntry(this, currentTime, Integer.parseInt(String.valueOf(socialnessButtonOne.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.radioButton2QuestionOne:
                    currentTime = Calendar.getInstance().getTime();
                    socialnessEntry(this, currentTime, Integer.parseInt(String.valueOf(socialnessButtonTwo.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton3QuestionOne:
                    currentTime = Calendar.getInstance().getTime();
                    socialnessEntry(this, currentTime, Integer.parseInt(String.valueOf(socialnessButtonThree.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton4QuestionOne:
                    currentTime = Calendar.getInstance().getTime();
                    socialnessEntry(this, currentTime, Integer.parseInt(String.valueOf(socialnessButtonFour.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
            }
        });

        radioGroupMood.setOnCheckedChangeListener((group, checkedId) -> {
            Util.stopBackgroundNotification(DAILY_QUESTION);
            switch (checkedId) {
                case R.id.radioButton1QuestionTwo:
                    Date currentTime = Calendar.getInstance().getTime();
                    moodEntry(this, currentTime, Integer.parseInt(String.valueOf(moodButtonOne.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton2QuestionTwo:
                    currentTime = Calendar.getInstance().getTime();
                    moodEntry(this, currentTime, Integer.parseInt(String.valueOf(moodButtonTwo.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton3QuestionTwo:
                    currentTime = Calendar.getInstance().getTime();
                    moodEntry(this, currentTime, Integer.parseInt(String.valueOf(moodButtonThree.getText())));
                    Toast.makeText(this, "Answer Saved", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.radioButton4QuestionTwo:
                    currentTime = Calendar.getInstance().getTime();
                    moodEntry(this, currentTime, Integer.parseInt(String.valueOf(moodButtonFour.getText())));
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
    protected void onResume() {
        super.onResume();
        Date ct = Calendar.getInstance().getTime();
        setAllRadioButtonStatus(getDataType(this), ct);

        if (isIntelligentAgentInitialised(this) && !isQuestionaireAnswered(this) && !questionnaire) {

            //sets the toolbar color to gender of the intelligent agent
            Util.setToolbarColor(getIntelligentAgent(this), toolbar, getResources());
            Util.navigationViewVisibility(navigationView, getIntelligentAgent(this), getDataType(this));

            questionnaire = true;
            Intent intent = new Intent(this, QuestionaireActivity.class);
            startActivity(intent);
        }

    }

    private void setAllRadioButtonStatus(Data_Type dataType, Date ct) {

        if (dataType.isSocialness()) {
            setSocialnessRadioButton(ct);
            socialnessVisible();
        } else {
            socialnessInvisible();
        }

        if (dataType.isMood()) {
            setMoodRadioButton(ct);
            moodVisible();
        } else {
            moodInvisible();
        }
    }

    private void moodVisible() {
        moodDailyQuestion.setVisibility(View.VISIBLE);
        moodClarification.setVisibility(View.VISIBLE);
        radioGroupMood.setVisibility(View.VISIBLE);
    }

    private void moodInvisible() {
        moodDailyQuestion.setVisibility(View.GONE);
        moodClarification.setVisibility(View.GONE);
        radioGroupMood.setVisibility(View.GONE);
    }


    private void socialnessVisible() {
        socialnessDailyQuestion.setVisibility(View.VISIBLE);
        socialnessClarification.setVisibility(View.VISIBLE);
        radioGroupSocialness.setVisibility(View.VISIBLE);
    }

    private void socialnessInvisible() {
        socialnessDailyQuestion.setVisibility(View.GONE);
        socialnessClarification.setVisibility(View.GONE);
        radioGroupSocialness.setVisibility(View.GONE);
    }

    private void setSocialnessRadioButton(Date ct) {


        if (isSocialnessEntryDate(this, ct)) {
            int value = Objects.requireNonNull(getSocialnessEntryDate(this, ct)).getRating();

            ((RadioButton) radioGroupSocialness.getChildAt(value - 1)).setChecked(true);
        }


    }

    private void setMoodRadioButton(Date ct) {
        if (isMoodEntryToday(this, ct)) {
            int value = Objects.requireNonNull(getMoodEntryDate(this, ct)).getRating();

            ((RadioButton) radioGroupMood.getChildAt(value - 1)).setChecked(true);
        }


    }


}