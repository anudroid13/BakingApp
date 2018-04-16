package com.darwinbox.bakingapp;

import android.app.Application;
import android.content.Context;

public class AppController extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Context getmContext() {
        return mContext.getApplicationContext();
    }
}
