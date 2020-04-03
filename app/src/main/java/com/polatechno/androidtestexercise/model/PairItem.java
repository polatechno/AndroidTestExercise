package com.polatechno.androidtestexercise.model;

public class PairItem {
    String title;
    boolean isSelected;

    public PairItem() {
    }

    public PairItem(String title, boolean isSelected) {
        this.title = title;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
