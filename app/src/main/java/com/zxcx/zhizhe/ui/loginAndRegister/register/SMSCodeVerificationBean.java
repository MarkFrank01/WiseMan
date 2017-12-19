package com.zxcx.zhizhe.ui.loginAndRegister.register;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by anm on 2017/12/19.
 */

public class SMSCodeVerificationBean {
    /**
     * phoneNum : string
     * verifyKey : string
     */

    @JSONField(name = "phoneNum")
    private String phoneNum;
    @JSONField(name = "verifyKey")
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
