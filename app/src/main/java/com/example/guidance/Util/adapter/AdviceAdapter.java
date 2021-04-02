package com.example.guidance.Util.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guidance.R;
import com.example.guidance.realm.model.Advice;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.RealmResults;

/**
 * Created by Conor K on 01/04/2021.
 */
public class AdviceAdapter extends RecyclerView.Adapter<AdviceAdapter.MyViewHolder> {

    Context ct;
    Advice[] ad;
    Resources resources;
    private String TAG = "AdviceAdapter";
    Integer colour;
    boolean todayAdvice;


    public AdviceAdapter(Context context, RealmResults<Advice> advice, Resources recs, int clr, boolean tAdvice){
        ct = context;
        ad = advice.toArray(new Advice[0]);
        resources = recs;
        colour = clr;
        todayAdvice = tAdvice;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ct);

        View view = inflater.inflate(R.layout.advice_my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.advice.setText(ad[position].getAdvice());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
//        Log.d(TAG, "onBindViewHolder: date is " + ad[position].getDateTimeAdviceFor());
        String dateFormat = simpleDateFormat.format(ad[position].getDateTimeAdviceFor());
//        Log.d(TAG, "onBindViewHolder: dateFormat " + dateFormat);

        String date = resources.getString(R.string.date_advice_for) + dateFormat;
        holder.date.setText(date);

        if(todayAdvice){
            holder.cardView.setCardBackgroundColor(colour);
        }


    }

    @Override
    public int getItemCount() {
        return ad.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView advice, date;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            advice = itemView.findViewById(R.id.textViewMyRowAdvice);
            date = itemView.findViewById(R.id.textViewMyRowDateFor);
            cardView = itemView.findViewById(R.id.cardViewAdvice);

        }
    }

}
