package com.zxcx.zhizhe.ui.loginAndRegister.login;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;

public class LoginBean extends RetrofitBaen {

    /**
     * serviceStartTime : 0
     * token : string
     * user : {"avatar":"string","birth":"string","createTime":"2017-07-24T06:46:20.757Z","gender":0,"id":0,"name":"string"}
     */

    @SerializedName("serviceStartTime")
    private long serviceStartTime;
    @SerializedName("token")
    private String token;
    @SerializedName("writerStatus")
    private int writerStatus;
    @SerializedName("user")
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

    public int getWriterStatus() {
        return writerStatus;
    }

    public void setWriterStatus(int writerStatus) {
        this.writerStatus = writerStatus;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }
}

