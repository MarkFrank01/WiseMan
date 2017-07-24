package com.zxcx.shitang;

import android.app.Application;
import android.content.Context;

import com.meituan.android.walle.WalleChannelReader;
import com.mob.MobSDK;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.zxcx.shitang.ui.MainActivity;


public class App extends Application {
    private static Context context;
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = getApplicationContext();

        MobSDK.init(context, "1e10ca582d273", "09e1ee677112d1e4ebca49138dc38481");

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setAppChannel(WalleChannelReader.getChannel(context));
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Bugly.init(context, "9e3ef1932f", true,strategy);
    }

    public static Context getContext() {
        return context;
    }

}
