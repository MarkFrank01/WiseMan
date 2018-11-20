package com.zxcx.zhizhe.ui.welcome;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBean;

/**
 * Created by anm on 2017/9/25.
 */

public class ADBean extends RetrofitBean implements Parcelable {
	
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
	@SerializedName("titleImage")
	private String titleImage;
	@SerializedName("id")
	private int id;
	@SerializedName("styleType") //广告类型 0广告 1公告 2 活动
	private int styleType;
	@SerializedName("platformType")
	private int platformType;
	@SerializedName("shareImage")
    private String shareImage;
	@SerializedName("shareDescription")
    private String shareDescription;
	public ADBean() {
	}
	
	protected ADBean(Parcel in) {
		this.adNum = in.readInt();
		this.behavior = in.readString();
		this.content = in.readString();
		this.createTime = in.readString();
		this.description = in.readString();
		this.titleImage = in.readString();
		this.id = in.readInt();
		this.styleType = in.readInt();
		this.platformType = in.readInt();
		this.shareImage = in.readString();
		this.shareDescription = in.readString();
	}
	
	public String getTitleImage() {
		return titleImage;
	}
	
	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}
	
	public int getStyleType() {
		return styleType;
	}
	
	public void setStyleType(int styleType) {
		this.styleType = styleType;
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

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public String getShareDescription() {
        return shareDescription;
    }

    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
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
		dest.writeString(this.titleImage);
		dest.writeInt(this.id);
		dest.writeInt(this.styleType);
		dest.writeInt(this.platformType);
		dest.writeString(this.shareImage);
		dest.writeString(this.shareDescription);
	}
}
