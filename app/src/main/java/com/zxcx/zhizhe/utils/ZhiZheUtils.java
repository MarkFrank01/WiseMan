package com.zxcx.zhizhe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;

import java.util.Date;

public class ZhiZheUtils {
    private static final double EARTH_RADIUS = 6378137.0;

    public static void saveLoginData(LoginBean bean) {
        Date date = new Date();
        SharedPreferencesUtil.saveData(SVTSConstants.localTimeStamp, date.getTime());
        SharedPreferencesUtil.saveData(SVTSConstants.serverTimeStamp, bean.getServiceStartTime());
        SharedPreferencesUtil.saveData(SVTSConstants.token, bean.getToken());
        SharedPreferencesUtil.saveData(SVTSConstants.userId, bean.getUser().getId());
        SharedPreferencesUtil.saveData(SVTSConstants.nickName, bean.getUser().getName());
        SharedPreferencesUtil.saveData(SVTSConstants.sex, bean.getUser().getGender());
        SharedPreferencesUtil.saveData(SVTSConstants.birthday, bean.getUser().getBirth());
    }

    public static String getHDImageUrl(String imageUrl) {
        if (getIsHD()){
            return imageUrl;
        }else {
            return imageUrl+"?x-oss-process=image/resize,p_50";
        }
    }

    public static boolean getIsHD() {
        boolean isOnlyWifi = SharedPreferencesUtil.getBoolean(SVTSConstants.isOnlyWifi,false);
        boolean isWifi = isWifiConnected();
        if (isOnlyWifi && !isWifi){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 判断WIFI网络是否可用
     * */
    public static boolean isWifiConnected() {
        if (App.getContext() != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为WIFI
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return networkInfo.isAvailable();
        }
        return false;
    }

}