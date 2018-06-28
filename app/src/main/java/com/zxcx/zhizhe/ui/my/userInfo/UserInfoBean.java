package com.zxcx.zhizhe.ui.my.userInfo;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

public class UserInfoBean extends RetrofitBaen {

	/**
	 * id : 8 birth : 2017-03-25 avatar : default name : zxst_82698 gender : 1 createTime :
	 * 1508142980000 bandingQQ : true bandingWeixin : false bandingWeibo : false
	 */

	@SerializedName("id")
	private int id;
	@SerializedName("birth")
	private String birth;
	@SerializedName("avatar")
	private String avatar;
	@SerializedName("name")
	private String name;
	@SerializedName("sign")
	private String signture;
	@SerializedName("phoneNum")
	private String phoneNum;
	@SerializedName("gender")
	private int gender;
	@SerializedName("createTime")
	private long createTime;
	@SerializedName("bandingQQ")
	private boolean bandingQQ;
	@SerializedName("bandingWeixin")
	private boolean bandingWeixin;
	@SerializedName("bandingWeibo")
	private boolean bandingWeibo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSignture() {
		return signture;
	}

	public void setSignture(String signture) {
		this.signture = signture;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean isBandingQQ() {
		return bandingQQ;
	}

	public void setBandingQQ(boolean bandingQQ) {
		this.bandingQQ = bandingQQ;
	}

	public boolean isBandingWeixin() {
		return bandingWeixin;
	}

	public void setBandingWeixin(boolean bandingWeixin) {
		this.bandingWeixin = bandingWeixin;
	}

	public boolean isBandingWeibo() {
		return bandingWeibo;
	}

	public void setBandingWeibo(boolean bandingWeibo) {
		this.bandingWeibo = bandingWeibo;
	}
}

