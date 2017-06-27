package com.zxcx.shitang;

import android.app.Application;
import android.content.Context;


public class App extends Application {
    private static Context context;
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
