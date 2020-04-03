package com.polatechno.androidtestexercise.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.polatechno.androidtestexercise.AppUtils.MyHelperMethods;
import com.polatechno.androidtestexercise.R;
import com.polatechno.androidtestexercise.ui.adapter.SignalListAdapter;
import com.polatechno.androidtestexercise.model.PartnerAccount;
import com.polatechno.androidtestexercise.model.SignalItem;
import com.polatechno.androidtestexercise.model.SignalListRequestParams;
import com.polatechno.androidtestexercise.model.SignalListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //UI views
    private RecyclerView mSignalsRecView;
    private ViewStub stub;
    private TextView tv_empty_message;
    private ChipGroup chipGroupInstruments;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeToRefresh;

    //Data variables
    private List<SignalItem> mSignals = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private SignalListAdapter mSignalListAdapter;
    private Gson gson;
    private PartnerAccount partnerUser;
    private HashMap<String, String> mapInitialPairs = new HashMap<>();
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_signals);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.signal_list_title);
        }

        initViews();
        setUpRecyclerView();

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getRequestParams();
        mainViewModel.getSignals();

        setModelViewObserver();
        setQueryParamsObserver();


//        getSignalListByInstruments();
    }

    private void setQueryParamsObserver() {
        mainViewModel.signalListRequestParamsMutableLiveData.observe(this, new Observer<SignalListRequestParams>() {
            @Override
            public void onChanged(SignalListRequestParams signalListRequestParams) {

                chipGroupInstruments.removeAllViews();

                Chip eachChipView;
                for (String key : signalListRequestParams.getPairs().keySet()) {

                    eachChipView = new Chip(MainActivity.this, null, R.attr.CustomChipChoiceStyle);

                    eachChipView.setText(key);
                    eachChipView.setChecked(signalListRequestParams.getPairs().get(key).isSelected());
                    eachChipView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            mainViewModel.handlePairStatusChange(compoundButton.getText().toString(), b);

                            Log.d(TAG, "onCheckedChanged: new status : " + b + " Text " + compoundButton.getText().toString());

                        }
                    });

                    chipGroupInstruments.addView((View) eachChipView);

                }


            }
        });

    }

    private void setModelViewObserver() {

        mainViewModel.signalListResponse.observe(this, new Observer<SignalListResponse>() {
            @Override
            public void onChanged(SignalListResponse signalListResponse) {

                Log.d(TAG, "onChanged: Live data changed... ");

                if (signalListResponse.isLoading()) {

                    Log.d(TAG, "onChanged: Live data changed... isLoading = True ");

                    tv_empty_message.setVisibility(View.GONE);
                    stub.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    mSignalsRecView.setVisibility(View.GONE);

                } else {

                    Log.d(TAG, "onChanged: Live data changed... isLoading = false ");

                    //onResponse
                    if (signalListResponse.isOnResponse()) {


                        Log.d(TAG, "onChanged: Live data changed... status:  OnResponse ");

                        mSignalsRecView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        swipeToRefresh.setRefreshing(false);

                        if (signalListResponse.getStatusCode() == 200) {
                            mSignalListAdapter.addItems(signalListResponse.getSignalItems());

                            Log.d(TAG, "onChanged: statusCode = 200");

                            //If empty result, show empty message
                            if (signalListResponse.getSignalItems().size() == 0) {
                                tv_empty_message.setVisibility(View.VISIBLE);
                            }

                            //if user token is not valid. User should login again...
                        } else if (signalListResponse.getStatusCode() == 403) {
                            Log.d(TAG, "onChanged: statusCode = 403");
                            logOutUser();
                        }


                    }
                    //onFailure
                    else {

                        Log.d(TAG, "onChanged: Live data changed... status:  onFailure " + signalListResponse.getMessage());

                        Log.i("onFailure", "Retrofit OnFailure: " + signalListResponse.getMessage());
                        Toast.makeText(getApplicationContext(), "Unable to fetch json: " + signalListResponse.getMessage(), Toast.LENGTH_LONG).show();

                        mSignalsRecView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        swipeToRefresh.setRefreshing(false);
                        stub.setVisibility(View.VISIBLE);

                    }

                }

            }
        });

    }

    private void initViews() {


        stub = findViewById(R.id.stub_no_internet);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        tv_empty_message = findViewById(R.id.tv_empty_message);

        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeToRefresh.setRefreshing(false);
                        mainViewModel.handleForceRefresh();

//                        getSignalListByInstruments();
//                        getSignalsLiveData();
                    }
                }
        );

        chipGroupInstruments = findViewById(R.id.chipGroupInstruments);

    }

    //Handling retry call shows on the screen
    public void onRetryClick(View view) {
        stub.setVisibility(View.GONE);
        mainViewModel.handleForceRefresh();
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);

        mSignalsRecView = findViewById(R.id.mRecyclerView);
        mSignalsRecView.setLayoutManager(mLayoutManager);
        mSignalsRecView.setItemAnimator(new DefaultItemAnimator());
        mSignalListAdapter = new SignalListAdapter(
                getApplicationContext(), new ArrayList<SignalItem>());
        mSignalsRecView.setAdapter(mSignalListAdapter);
    }

    private void goToLoginActivity() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void logOutUser() {
        MyHelperMethods.logOutUser(this);
        goToLoginActivity();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (id == R.id.action_logout) {
            logOutUser();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
