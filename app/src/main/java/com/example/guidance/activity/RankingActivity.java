package com.example.guidance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guidance.R;
import com.example.guidance.ServiceReceiver.onPauseServiceReceiver;
import com.example.guidance.Util.MyItemTouchHelper;
import com.example.guidance.Util.RankingRecyclerAdapter;
import com.example.guidance.Util.Util;
import com.example.guidance.realm.model.Data_Type;
import com.example.guidance.realm.model.Intelligent_Agent;
import com.example.guidance.realm.model.Ranking;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import static com.example.guidance.Util.Util.navigationViewVisibility;
import static com.example.guidance.realm.databasefunctions.DataTypeDatabaseFunctions.getDataType;
import static com.example.guidance.realm.databasefunctions.IntelligentAgentDatabaseFunctions.getIntelligentAgent;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.getRanking;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.getRankingList;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.isRankingInitialised;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.location;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.mood;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.screentime;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.socialness;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.step;
import static com.example.guidance.realm.databasefunctions.RankingDatabaseFunctions.updateRanking;

public class RankingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RankingRecyclerAdapter.OnRankingListener {

    private DrawerLayout drawer;
    private TextInputEditText idealSteps, screentimeLimit;
    private TextInputLayout idealStepsLayout, screentimeLimitLayout;

    //Tag
    private static final String TAG = "RankingActivity";

    // ui components
    private RecyclerView mRecyclerView;

    // vars
    private final ArrayList<String> ranking = new ArrayList<>();
    private RankingRecyclerAdapter rankingRecyclerAdapter;
    private String[] rankingList;
    private final int rankingListSize = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intelligent_Agent intelligent_agent = getIntelligentAgent(this);
        Data_Type dataType = getDataType(this);
        Ranking ranking = getRanking(this);
        //sets the activityTheme to the gender of the intelligent agent, this is done before the onCreate
        //so that the user does not see a flash of one colour as it changes to the other
        Util.setActivityTheme(intelligent_agent, this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);


        drawer = findViewById(R.id.drawer_layout_ranking_activity);
        idealSteps = findViewById(R.id.textInputEditTextidealStepCount);
        idealStepsLayout = findViewById(R.id.textInputLayoutidealStepcount);
        screentimeLimit = findViewById(R.id.textInputEditTextScreenlimit);
        screentimeLimitLayout = findViewById(R.id.textInputLayoutScreenlimit);


        changeVisibility(dataType, ranking);


        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //hides the navigation items that shouldn't be shown
        navigationViewVisibility(navigationView, intelligent_agent, dataType);
        setSupportActionBar(toolbar);

        //sets the toolbar color to gender of the intelligent agent
        Util.setToolbarColor(intelligent_agent, toolbar, getResources());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mRecyclerView = findViewById(R.id.recyclerViewRanking);

        initRecyclerView();
        fillRecyclerView();
    }

    private void changeVisibility(Data_Type dataType, Ranking ranking) {

        if (dataType.isSteps()) {
            idealSteps.setVisibility(View.VISIBLE);
            idealStepsLayout.setVisibility(View.VISIBLE);
            idealSteps.setText(String.valueOf(ranking.getIdealStepCount()));
        }else{
            idealSteps.setVisibility(View.INVISIBLE);
            idealStepsLayout.setVisibility(View.INVISIBLE);
        }

        if (dataType.isScreentime()) {
            screentimeLimit.setVisibility(View.VISIBLE);
            screentimeLimitLayout.setVisibility(View.VISIBLE);
            screentimeLimit.setText(String.valueOf(ranking.getScreentimeLimit()));

        }else{
            screentimeLimit.setVisibility(View.INVISIBLE);
            screentimeLimitLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void fillRecyclerView() {
        ranking.clear();

//        ranking.addAll(Arrays.asList(rankingList));
//        ranking.addAll(Arrays.asList(getRankingList(this, rankingListSize)));
        ranking.addAll(getRankingList(this, rankingListSize));
        rankingRecyclerAdapter.notifyDataSetChanged();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_menu:
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_advice:
                myIntent = new Intent(this, AdviceActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_data:
                myIntent = new Intent(this, DataActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_justification:
                myIntent = new Intent(this, JustificationActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_debug:
                myIntent = new Intent(this, DebugActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_daily_question:
                myIntent = new Intent(this, DailyQuestionActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_ranking:
                myIntent = new Intent(this, RankingActivity.class);
                startActivity(myIntent);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause:");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("onPauseServiceReceiver");
        broadcastIntent.setClass(this, onPauseServiceReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onPause();
    }

    @Override
    public void onRankingClick(int position) {

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        rankingRecyclerAdapter = new RankingRecyclerAdapter(ranking, this);
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(rankingRecyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        rankingRecyclerAdapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(rankingRecyclerAdapter);
    }

    public void saveRankingPreference(View view) {
        boolean rankingInit = isRankingInitialised(this);


        Integer pos_step = null;
        Integer pos_screentime = null;
        Integer pos_location = null;
        Integer pos_socialness = null;
        Integer pos_mood = null;
        Integer pos_idealStepCount = null;
        Integer pos_screentimeLimit = null;

        Data_Type dataType = getDataType(this);

        int number = 0;
        if (dataType.isSteps()) {
            number++;


            pos_idealStepCount = Integer.valueOf(String.valueOf(idealSteps.getText()));
        }
        if (dataType.isScreentime()) {
            pos_screentimeLimit = Integer.valueOf(String.valueOf(screentimeLimit.getText()));

            number++;
        }
        if (dataType.isLocation()) {
            number++;
        }
        if (dataType.isSocialness()) {
            number++;
        }
        if (dataType.isMood()) {
            number++;
        }


        for (int i = 0; i < number; i++) {
            String title = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.ranking_title)).getText().toString();
            switch (title) {
                case step:
                    pos_step = i;
                    break;
                case screentime:
                    pos_screentime = i;
                    break;
                case location:
                    pos_location = i;
                    break;
                case socialness:
                    pos_socialness = i;
                    break;
                case mood:
                    pos_mood = i;
                    break;
            }

        }

//        Log.d(TAG, "pos_step: " + pos_step);
//        Log.d(TAG, "pos_screentime: " + pos_screentime);
//        Log.d(TAG, "pos_location: " + pos_location);
//        Log.d(TAG, "pos_socialness: " + pos_socialness);
//        Log.d(TAG, "pos_mood: " + pos_mood);



        if (rankingInit) {
            updateRanking(this, pos_step, pos_location, pos_screentime, pos_socialness, pos_mood, pos_idealStepCount, pos_screentimeLimit);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Data_Type dataType = getDataType(this);
        Ranking ranking = getRanking(this);

        changeVisibility(dataType, ranking);

        fillRecyclerView();


    }
}