package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 */

public class PhoneRegisteredEvent {
    private String phone;

    public PhoneRegisteredEvent(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
