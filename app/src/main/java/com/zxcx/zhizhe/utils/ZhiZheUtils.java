package com.zxcx.zhizhe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.event.LogoutEvent;
import com.zxcx.zhizhe.event.UserInfoChangeSuccessEvent;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

public class ZhiZheUtils {
    private static final double EARTH_RADIUS = 6378137.0;

    public static void saveLoginData(LoginBean bean) {
        Date date = new Date();
        SharedPreferencesUtil.saveData(SVTSConstants.localTimeStamp, date.getTime());
        SharedPreferencesUtil.saveData(SVTSConstants.serverTimeStamp, bean.getServiceStartTime());
        SharedPreferencesUtil.saveData(SVTSConstants.token, bean.getToken());
        saveUserInfo(bean.getUser());
    }

    public static void saveUserInfo(UserInfoBean bean) {
        SharedPreferencesUtil.saveData(SVTSConstants.userId, bean.getId());
        SharedPreferencesUtil.saveData(SVTSConstants.nickName, bean.getName());
        SharedPreferencesUtil.saveData(SVTSConstants.signture, bean.getSignture());
        SharedPreferencesUtil.saveData(SVTSConstants.sex, bean.getGender());
        SharedPreferencesUtil.saveData(SVTSConstants.birthday, bean.getBirth());
        SharedPreferencesUtil.saveData(SVTSConstants.imgUrl, bean.getAvatar());
        SharedPreferencesUtil.saveData(SVTSConstants.isBindingWX, bean.isBandingWeixin());
        SharedPreferencesUtil.saveData(SVTSConstants.isBindingQQ, bean.isBandingQQ());
        SharedPreferencesUtil.saveData(SVTSConstants.isBindingWB, bean.isBandingWeibo());
        EventBus.getDefault().post(new UserInfoChangeSuccessEvent());
    }

    public static void logout() {
        EventBus.getDefault().post(new LogoutEvent());
        SharedPreferencesUtil.saveData(SVTSConstants.localTimeStamp, (long) 0);
        SharedPreferencesUtil.saveData(SVTSConstants.serverTimeStamp, (long) 0);
        SharedPreferencesUtil.saveData(SVTSConstants.token, "");
        SharedPreferencesUtil.saveData(SVTSConstants.userId, 0);
        SharedPreferencesUtil.saveData(SVTSConstants.nickName, "");
        SharedPreferencesUtil.saveData(SVTSConstants.signture, "");
        SharedPreferencesUtil.saveData(SVTSConstants.birthday, "");
        SharedPreferencesUtil.saveData(SVTSConstants.sex, 0);
        SharedPreferencesUtil.saveData(SVTSConstants.imgUrl, "");
        SharedPreferencesUtil.saveData(SVTSConstants.isBindingWX, false);
        SharedPreferencesUtil.saveData(SVTSConstants.isBindingQQ, false);
        SharedPreferencesUtil.saveData(SVTSConstants.isBindingWB, false);
    }

    public static String getHDImageUrl(String imageUrl) {
        if (getIsHD()){
            return imageUrl;
        }else {
            return imageUrl+"?x-oss-process=image/resize,p_50";
        }
    }

    public static boolean getIsHD() {
        int imageLoadMode = SharedPreferencesUtil.getInt(SVTSConstants.imageLoadMode, 0);
        boolean isWifi = isWifiConnected();
        switch (imageLoadMode){
            case 0:
                return isWifi;
            case 1:
                return true;
            case 2:
                return false;
            default:
                return false;
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