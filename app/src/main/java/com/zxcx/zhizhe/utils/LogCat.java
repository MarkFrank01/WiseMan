package com.zxcx.zhizhe.utils;


import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.BuildConfig;

public class LogCat {

	/**
	 * 初始化log工具，在app入口处调用
	 */
	public static void init() {
		FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(2)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
			.tag(App.getContext()
				.getPackageName())   // (Optional) Global tag for every log. Default PRETTY_LOGGER
			.build();

		Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
			@Override
			public boolean isLoggable(int priority, String tag) {
				return BuildConfig.LOG;
			}
		});
	}

	public static void v(String msg) {
		Logger.v(msg);
	}

	public static void v(String msg, Throwable tr) {
		Logger.v(msg, tr);
	}

	public static void d(String msg) {
		Logger.d(msg);
	}

	public static void d(String msg, Throwable tr) {
		Logger.d(msg, tr);
	}

	public static void i(String msg) {
		Logger.i(msg);
	}

	public static void i(String msg, Throwable tr) {
		Logger.i(msg, tr);
	}

	public static void w(String msg) {
		Logger.w(msg);
	}

	public static void w(String msg, Throwable tr) {
		Logger.w(msg, tr);
	}

	public static void e(String msg) {
		Logger.e(msg);
	}

	public static void e(String msg, Throwable tr) {
		Logger.e(msg, tr);
	}

	public static void json(String msg) {
		Logger.json(msg);
	}

	public static void xml(String msg) {
		Logger.xml(msg);
	}
}
