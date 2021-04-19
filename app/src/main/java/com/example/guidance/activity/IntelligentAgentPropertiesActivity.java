package com.example.guidance.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.guidance.R;
import com.example.guidance.Util.Util;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.google.android.material.navigation.NavigationView;

import static com.example.guidance.Util.IA.*;
import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.isIntelligentAgentInitialised;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.isQuestionaireAnswered;

public class IntelligentAgentPropertiesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Tag
    private static final String TAG = "IntelligentAgentPropertiesActivity";
    private DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    boolean questionnaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
        Data_Type dataType = getDataType(this);

        //sets the activityTheme to the gender of the intelligent agent, this is done before the onCreate
        //so that the user does not see a flash of one colour as it changes to the other
        Util.setActivityTheme(intelligent_agent, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligent_agent_properties);

        if (!isIntelligentAgentInitialised(this)) {
            Intent intent = new Intent(this, PasscodeActivity.class);
            startActivity(intent);
        }
//        drawer = findViewById(R.id.drawer_layout_main_activity);
        drawer = findViewById(R.id.drawer_layout_intelligent_agent_properties_activity);

        TextView analysis = findViewById(R.id.textViewAnalysis);
        TextView advice = findViewById(R.id.textViewAdvice);
        TextView gender = findViewById(R.id.textViewGender);
        TextView interaction = findViewById(R.id.textViewInteraction);



         toolbar = findViewById(R.id.toolbar);
         navigationView = findViewById(R.id.nav_view);
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

        //Sets the analysis textView to the relevant text attribute
        if(intelligent_agent.getAnalysis().equals(MACHINE_LEARNING)){
            analysis.setText(TEXT_MACHINE_LEARNING);
        }else if (intelligent_agent.getAnalysis().equals(TRADITIONAL_PROGRAMMING)){
            analysis.setText(TEXT_TRADITIONAL_PROGRAMMING);
        }

        //Sets the advice textView to the relevant text attribute
        if(withJustification(intelligent_agent)){
            advice.setText(TEXT_WITH_JUSTIFICATION);
        }else if (noJustification(intelligent_agent)){
            advice.setText(TEXT_NO_JUSTIFICATION);
        }
        //Sets the gender textView to the relevant text attribute
        if(intelligent_agent.getGender().equals(MALE)){
            gender.setText(TEXT_MALE);
        }else if (intelligent_agent.getGender().equals(FEMALE)){
            gender.setText(TEXT_FEMALE);
        }

        //Sets the interaction textView to the relevant text attribute
        if(intelligent_agent.getInteraction().equals(HIGH)){
            interaction.setText(TEXT_HIGH);
        }else if (intelligent_agent.getInteraction().equals(LOW)){
            interaction.setText(TEXT_LOW);
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

    @Override
    protected void onResume() {


        if (isIntelligentAgentInitialised(this) && !isQuestionaireAnswered(this) && !questionnaire) {

            //sets the toolbar color to gender of the intelligent agent
            Util.setToolbarColor(getIntelligentAgent(this), toolbar, getResources());
            Util.navigationViewVisibility(navigationView, getIntelligentAgent(this), getDataType(this));

            questionnaire = true;
            Intent intent = new Intent(this, QuestionaireActivity.class);
            startActivity(intent);
        }

        super.onResume();
    }

}