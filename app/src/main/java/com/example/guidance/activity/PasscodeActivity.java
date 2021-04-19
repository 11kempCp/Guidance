package com.example.guidance.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guidance.R;
import com.example.guidance.Util.Util;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import static com.example.guidance.Util.IA.Advice;
import static com.example.guidance.Util.IA.Analysis;
import static com.example.guidance.Util.IA.Gender;
import static com.example.guidance.Util.IA.Interaction;
import static com.example.guidance.Util.IA.Output;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.initialiseDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.intelligentAgentEntry;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.isDataTypeInitialised;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.initialiseRanking;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.isRankingInitialised;
import static com.example.guidance.realm.databasefunctions.UserInformationDatabaseFunctions.userInformationEntry;

public class PasscodeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "PasscodeActivity";
    private TextInputEditText passcode, name, age, otherGender;
    ArrayList<String> passcodeList = new ArrayList<>();
    private Spinner spinner;
    private TextInputLayout textInputLayoutInputGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);

        Util.setActivityTheme(intelligent_agent, this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        passcode = findViewById(R.id.inputPasscode);
        name = findViewById(R.id.inputName);
        age = findViewById(R.id.inputAge);
        otherGender = findViewById(R.id.inputGenderOther);
        textInputLayoutInputGender = findViewById(R.id.textInputLayoutGender);


        //todo change to a hardcoded passcode list
        generatePasscodes();

        spinner = (Spinner) findViewById(R.id.spinnerGender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }


    public void enterPasscode(View view) {

        String passcode = String.valueOf(this.passcode.getText());
        if (isValidPasscode(passcode)) {

            intelligentAgentEntry(PasscodeActivity.this, passcode);


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

            userInformationEntry(PasscodeActivity.this, entryName, entryAge, entryGender, userSpecifiedGender);

            //initialises the Data_Type realm
            if (!isDataTypeInitialised(this)) {
                initialiseDataType(PasscodeActivity.this);
            } else {
                Log.d(TAG, "dataType already initialised");
            }

            //initialises the Ranking realm
            if (!isRankingInitialised(this)) {
                initialiseRanking(this, 0,1,2,3,4, 3000,60);
            } else {
                Log.d(TAG, "ranking already initialised");
            }

//            requestPerms(this, PasscodeActivity.this);


//            Intent intent = new Intent(this, QuestionaireActivity.class);
//            startActivity(intent);

            //ends the activity and returns the user to the MainActivity
            finish();
        } else {
            Log.d(TAG, "Invalid passcode");
            Toast.makeText(this, "Invalid Passcode", Toast.LENGTH_SHORT).show();
        }


    }


    //todo change to a hardcoded passcode list
    //Creates the passcodes
    public void generatePasscodes() {

        StringBuilder passcode = new StringBuilder();
        int i = 0;
        for (String analysis : Analysis) {
            for (String advice : Advice) {
                for (String gender : Gender) {
                    for (String interaction : Interaction) {
                        for (String output : Output) {
                            i++;
                            passcode.append(analysis);
                            passcode.append(advice);
                            passcode.append(gender);
                            passcode.append(interaction);
                            passcode.append(output);
                            passcodeList.add(String.valueOf(passcode));
                            passcode.setLength(0);
                        }
                    }
                }
            }
        }

        if (i == (Analysis.size() * Advice.size() * Gender.size() * Interaction.size() * Output.size())) {
            Log.d(TAG, "generatePasscodes: correct size " + i);
        }

        Log.d(TAG, "generatePasscodes: " + passcodeList);


    }

    public void generatePasscodesAdjusted() {

        StringBuilder passcode = new StringBuilder();
        int i = 0;
        for (String analysis : Analysis) {
            for (String advice : Advice) {
                for (String gender : Gender) {
                            i++;
                            passcode.append(analysis);
                            passcode.append(advice);
                            passcode.append(gender);
                            passcodeList.add(String.valueOf(passcode));
                            passcode.setLength(0);

                }
            }
        }

        if (i == (Analysis.size() * Advice.size() * Gender.size())) {
            Log.d(TAG, "generatePasscodes: correct size " + i);
        }

        Log.d(TAG, "generatePasscodes: " + passcodeList);


    }


    //Checks to see if the passcode entered is valid
    private boolean isValidPasscode(String passcode) {
        return passcodeList.contains(passcode);
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
}