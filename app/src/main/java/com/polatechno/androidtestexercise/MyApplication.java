package com.polatechno.androidtestexercise;

import android.app.Application;

public class MyApplication extends Application {

    // Singleton instance
    private static MyApplication sInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // Setup singleton instance
        sInstance = this;
    }

    // Getter to access Singleton instance
    public static MyApplication getInstance() {
        return sInstance ;
    }


}