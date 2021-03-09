package com.example.guidance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.guidance.R;
import com.example.guidance.Util.QuestionaireRecyclerViewAdapter;

import java.util.Calendar;
import java.util.Date;

import static com.example.guidance.Util.QuestionaireRecyclerViewAdapter.answers;
import static com.example.guidance.Util.QuestionaireRecyclerViewAdapter.getAmountQuestionsAnswered;
import static com.example.guidance.realm.DatabaseFunctions.insertQuestionnaire;

public class QuestionaireActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionaire);

        recyclerView = findViewById(R.id.recyclerViewQuestionaire);

         question = getResources().getStringArray(R.array.questions);

        QuestionaireRecyclerViewAdapter questionaireRecyclerViewAdapter = new QuestionaireRecyclerViewAdapter(this, question);


        recyclerView.setAdapter(questionaireRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void submit(View view) {



        if(getAmountQuestionsAnswered() == question.length){

            Date currentTime = Calendar.getInstance().getTime();
            insertQuestionnaire(this, question, answers, currentTime);
            finish();
        }else {
            Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show();
        }
    }


}