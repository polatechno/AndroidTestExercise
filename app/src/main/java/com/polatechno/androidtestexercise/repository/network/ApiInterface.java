package com.polatechno.androidtestexercise.repository.network;

import com.polatechno.androidtestexercise.model.PartnerAccount;
import com.polatechno.androidtestexercise.model.SignalItem;

import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    //network call for login api...
    @POST("api/Authentication/RequestMoblieCabinetApiToken")
    Call<String> login(@Body PartnerAccount body);



    //network call for get fetch signal with selected instrument pairs...
    @GET("clientmobile/GetAnalyticSignals/{partnerId}")
    Call<List<SignalItem>> getSignalListByInstruments(@Header("passkey") String token,
                                                      @Path(value = "partnerId", encoded = true) String partnerId,
                                                      @QueryMap Map<String, String> paramsMap);




}
