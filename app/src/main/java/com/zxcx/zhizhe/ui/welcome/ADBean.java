package com.zxcx.zhizhe.ui.welcome;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

/**
 * Created by anm on 2017/9/25.
 */

public class ADBean extends RetrofitBaen implements Parcelable {
	
	public static final Creator<ADBean> CREATOR = new Creator<ADBean>() {
		@Override
		public ADBean createFromParcel(Parcel source) {
			return new ADBean(source);
		}
		
		@Override
		public ADBean[] newArray(int size) {
			return new ADBean[size];
		}
	};
	/**
	 * adNum : 0 behavior : 点击跳转地址 content : 图片地址 createTime : 2017-09-25T09:18:25.424Z description
	 * : 广告标题 id : 0 platformType : 0
	 */

	@SerializedName("adNum")
	private int adNum;
	@SerializedName("behavior")
	private String behavior;
	@SerializedName("content")
	private String content;
	@SerializedName("createTime")
	private String createTime;
	@SerializedName("description")
	private String description;
	@SerializedName("id")
	private int id;
	@SerializedName("platformType")
	private int platformType;
	
	public ADBean() {
	}

	protected ADBean(Parcel in) {
		this.adNum = in.readInt();
		this.behavior = in.readString();
		this.content = in.readString();
		this.createTime = in.readString();
		this.description = in.readString();
		this.id = in.readInt();
		this.platformType = in.readInt();
	}

	public int getAdNum() {
		return adNum;
	}

	public void setAdNum(int adNum) {
		this.adNum = adNum;
	}

	public String getBehavior() {
		return behavior;
	}

	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getPlatformType() {
		return platformType;
	}
	
	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.adNum);
		dest.writeString(this.behavior);
		dest.writeString(this.content);
		dest.writeString(this.createTime);
		dest.writeString(this.description);
		dest.writeInt(this.id);
		dest.writeInt(this.platformType);
	}
}
