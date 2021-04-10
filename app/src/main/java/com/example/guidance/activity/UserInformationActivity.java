package com.example.guidance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.guidance.R;
import com.example.guidance.Util.Util;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.example.guidance.realm.model.User_Information;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.UserInformationDatabaseFunctions.getUserInformation;
import static com.example.guidance.realm.databasefunctions.UserInformationDatabaseFunctions.updateUserInformation;

public class UserInformationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    //Tag
    private static final String TAG = "UserInformationActivity";


    private DrawerLayout drawer;
    private TextInputEditText name, age, otherGender;
    private Spinner spinner;
    private TextInputLayout textInputLayoutInputGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
        Data_Type dataType = getDataType(this);
        User_Information user_information = getUserInformation(this);

        //sets the activityTheme to the gender of the intelligent agent, this is done before the onCreate
        //so that the user does not see a flash of one colour as it changes to the other
        Util.setActivityTheme(intelligent_agent, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);


        drawer = findViewById(R.id.drawer_layout_user_information_activity);

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


        name = findViewById(R.id.inputNameUserInformation);
        age = findViewById(R.id.inputAgeUserInformation);
        otherGender = findViewById(R.id.inputGenderOtherUserInformation);
        textInputLayoutInputGender = findViewById(R.id.textInputLayoutGenderUserInformation);


        spinner = (Spinner) findViewById(R.id.spinnerGenderUserInformation);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (user_information.getName() != null) {
            name.setText(user_information.getName());
        }

        if (user_information.getAge() != null) {
            age.setText(String.valueOf(user_information.getAge()));
        }

        Log.d(TAG, "onCreate: gender " + user_information.getGender());

        if (user_information.getGender() != null) {


            if (user_information.getGender().equals(getResources().getString(R.string.male))) {
                spinner.setSelection(1);

            }

            if (user_information.getGender().equals(getResources().getString(R.string.female))) {
                spinner.setSelection(2);
            }

            if (user_information.getGender().equals(getResources().getString(R.string.other))) {
                spinner.setSelection(3);
                textInputLayoutInputGender.getEditText().setText(user_information.getUserSpecifiedGender());


                //something set visible
                otherGender.setVisibility(View.VISIBLE);
                textInputLayoutInputGender.setVisibility(View.VISIBLE);
            } else {
                otherGender.setVisibility(View.INVISIBLE);
                textInputLayoutInputGender.setVisibility(View.INVISIBLE);
            }

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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: " + parent.getItemAtPosition(position));

        if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {
            Log.d(TAG, "onItemSelected: visible");
            //something set visible
            otherGender.setVisibility(View.VISIBLE);
            textInputLayoutInputGender.setVisibility(View.VISIBLE);

        } else {
            Log.d(TAG, "onItemSelected: invisible");

            //hide visible something
            otherGender.setVisibility(View.INVISIBLE);
            textInputLayoutInputGender.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void saveUserInformation(View view) {

        String entryName = null;
        Integer entryAge = null;
        String entryGender = null;
        String userSpecifiedGender = null;

        if (!String.valueOf(name.getText()).equals("")) {
            entryName = String.valueOf(name.getText());
        }

        if (!String.valueOf(age.getText()).equals("")) {
            entryAge = Integer.valueOf(String.valueOf(age.getText()));
        }

        if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.male))) {
            entryGender = spinner.getSelectedItem().toString();
        } else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.female))) {
            entryGender = spinner.getSelectedItem().toString();
        } else if (spinner.getSelectedItem().toString().equals(getResources().getString(R.string.other))) {
            if (!String.valueOf(otherGender.getText()).equals("")) {
                entryGender = getResources().getString(R.string.other);
                userSpecifiedGender = String.valueOf(otherGender.getText());

            }
        }

        updateUserInformation(this, entryName, entryAge, entryGender, userSpecifiedGender);
    }

    public void deleteUserInformation(View view) {

        String entryName = null;
        Integer entryAge = null;
        String entryGender = null;
        String userSpecifiedGender = null;

        updateUserInformation(this, entryName, entryAge, entryGender, userSpecifiedGender);

        refreshUiElements();

    }

    public void refreshUiElements() {
        name.setText("");
        age.setText("");
        spinner.setSelection(0);
        otherGender.setVisibility(View.INVISIBLE);
        textInputLayoutInputGender.setVisibility(View.INVISIBLE);


    }
}
