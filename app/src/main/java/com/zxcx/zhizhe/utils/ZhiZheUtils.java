package com.zxcx.zhizhe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;

import com.zxcx.zhizhe.App;
import com.zxcx.zhizhe.event.LogoutEvent;
import com.zxcx.zhizhe.event.UserInfoChangeSuccessEvent;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.my.creation.ApplyReviewActivity;
import com.zxcx.zhizhe.ui.my.creation.CreationAgreementDialog;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zxcx.zhizhe.ui.my.MyTabBeanKt.writer_status_review;
import static com.zxcx.zhizhe.ui.my.MyTabBeanKt.writer_status_writer;

/**
 * 项目相关操作封装
 */

public class ZhiZheUtils {
	
	private static final double EARTH_RADIUS = 6378137.0;
	
	public static void saveLoginData(LoginBean bean) {
		Date date = new Date();
		SharedPreferencesUtil.saveData(SVTSConstants.localTimeStamp, date.getTime());
		SharedPreferencesUtil.saveData(SVTSConstants.serverTimeStamp, bean.getServiceStartTime());
		SharedPreferencesUtil.saveData(SVTSConstants.token, bean.getToken());
		SharedPreferencesUtil.saveData(SVTSConstants.writerStatus, bean.getWriterStatus());
		saveUserInfo(bean.getUser());
	}
	
	public static void saveUserInfo(UserInfoBean bean) {
		SharedPreferencesUtil.saveData(SVTSConstants.userId, bean.getId());
		SharedPreferencesUtil.saveData(SVTSConstants.nickName, bean.getName());
		SharedPreferencesUtil.saveData(SVTSConstants.phone, bean.getPhoneNum());
		SharedPreferencesUtil.saveData(SVTSConstants.signature, bean.getSignature());
		SharedPreferencesUtil.saveData(SVTSConstants.sex, bean.getGender());
		SharedPreferencesUtil.saveData(SVTSConstants.birthday, bean.getBirth());
		SharedPreferencesUtil.saveData(SVTSConstants.imgUrl, bean.getAvatar());
		SharedPreferencesUtil.saveData(SVTSConstants.isBindingWX, bean.getBandingWeixin());
		SharedPreferencesUtil.saveData(SVTSConstants.isBindingQQ, bean.getBandingQQ());
		SharedPreferencesUtil.saveData(SVTSConstants.isBindingWB, bean.getBandingWeibo());
		EventBus.getDefault().post(new UserInfoChangeSuccessEvent());
	}
	
	public static void logout() {
		SharedPreferencesUtil.saveData(SVTSConstants.localTimeStamp, (long) 0);
		SharedPreferencesUtil.saveData(SVTSConstants.serverTimeStamp, (long) 0);
		SharedPreferencesUtil.saveData(SVTSConstants.token, "");
		SharedPreferencesUtil.saveData(SVTSConstants.writerStatus, 0);
		saveUserInfo(new UserInfoBean());
		EventBus.getDefault().post(new LogoutEvent());
	}
	
	public static String getHDImageUrl(String imageUrl) {
		if (getIsHD()) {
			return imageUrl;
		} else {
			return imageUrl + "?x-oss-process=image/resize,p_50";
		}
	}
	
	public static boolean getIsHD() {
		int imageLoadMode = SharedPreferencesUtil.getInt(SVTSConstants.imageLoadMode, 0);
		boolean isWifi = isWifiConnected();
		switch (imageLoadMode) {
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
	 */
	public static boolean isWifiConnected() {
		if (App.getContext() != null) {
			// 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
			ConnectivityManager manager = (ConnectivityManager) App.getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 获取NetworkInfo对象
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			//判断NetworkInfo对象是否为空 并且类型是否为WIFI
			if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return networkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 判断是否有写手权限
	 */
	public static boolean isWriter(FragmentActivity activity) {
		int writerStatus = SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0);
		switch (writerStatus) {
			case writer_status_writer:
				return true;
			case writer_status_review:
				//资格审核中
				Intent intent1 = new Intent(activity, ApplyReviewActivity.class);
				activity.startActivity(intent1);
				break;
			default:
				CreationAgreementDialog dialog = new CreationAgreementDialog();
				dialog.show(activity.getSupportFragmentManager(), "");
				break;
			
		}
		return false;
	}
	
	/**
	 * 格式化数字,输出k，w结尾的小数
	 */
	public static String getFormatNumber(int number) {
		if (number < 1000) {
			return String.valueOf(number);
		} else if (number < 10000) {
			double dou = number / 1000.00;
			DecimalFormat df = new DecimalFormat("#.#");
			return df.format(dou) + "k";
		} else {
			double dou = number / 10000.00;
			DecimalFormat df = new DecimalFormat("#.#");
			return df.format(dou) + "w";
		}
	}

    /**
     * 传入时间戳变成时间
     */
    public static String timeChange(long num){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.parseLong(String.valueOf(num))));
    }
}