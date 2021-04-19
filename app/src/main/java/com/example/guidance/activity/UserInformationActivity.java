package com.example.guidance.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.guidance.Util.Util.navigationViewDebug;
import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.Util.Util.scheduleExport;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.updateAPIKey;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.getSizeAllQuestionnaire;
import static com.example.guidance.realm.databasefunctions.UserInformationDatabaseFunctions.getUserInformation;
import static com.example.guidance.realm.databasefunctions.UserInformationDatabaseFunctions.updateUserInformation;

public class UserInformationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    //Tag
    private static final String TAG = "UserInformationActivity";


    private DrawerLayout drawer;
    private TextInputEditText name, age, otherGender, apiKey;
    private Spinner spinner;
    private TextInputLayout textInputLayoutInputGender, textInputLayoutAPIKey;
    Button buttonAPIKey;
    NavigationView navigationView;


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


        name = findViewById(R.id.inputNameUserInformation);
        age = findViewById(R.id.inputAgeUserInformation);
        otherGender = findViewById(R.id.inputGenderOtherUserInformation);
        textInputLayoutInputGender = findViewById(R.id.textInputLayoutGenderUserInformation);

        textInputLayoutAPIKey = findViewById(R.id.textInputSaveAPIKey);
        apiKey = findViewById(R.id.textInputSaveAPIKEY);
        buttonAPIKey = findViewById(R.id.buttonSaveAPIKey);

        textInputLayoutAPIKey.setVisibility(GONE);
        apiKey.setVisibility(GONE);
        buttonAPIKey.setVisibility(GONE);


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
                otherGender.setVisibility(VISIBLE);
                textInputLayoutInputGender.setVisibility(VISIBLE);
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

        hideKeyboard(UserInformationActivity.this);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: " + parent.getItemAtPosition(position));

        if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other))) {
            Log.d(TAG, "onItemSelected: visible");
            //something set visible
            otherGender.setVisibility(VISIBLE);
            textInputLayoutInputGender.setVisibility(VISIBLE);

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

        if (String.valueOf(name.getText()).equals(getResources().getString(R.string.debugAPI))) {

            navigationViewDebug(navigationView);
            Toast.makeText(this, "Made Debug Visible", Toast.LENGTH_SHORT).show();

        } else if (String.valueOf(name.getText()).equals(getResources().getString(R.string.password))) {


            if(getSizeAllQuestionnaire(this)<2){
//                questionnaire = true;

                Intent intent = new Intent(this, QuestionaireActivity.class);
                startActivity(intent);
            }





            Date currentDate = Calendar.getInstance().getTime();

            if (currentDate.after(getIntelligentAgent(this).getEnd_Date())) {
                textInputLayoutAPIKey.setVisibility(VISIBLE);
                apiKey.setVisibility(VISIBLE);
                buttonAPIKey.setVisibility(VISIBLE);
//                Toast.makeText(this, "Displayed API Text Box and Button", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Study still Ongoing", Toast.LENGTH_SHORT).show();
            }


        } else {
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

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            updateUserInformation(this, entryName, entryAge, entryGender, userSpecifiedGender);
        }


    }

    public void deleteUserInformation(View view) {

        String entryName = null;
        Integer entryAge = null;
        String entryGender = null;
        String userSpecifiedGender = null;

        updateUserInformation(this, entryName, entryAge, entryGender, userSpecifiedGender);

        refreshUiElements();

        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    public void refreshUiElements() {
        name.setText("");
        age.setText("");
        spinner.setSelection(0);
        otherGender.setVisibility(View.INVISIBLE);
        textInputLayoutInputGender.setVisibility(View.INVISIBLE);


    }

    public void saveAPIKEY(View view) {

        String key = null;


        if (!String.valueOf(apiKey.getText()).equals("")) {
            key = Objects.requireNonNull(apiKey.getText()).toString();
        }
        if (key != null) {
            updateAPIKey(this, key);

            scheduleExport(this);

            if(getIntelligentAgent(this).isStudyStatus()){
                textInputLayoutAPIKey.setVisibility(GONE);
                apiKey.setVisibility(GONE);
                buttonAPIKey.setVisibility(GONE);
            }


        } else {
            Toast.makeText(this, "Please Enter The API Key Provided", Toast.LENGTH_SHORT).show();
        }
    }
}
