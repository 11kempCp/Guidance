package com.example.guidance.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guidance.R;
import com.example.guidance.Util.Util;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import static com.example.guidance.Util.IA.Advice;
import static com.example.guidance.Util.IA.Analysis;
import static com.example.guidance.Util.IA.Gender;
import static com.example.guidance.Util.IA.Interaction;
import static com.example.guidance.Util.IA.Output;
import static com.example.guidance.Util.Util.requestPerms;
import static com.example.guidance.realm.DatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.DatabaseFunctions.initialiseDataType;
import static com.example.guidance.realm.DatabaseFunctions.intelligentAgentEntry;
import static com.example.guidance.realm.DatabaseFunctions.isDataTypeInitialised;
import static com.example.guidance.realm.DatabaseFunctions.isIntelligentAgentInitialised;

public class PasscodeActivity extends AppCompatActivity {

    private static final String TAG = "PasscodeActivity";
    //    private TextInputLayout textInput;
    private TextInputEditText textInput;
    ArrayList<String> passcodeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);

        Util.setActivityTheme(intelligent_agent, this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        textInput = findViewById(R.id.input);
        generatePasscodes();

    }


    public void enterPasscode(View view) {

        String passcode = String.valueOf(textInput.getText());
        if (isValidPasscode(passcode)) {

            if (!isIntelligentAgentInitialised(this)) {
                intelligentAgentEntry(PasscodeActivity.this, passcode);
            } else {
                Log.d(TAG, "intelligentAgentEntry: IA already initialised");
            }

            if (!isDataTypeInitialised(this)) {
                initialiseDataType(PasscodeActivity.this);
            } else {
                Log.d(TAG, "dataType already initialised");
            }

            requestPerms(this, PasscodeActivity.this);


//            Intent intent = new Intent(this, QuestionaireActivity.class);
//            startActivity(intent);


            finish();
        } else {
            Log.d(TAG, "Invalid passcode");
            Toast.makeText(this, "Invalid Passcode", Toast.LENGTH_SHORT).show();
        }


    }

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


    private boolean isValidPasscode(String passcode) {
        return passcodeList.contains(passcode);
    }


}