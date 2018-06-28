package com.zxcx.zhizhe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by anm on 2017/8/8.
 */

public class SystemUtils {

	public static boolean isProessRunning(Context context, String proessName) {

		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo info : lists) {
			if (info.processName.equals(proessName)) {
				//Log.i("Service2进程", ""+info.processName);
				isRunning = true;
			}
		}

		return isRunning;
	}
}
