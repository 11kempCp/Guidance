package com.example.guidance.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Conor K on 10/02/2021.
 */

    //https://www.youtube.com/watch?v=18VcnYN5_LM
    // Implemented following the above tutorial
    // Dynamically creates the switches for the DataActivity,
    // allows more data types to be added without needing to constantly remake DataActivity
public class DataRecyclerViewAdapter extends RecyclerView.Adapter<DataRecyclerViewAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public DataRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DataRecyclerViewAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
