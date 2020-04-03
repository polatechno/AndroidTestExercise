package com.polatechno.androidtestexercise.model;

import java.util.HashMap;

public class SignalListRequestParams {

    private int tradingsystem;
    private HashMap<String, PairItem> pairs;
    private Long from;
    private Long to;

    public SignalListRequestParams() {
    }

    public SignalListRequestParams(int tradingsystem, HashMap<String, PairItem> pairs, Long from, Long to) {
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

    public void addPair(PairItem pair) {
        pairs.put(pair.getTitle(), pair);
    }



    public HashMap<String, PairItem> getPairs() {
        return pairs;
    }

    public String getPairsString() {
        StringBuilder mapAsString = new StringBuilder("");

        int i = 0;
        for (String key : pairs.keySet()) {

            if (pairs.get(key).isSelected) {

                i++;
                if (i == 1) {
                    mapAsString.append(key);
                } else {
                    mapAsString.append("," + key);
                }

            }

        }

        return mapAsString.toString();
    }

    public void setPairs(HashMap<String, PairItem> pairs) {
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
