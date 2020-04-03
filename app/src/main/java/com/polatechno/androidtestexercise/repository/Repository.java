package com.polatechno.androidtestexercise.repository;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.polatechno.androidtestexercise.AppUtils.MyHelperMethods;
import com.polatechno.androidtestexercise.model.PairItem;
import com.polatechno.androidtestexercise.model.PartnerAccount;
import com.polatechno.androidtestexercise.model.SignalItem;
import com.polatechno.androidtestexercise.model.SignalListRequestParams;
import com.polatechno.androidtestexercise.model.SignalListResponse;
import com.polatechno.androidtestexercise.repository.network.ApiClient;
import com.polatechno.androidtestexercise.repository.network.ApiInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private Application application;

    private static final String TAG = "SignalRepository";

    private SignalListResponse signalApiResponse;
    private MutableLiveData<SignalListResponse> liveSignalListResponse;

    private MutableLiveData<SignalListRequestParams> liveSignalListRequestParams;
    private SignalListRequestParams localRequestParams;

    private MutableLiveData<PartnerAccount> livePartnerAccount;
    private PartnerAccount localPartnerAccount;

    public Repository(Application app) {
        this.application = app;

        livePartnerAccount = new MutableLiveData<>();
        liveSignalListRequestParams = new MutableLiveData<>();

        initUser();
        initRequestParams();

        signalApiResponse = new SignalListResponse();
        liveSignalListResponse = new MutableLiveData<>();
    }

    //sets authorized user info
    private void initUser() {
        Gson gson = new Gson();
        localPartnerAccount = MyHelperMethods.getLoggedInUser(application, gson);
        livePartnerAccount.postValue(localPartnerAccount);
    }

    //sets initial params
    private void initRequestParams() {

        localRequestParams = new SignalListRequestParams();
        HashMap<String, PairItem> mapInitialPairs = new HashMap<>();

        //Preloading available instruments, to fetch all available.
        //EURUSD, GBPUSD, USDJPY, USDCHF, USDCAD, AUDUSD, NZDUSD
        mapInitialPairs.put("EURUSD", new PairItem("EURUSD", true));
        mapInitialPairs.put("GBPUSD", new PairItem("GBPUSD", true));
        mapInitialPairs.put("USDJPY", new PairItem("USDJPY", true));
        mapInitialPairs.put("USDCHF", new PairItem("USDCHF", true));
        mapInitialPairs.put("USDCAD", new PairItem("USDCAD", true));
        mapInitialPairs.put("AUDUSD", new PairItem("AUDUSD", true));
        mapInitialPairs.put("NZDUSD", new PairItem("NZDUSD", true));

        //initializing request query params. In my case, only instruments are dynamic. Fixed time range. Fixed trading system. And for this authenticated user.
        localRequestParams.setTradingsystem(3);
        localRequestParams.setPairs(mapInitialPairs);
        localRequestParams.setFrom((long) 1479860023);
        localRequestParams.setTo((long) 1480066860);

        liveSignalListRequestParams.setValue(localRequestParams);
    }

    //Returns latest request params to use inside this class
    private SignalListRequestParams getCurrentRequestParams() {
        return liveSignalListRequestParams.getValue();
    }

    //Returns latest live data requestParams
    public MutableLiveData<SignalListRequestParams> getRequestParamsLiveData() {
        return liveSignalListRequestParams;
    }

    //Updates queryParams on intrument pair item status change
    public void refreshRequestParams(String key, boolean isSelected) {

        SignalListRequestParams requestParams = getCurrentRequestParams();
        requestParams.getPairs().get(key).setSelected(isSelected);
        liveSignalListRequestParams.setValue(requestParams);
    }

    //Sending live data viewModel
    public MutableLiveData<SignalListResponse> getSignalsLiveData() {

        SignalListRequestParams signalListRequestParams = getCurrentRequestParams();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        //set paramsMap for request
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("tradingsystem", String.valueOf(signalListRequestParams.getTradingsystem()));
        paramsMap.put("pairs", signalListRequestParams.getPairsString());
        paramsMap.put("from", String.valueOf(signalListRequestParams.getFrom()));
        paramsMap.put("to", String.valueOf(signalListRequestParams.getTo()));

        Call<List<SignalItem>> apiCall = apiService.getSignalListByInstruments(localPartnerAccount.getPasskey(), localPartnerAccount.getLogin(), paramsMap);

        signalApiResponse.setLoading(true);
        liveSignalListResponse.postValue(signalApiResponse);

        apiCall.enqueue(new Callback<List<SignalItem>>() {
            @Override
            public void onResponse(Call<List<SignalItem>> call, Response<List<SignalItem>> response) {

                Log.d(TAG, "onResponse: " + response.body());

                signalApiResponse.setOnResponse(true);
                signalApiResponse.setLoading(false);
                signalApiResponse.setStatusCode(response.code());

                if (response.code() == 200) {
                    signalApiResponse.setSignalItems(response.body());
                }

                liveSignalListResponse.postValue(signalApiResponse);
            }

            @Override
            public void onFailure(Call<List<SignalItem>> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getMessage());

                signalApiResponse.setLoading(false);
                signalApiResponse.setOnResponse(false);
                signalApiResponse.setMessage("Error: " + t.getMessage());

                liveSignalListResponse.postValue(signalApiResponse);
            }
        });

        return liveSignalListResponse;
    }

}
