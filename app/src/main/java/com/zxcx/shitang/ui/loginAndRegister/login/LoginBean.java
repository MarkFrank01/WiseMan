package com.zxcx.shitang.ui.loginAndRegister.login;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.shitang.ui.my.userInfo.UserInfoBean;

public class LoginBean {

    /**
     * serviceStartTime : 0
     * token : string
     * user : {"avatar":"string","birth":"string","createTime":"2017-07-24T06:46:20.757Z","gender":0,"id":0,"name":"string"}
     */

    @JSONField(name = "serviceStartTime")
    private long serviceStartTime;
    @JSONField(name = "token")
    private String token;
    @JSONField(name = "user")
    private UserInfoBean user;

    public long getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(long serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }
}

