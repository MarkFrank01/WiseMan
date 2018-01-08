package com.zxcx.zhizhe.ui.welcome;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

/**
 * Created by anm on 2017/9/25.
 */

public class WelcomeBean extends RetrofitBaen {

    /**
     * adNum : 0
     * behavior : string
     * content : string
     * createTime : 2017-09-25T09:18:25.424Z
     * description : string
     * id : 0
     * platformType : 0
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
}
