package com.example.guidance.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guidance.R;

import java.util.Arrays;

/**
 * Created by Conor K on 09/03/2021.
 */
public class QuestionaireRecyclerViewAdapter extends RecyclerView.Adapter<QuestionaireRecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = "QuestionaireRecyclerViewAdapter";

    Context context;
    static String[] questions;
    static String[] answers;
    private static int questions_answered;
    Resources resources;
    String[][] array;

    public QuestionaireRecyclerViewAdapter(Context ct, String[] qs, TypedArray question_answers, Resources recs) {
        context = ct;
        questions = qs;
        answers = new String[qs.length];
        questions_answered = 0;
        resources = recs;

        int n = question_answers.length();
        array = new String[n][];

        //
        for (int i = 0; i < n; ++i) {
            int id = question_answers.getResourceId(i, 0);
            if (id > 0) {
                array[i] = resources.getStringArray(id);
            } else {
                // something wrong with the XML
                Log.d(TAG, "QuestionaireRecyclerViewAdapter: error");
            }
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Displays the question from the appropriate position
        // in the questions String-Array from Strings.xml
        holder.question.setText(questions[position]);
        holder.radioGroupAnswer.setOrientation(LinearLayout.HORIZONTAL);

        //TODO potentially change the radioButton's textsize

        //Dynamically populates the question's radiogroup with the answers to the question
        for (String fs : array[position]) {
            //Creates a new radio button
            RadioButton rb = new RadioButton(context);
            //Sets the text of the radio button to an answer option from the appropirate
            //answer_ String-Array
            rb.setText(fs);
            //adds the radio button to the radiogroup
            holder.radioGroupAnswer.addView(rb);
        }


        //When a answer is selected
        holder.radioGroupAnswer.setOnCheckedChangeListener((group, checkedId) -> {

            //If no radiobutton in the radiogroup was previously selected then
            //the answers[position] will == null, then increments the questions_answered so that
            //the amount of questions_answered will equal the amount of questions answered
            if (answers[position] == null) {
                questions_answered++;
            }


            View radioButton = group.findViewById(checkedId);
            int radioId = group.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) group.getChildAt(radioId);
            String selection = (String) btn.getText();

            //sets the answer[position] to equal the text of the selection
            answers[position] = selection;

//            Log.d(TAG, "onBindViewHolder: " + selection);
        });

    }


    @Override
    public int getItemCount() {
        return questions.length;
    }


    public static int getAmountQuestionsAnswered() {
        return questions_answered;
    }

    public static String[] getAnswers() {
        return answers;
    }

    public static String[] getQuestions() {
        return questions;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView question;
        RadioGroup radioGroupAnswer;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.textViewQuestion);
            radioGroupAnswer = itemView.findViewById(R.id.radioGroupQuestionaire);
        }
    }
}
