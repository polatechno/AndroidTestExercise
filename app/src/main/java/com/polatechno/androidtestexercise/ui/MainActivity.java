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
import com.polatechno.androidtestexercise.adapter.SignalListAdapter;
import com.polatechno.androidtestexercise.model.PartnerAccount;
import com.polatechno.androidtestexercise.model.SignalItem;
import com.polatechno.androidtestexercise.model.SignalListRequestParams;
import com.polatechno.androidtestexercise.network.ApiClient;
import com.polatechno.androidtestexercise.network.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private SignalListRequestParams signalListRequestParams = new SignalListRequestParams();
    private HashMap<String, String> mapInitialPairs = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_signals);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.signal_list_title);
        }

        gson = new Gson();

        //checks from the cache, if user is authenticated. if not, will go to LoginActivity
        partnerUser = MyHelperMethods.getLoggedInUser(this, gson);
        if (partnerUser == null) {
            goToLoginActivity();
        }


        //Preloading available instruments, to fetch all available.
        //EURUSD, GBPUSD, USDJPY, USDCHF, USDCAD, AUDUSD, NZDUSD
        mapInitialPairs.put("EURUSD", "EURUSD");
        mapInitialPairs.put("GBPUSD", "GBPUSD");
        mapInitialPairs.put("USDJPY", "USDJPY");
        mapInitialPairs.put("USDCHF", "USDCHF");
        mapInitialPairs.put("USDCAD", "USDCAD");
        mapInitialPairs.put("AUDUSD", "AUDUSD");
        mapInitialPairs.put("NZDUSD", "NZDUSD");

        //initializing request query params. In my case, only instruments are dynamic. Fixed time range. Fixed trading system. And for this authenticated user.
        signalListRequestParams.setTradingsystem(3);
        signalListRequestParams.setPairs(mapInitialPairs);
        signalListRequestParams.setFrom((long) 1479860023);
        signalListRequestParams.setTo((long) 1480066860);

        initViews();
        setUpRecyclerView();
        populateIntrumentsChipGroup();
        getSignalListByInstruments();
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
                        getSignalListByInstruments();
                    }
                }
        );

    }

    //Showing instrument pairs. Initially all selected. Then it fetches dynamically.
    private void populateIntrumentsChipGroup() {

        chipGroupInstruments = findViewById(R.id.chipGroupInstruments);

        Chip eachChipView;
        for (String key : mapInitialPairs.keySet()) {

            eachChipView = new Chip(this, null, R.attr.CustomChipChoiceStyle);

            eachChipView.setText(key);
            eachChipView.setChecked(true);
            eachChipView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        signalListRequestParams.addPair(compoundButton.getText().toString());
                    } else {
                        signalListRequestParams.removePair(compoundButton.getText().toString());
                    }

                    getSignalListByInstruments();

                    Log.d(TAG, "onCheckedChanged: new status : " + b + " Text " + compoundButton.getText().toString());
                }
            });

            chipGroupInstruments.addView((View) eachChipView);


        }
    }

    //Handling retry call shows on the screen
    public void onRetryClick(View view) {
        stub.setVisibility(View.GONE);
        getSignalListByInstruments();
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);

        mSignalsRecView = findViewById(R.id.mRecyclerView);
        mSignalsRecView.setLayoutManager(mLayoutManager);
        mSignalsRecView.setItemAnimator(new DefaultItemAnimator());
        mSignalListAdapter = new SignalListAdapter(
                getApplicationContext(),
                mSignals);
        mSignalsRecView.setAdapter(mSignalListAdapter);
    }

    private void getSignalListByInstruments() {

        tv_empty_message.setVisibility(View.GONE);
        stub.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mSignalsRecView.setVisibility(View.GONE);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        //set paramsMap for request
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("tradingsystem", String.valueOf(signalListRequestParams.getTradingsystem()));
        paramsMap.put("pairs", signalListRequestParams.getPairs());
        paramsMap.put("from", String.valueOf(signalListRequestParams.getFrom()));
        paramsMap.put("to", String.valueOf(signalListRequestParams.getTo()));

        Call<List<SignalItem>> apiCall = apiService.getSignalListByInstruments(partnerUser.getPasskey(), partnerUser.getLogin(), paramsMap);

        apiCall.enqueue(new Callback<List<SignalItem>>() {
            @Override
            public void onResponse(Call<List<SignalItem>> call, Response<List<SignalItem>> response) {

                mSignalsRecView.setVisibility(View.VISIBLE);

                progressBar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);

                //If respose success, if any list data received.
                if (response.code() == 200) {
                    mSignalListAdapter.addItems(response.body());

                    //If empty result, show empty message
                    if (mSignalListAdapter.getItemCount() == 0) {
                        tv_empty_message.setVisibility(View.VISIBLE);
                    }


                    //if user token is not valid. User should login again...
                } else if (response.code() == 403) {
                    logOutUser();
                }

            }

            @Override
            public void onFailure(Call<List<SignalItem>> call, Throwable t) {

                Log.d(TAG, "onFailure: " + call.toString());

                Log.i("onFailure", "Retrofit OnFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();

                mSignalsRecView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                stub.setVisibility(View.VISIBLE);
            }
        });
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
