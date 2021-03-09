package com.example.guidance.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.guidance.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import static com.example.guidance.Util.Intelligent_Agent.Advice;
import static com.example.guidance.Util.Intelligent_Agent.Analysis;
import static com.example.guidance.Util.Intelligent_Agent.Gender;
import static com.example.guidance.Util.Intelligent_Agent.Interaction;
import static com.example.guidance.Util.Intelligent_Agent.Output;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        textInput = findViewById(R.id.input);

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
            }else {
                Log.d(TAG, "dataType already initialised");
            }

            //todo ask permissions

            finish();
        } else {
            Log.d(TAG, "Invalid passcode");
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

        generatePasscodes();
        return passcodeList.contains(passcode);
    }


}