package com.zxcx.shitang;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.meituan.android.walle.WalleChannelReader;
import com.mob.MobSDK;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.zxcx.shitang.ui.MainActivity;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;


public class App extends Application {
    private static Context context;
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = getApplicationContext();

        //SMSSDK,ShareSDK
        MobSDK.init(context, "1e10ca582d273", "09e1ee677112d1e4ebca49138dc38481");

        //Bugly
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setAppChannel(WalleChannelReader.getChannel(context));
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Bugly.init(context, "9e3ef1932f", true, strategy);

        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(context);

        //极光统计
        JAnalyticsInterface.setDebugMode(true);
        JAnalyticsInterface.init(context);

        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight,false);
        if (isNight){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static Context getContext() {
        return context;
    }

}
