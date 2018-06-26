package com.zxcx.zhizhe.ui.loginAndRegister.login;

import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

/**
 * Created by anm on 2017/12/19.
 */

public class SMSCodeVerificationBean extends RetrofitBaen{
    /**
     * phoneNum : string
     * verifyKey : string
     */

    @SerializedName("phoneNum")
    private String phoneNum;
    @SerializedName("verifyKey")
    private String verifyKey;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getVerifyKey() {
        return verifyKey;
    }

    public void setVerifyKey(String verifyKey) {
        this.verifyKey = verifyKey;
    }
}
