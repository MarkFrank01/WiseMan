package com.zxcx.zhizhe.ui.my.setting;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

/**
 * Created by anm on 2017/12/19.
 */

public class MessageModeBean extends RetrofitBaen {

	/**
	 * dynamicMessageSetting : true systemMessageSetting : true
	 */

	@SerializedName("dynamicMessageSetting")
	private boolean dynamicMessageSetting;
	@SerializedName("systemMessageSetting")
	private boolean systemMessageSetting;

	public boolean isDynamicMessageSetting() {
		return dynamicMessageSetting;
	}

	public void setDynamicMessageSetting(boolean dynamicMessageSetting) {
		this.dynamicMessageSetting = dynamicMessageSetting;
	}

	public boolean isSystemMessageSetting() {
		return systemMessageSetting;
	}

	public void setSystemMessageSetting(boolean systemMessageSetting) {
		this.systemMessageSetting = systemMessageSetting;
	}
}
