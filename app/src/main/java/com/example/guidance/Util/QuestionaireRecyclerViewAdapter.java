package com.example.guidance.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guidance.R;

/**
 * Created by Conor K on 09/03/2021.
 */
public class QuestionaireRecyclerViewAdapter extends RecyclerView.Adapter<QuestionaireRecyclerViewAdapter.MyViewHolder>{

    private static final String TAG = "QuestionaireRecyclerViewAdapter";

    Context context;
    String[] questions;
    public static int[] answers;
    private static int questions_answered;

    public QuestionaireRecyclerViewAdapter(Context ct, String[] qs) {
        context = ct;
        questions = qs;
        answers = new int[qs.length];
        questions_answered = 0;
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

        holder.question.setText(questions[position]);

        //TODO answer implemention that is not numbered 1-4
        holder.answer.setOnCheckedChangeListener((group, checkedId) -> {
//            int f=0;
//            for(int an: answers){
//                f++;
//                Log.d(TAG, "onBindViewHolder: " + f + " " + an);
//            }

            if(answers[position] == 0){
                questions_answered++;
            }

            switch (checkedId) {
                case R.id.radioButtonQuestionaire1:
                    answers[position] = 1;
                    break;
                case R.id.radioButtonQuestionaire2:
                    answers[position] = 2;

                    break;
                case R.id.radioButtonQuestionaire3:
                    answers[position] = 3;

                    break;
                case R.id.radioButtonQuestionaire4:
                    answers[position] = 4;

                    break;
            }


            Log.d(TAG, "answer selected: " + answers[position]);
        });


    }

    @Override
    public int getItemCount() {
        return questions.length;
    }

    public static int getAmountQuestionsAnswered() {
        return questions_answered;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView question;
        RadioGroup answer;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.textViewQuestion);
            answer = itemView.findViewById(R.id.radioGroupQuestionaire);


        }



    }


}
