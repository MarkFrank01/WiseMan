package com.zxcx.zhizhe.ui.my.userInfo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

public class UserInfoBean extends RetrofitBaen {
    /**
     * avatar : string
     * birth : string
     * createTime : 2017-07-24T06:46:20.757Z
     * gender : 0
     * id : 0
     * name : string
     */

    @JSONField(name = "avatar")
    private String avatar;
    @JSONField(name = "birth")
    private String birth;
    @JSONField(name = "gender")
    private int gender;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "bandingQQ")
    private boolean bandingQQ;
    @JSONField(name = "bandingWeibo")
    private boolean bandingWeibo;
    @JSONField(name = "bandingWeixin")
    private boolean bandingWeixin;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBandingQQ() {
        return bandingQQ;
    }

    public void setBandingQQ(boolean bandingQQ) {
        this.bandingQQ = bandingQQ;
    }

    public boolean isBandingWeibo() {
        return bandingWeibo;
    }

    public void setBandingWeibo(boolean bandingWeibo) {
        this.bandingWeibo = bandingWeibo;
    }

    public boolean isBandingWeixin() {
        return bandingWeixin;
    }

    public void setBandingWeixin(boolean bandingWeixin) {
        this.bandingWeixin = bandingWeixin;
    }
}

