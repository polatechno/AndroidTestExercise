package com.polatechno.androidtestexercise.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class SignalItem implements Serializable {

    private long Id;
    private long ActualTime;
    private String Comment;
    private String Pair;
    private long Cmd;
    private long TradingSystem;
    private String Period;
    private BigDecimal Price;
    private BigDecimal Sl;
    private BigDecimal Tp;


    public SignalItem() {
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getActualTime() {
        return ActualTime;
    }

    public void setActualTime(long actualTime) {
        ActualTime = actualTime;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getPair() {
        return Pair;
    }

    public void setPair(String pair) {
        Pair = pair;
    }

    public long getCmd() {
        return Cmd;
    }

    public void setCmd(long cmd) {
        Cmd = cmd;
    }

    public long getTradingSystem() {
        return TradingSystem;
    }

    public void setTradingSystem(long tradingSystem) {
        TradingSystem = tradingSystem;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal price) {
        Price = price;
    }

    public BigDecimal getSl() {
        return Sl;
    }

    public void setSl(BigDecimal sl) {
        Sl = sl;
    }

    public BigDecimal getTp() {
        return Tp;
    }

    public void setTp(BigDecimal tp) {
        Tp = tp;
    }
}
