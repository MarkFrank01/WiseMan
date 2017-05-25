package com.zxcx.shitang;

import android.app.Application;
import android.content.Context;


/**
 * User: Picasso
 * Date: 2016-01-12
 * Time: 14:43
 * FIXME
 */
public class App extends Application {
    private static Context context;
    public static App app;
    public static final String UNIONPAY = "00";
    public static String URL;
    public static String DOMAIN;
    public static String URL_BASE ;
    public static String URL_pic;
    //public static final String URL_BASE = "http://192.168.1.113:8080/lxt/app/";
    //"http://www.smlxt.com/lxt/app/";
    //public static final String URL_BASE = "http://120.24.37.56:8000/lxt/app/";
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
