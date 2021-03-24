package com.example.guidance.Util;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guidance.R;

import java.util.ArrayList;

/**
 * Created by Conor K on 24/03/2021.
 */


public class RankingRecyclerAdapter extends RecyclerView.Adapter<RankingRecyclerAdapter.ViewHolder> implements
        ItemTouchHelperAdapter {

    private static final String TAG = "NotesRecyclerAdapter";

    private final ArrayList<String> mRanking;
    private final OnRankingListener mOnRankingListener;
    private ItemTouchHelper mTouchHelper;

    public RankingRecyclerAdapter(ArrayList<String> mRanking, OnRankingListener onRankingListener) {
        this.mRanking = mRanking;
        this.mOnRankingListener = onRankingListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ranking_list_item, parent, false);
        return new ViewHolder(view, mOnRankingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {

//            String text = position + ": " + mRanking.get(position);
            String text = mRanking.get(position);
            holder.title.setText(text);

        } catch (NullPointerException e) {
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return mRanking.size();
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String fromNote = mRanking.get(fromPosition);
        mRanking.remove(fromNote);
        mRanking.add(toPosition, fromNote);

        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onItemSwiped(int position) {
        mRanking.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.mTouchHelper = touchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnTouchListener,
            GestureDetector.OnGestureListener {

        TextView timestamp, title;
        OnRankingListener mOnRankingListener;
        GestureDetector mGestureDetector;

        public ViewHolder(View itemView, OnRankingListener onRankingListener) {
            super(itemView);
//            timestamp = itemView.findViewById(R.id.note_timestamp);
            title = itemView.findViewById(R.id.ranking_title);
            mOnRankingListener = onRankingListener;

            mGestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
        }



        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mOnRankingListener.onRankingClick(getAdapterPosition());
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mTouchHelper.startDrag(this);
        }



        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }


    }

    public interface OnRankingListener {
        void onRankingClick(int position);
    }
}