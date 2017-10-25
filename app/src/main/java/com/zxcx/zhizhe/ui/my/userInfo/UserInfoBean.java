package com.zxcx.zhizhe.ui.my.userInfo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

public class UserInfoBean extends RetrofitBaen {
    /**
     * id : 8
     * birth : 2017-03-25
     * avatar : default
     * name : zxst_82698
     * gender : 1
     * createTime : 1508142980000
     * bandingQQ : true
     * bandingWeixin : false
     * bandingWeibo : false
     */

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "birth")
    private String birth;
    @JSONField(name = "avatar")
    private String avatar;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "gender")
    private int gender;
    @JSONField(name = "createTime")
    private long createTime;
    @JSONField(name = "bandingQQ")
    private boolean bandingQQ;
    @JSONField(name = "bandingWeixin")
    private boolean bandingWeixin;
    @JSONField(name = "bandingWeibo")
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

