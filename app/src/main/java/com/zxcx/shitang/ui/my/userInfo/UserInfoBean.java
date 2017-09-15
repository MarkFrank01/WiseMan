package com.zxcx.shitang.ui.my.userInfo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.shitang.retrofit.RetrofitBaen;

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
    @JSONField(name = "createTime")
    private String createTime;
    @JSONField(name = "gender")
    private int gender;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "name")
    private String name;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
}

