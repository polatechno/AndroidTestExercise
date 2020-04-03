package com.polatechno.androidtestexercise.model;

import com.polatechno.androidtestexercise.model.SignalItem;

import java.util.List;

public class SignalListResponse {

    boolean isLoading;
    int statusCode;
    boolean isOnResponse;
    String message;
    List<SignalItem> signalItems;

    public SignalListResponse() {
    }

    public SignalListResponse(boolean isLoading, int statusCode, boolean isOnResponse, String message, List<SignalItem> signalItems) {
        this.isLoading = isLoading;
        this.statusCode = statusCode;
        this.isOnResponse = isOnResponse;
        this.message = message;
        this.signalItems = signalItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isOnResponse() {
        return isOnResponse;
    }

    public void setOnResponse(boolean onResponse) {
        isOnResponse = onResponse;
    }

    public List<SignalItem> getSignalItems() {
        return signalItems;
    }

    public void setSignalItems(List<SignalItem> signalItems) {
        this.signalItems = signalItems;
    }
}
