package com.zxcx.zhizhe.service.version_update.update_utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AppUtils {

    /**
     * 获取当前应用的版本号
     *
     * @param mContext context
     * @return 版本号
     */
    public static int getVersionCode(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }
        return 0;
    }

    /**
     * 获取当前应用的版本名称
     *
     * @param mContext context
     * @return 版本名称
     */
    public static String getVersionName(Context mContext) {
        if (mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }

        return "";
    }

    // 获取渠道号
    public static String getChannel(Context mContext) {
        String channel = "";
        try {
            ApplicationInfo info = mContext.getPackageManager().
                    getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null) {
                String metaData = info.metaData.getString("CP_CHANNEL");

                channel = metaData;

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }

}
