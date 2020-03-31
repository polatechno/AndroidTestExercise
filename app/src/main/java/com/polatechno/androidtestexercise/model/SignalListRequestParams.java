package com.polatechno.androidtestexercise.model;

import java.util.HashMap;

public class SignalListRequestParams {

    private int tradingsystem;
    private HashMap<String, String> pairs;
    private Long from;
    private Long to;

    public SignalListRequestParams() {
    }

    public SignalListRequestParams(int tradingsystem, HashMap<String, String> pairs, Long from, Long to) {
        this.tradingsystem = tradingsystem;
        this.pairs = pairs;
        this.from = from;
        this.to = to;
    }

    public int getTradingsystem() {
        return tradingsystem;
    }

    public void setTradingsystem(int tradingsystem) {
        this.tradingsystem = tradingsystem;
    }

    public void addPair(String pair) {
        pairs.put(pair, pair);
    }

    public void removePair(String pair) {

        String returned_value;
        for (String key : pairs.keySet()) {

            if (key.equals(pair)) {
                returned_value = (String) pairs.remove(key);
                break;
            }

        }
    }

    public String getPairs() {
        StringBuilder mapAsString = new StringBuilder("");

        int count = pairs.size();

        int i = 1;
        for (String key : pairs.keySet()) {

            if (i == count) {
                mapAsString.append(key);
            } else {
                mapAsString.append(key + ",");
            }

            i++;
        }

        return mapAsString.toString();
    }

    public void setPairs(HashMap<String, String> pairs) {
        this.pairs = pairs;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }
}
