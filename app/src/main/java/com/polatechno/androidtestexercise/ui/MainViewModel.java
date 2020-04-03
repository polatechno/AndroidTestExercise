package com.polatechno.androidtestexercise.ui;

import com.polatechno.androidtestexercise.MyApplication;
import com.polatechno.androidtestexercise.model.SignalListRequestParams;
import com.polatechno.androidtestexercise.repository.Repository;
import com.polatechno.androidtestexercise.model.SignalListResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    MutableLiveData<SignalListResponse> signalListResponse;
    MutableLiveData<SignalListRequestParams> signalListRequestParamsMutableLiveData;

    private Repository repository;

    public MainViewModel() {
        repository = new Repository(MyApplication.getInstance());
    }

    //Provides recyclerview data
    public LiveData<SignalListResponse> getSignals() {

        if (signalListResponse == null) {
            signalListResponse = repository.getSignalsLiveData();
        }
        return signalListResponse;
    }

    //Provides request query params to show chip group items
    public LiveData<SignalListRequestParams> getRequestParams() {

        if (signalListRequestParamsMutableLiveData == null) {
            signalListRequestParamsMutableLiveData = repository.getRequestParamsLiveData();
        }
        return signalListRequestParamsMutableLiveData;
    }

    //Updates pairs data
    public void handlePairStatusChange(String key, boolean isSelected) {

        repository.refreshRequestParams(key, isSelected);
        repository.getSignalsLiveData();
    }

    public void handleForceRefresh() {
        repository.getSignalsLiveData();
    }


}
















