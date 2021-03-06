package com.zxcx.zhizhe;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kingja.loadsir.core.LoadSir;
import com.meituan.android.walle.WalleChannelReader;
import com.mob.MobSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuch.adlibrary.utils.DisplayUtil;
import com.zxcx.zhizhe.loadCallback.EmptyCallback;
import com.zxcx.zhizhe.loadCallback.LoadingCallback;
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.ui.MainActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.LogCat;
import com.zxcx.zhizhe.widget.DefaultRefreshHeader;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;


public class App extends Application {
	
	public static App app;
	private static Context context;
	
	static {
		//设置全局的Header构建器
		SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
			
			layout.setEnableHeaderTranslationContent(false);
			layout.setHeaderHeight(80);
			return new DefaultRefreshHeader(
				context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
		});
		//设置全局的Footer构建器
		
	}
	
	private RefWatcher refWatcher;
	
	public static Context getContext() {
		return context;
	}
	
	public static RefWatcher getRefWatcher(Context context) {
		App application = (App) context.getApplicationContext();
		return application.refWatcher;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		context = getApplicationContext();
		
		//SMSSDK,ShareSDK
		MobSDK.init(this);
		
		//Bugly
		CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
		strategy.setAppChannel(WalleChannelReader.getChannel(context));
		Beta.canShowUpgradeActs.add(MainActivity.class);
		Bugly.init(context, "9e3ef1932f", BuildConfig.DEBUG, strategy);
		
		//极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(context);
		
		//极光统计
		JAnalyticsInterface.setDebugMode(true);
		JAnalyticsInterface.init(context);
		
		//LeakCanary
		refWatcher = LeakCanary.install(this);
		
		//夜间模式
		if (Constants.IS_NIGHT) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		} else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}
		
		//LoadSir
		LoadSir.beginBuilder()
			.addCallback(new NetworkErrorCallback())
			.addCallback(new LoginTimeoutCallback())
			.addCallback(new EmptyCallback())
			.addCallback(new LoadingCallback())
			.setDefaultCallback(LoadingCallback.class)
			.commit();
		
		//Logger初始化
		LogCat.init();

        Fresco.initialize(this);
        initDisplayOpinion();
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		// you must install multiDex whatever tinker is installed!
		MultiDex.install(base);
		
		// 安装tinker
		Beta.installTinker();
	}

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }
}
