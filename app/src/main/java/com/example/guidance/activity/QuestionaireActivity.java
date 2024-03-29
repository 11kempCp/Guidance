package com.example.guidance.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guidance.R;
import com.example.guidance.Util.adapter.QuestionaireRecyclerViewAdapter;
import com.example.guidance.Util.Util;
import com.example.guidance.realm.model.Intelligent_Agent;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.Util.adapter.QuestionaireRecyclerViewAdapter.getAmountQuestionsAnswered;
import static com.example.guidance.Util.adapter.QuestionaireRecyclerViewAdapter.getAnswers;
import static com.example.guidance.Util.adapter.QuestionaireRecyclerViewAdapter.getQuestions;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.QuestionnaireDatabaseFunctions.insertQuestionnaire;

public class QuestionaireActivity extends AppCompatActivity {

    private static final String TAG = "QuestionaireActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);

        Util.setActivityTheme(intelligent_agent, this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionaire);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewQuestionaire);

        Date currentTime = Calendar.getInstance().getTime();

        //Question and answers variables, defaulted to equal "Error"
        String[] question = getResources().getStringArray(R.array.placeholder_questions);
        TypedArray question_answers = getResources().obtainTypedArray(R.array.placeholder_answers);

        if (currentTime.after(intelligent_agent.getEnd_Date())) {

            //gets the question string-array from strings.xml
            question = getResources().getStringArray(R.array.end_questions);
            //gets the answers array of string arrays from strings.xml
            question_answers = getResources().obtainTypedArray(R.array.end_answers);

        } else if(currentTime.before(intelligent_agent.getEnd_Date())){

            //gets the question string-array from strings.xml
            question = getResources().getStringArray(R.array.questions);
            //gets the answers array of string arrays from strings.xml
            question_answers = getResources().obtainTypedArray(R.array.answers);
        }

        //fills the questionnaire with questions
        QuestionaireRecyclerViewAdapter questionaireRecyclerViewAdapter = new QuestionaireRecyclerViewAdapter(this, question, question_answers, getResources());
        recyclerView.setAdapter(questionaireRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void submit(View view) {

        //Gets the questions
        String[] question = getQuestions();
        //if all the questions have been answered then they can be submitted
        if (getAmountQuestionsAnswered() == question.length) {

            Date currentTime = Calendar.getInstance().getTime();
            insertQuestionnaire(this, question, getAnswers(), currentTime);

//            Log.d(TAG, "submit: " + Arrays.toString(getAnswers()));
            finish();
        } else {
            Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show();
        }
    }


}