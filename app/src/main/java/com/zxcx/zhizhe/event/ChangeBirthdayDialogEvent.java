package com.zxcx.zhizhe.event;

import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;

/**
 * Created by anm on 2017/7/4.
 */

public class ChangeBirthdayDialogEvent {
    private UserInfoBean mUserInfoBean;

    public ChangeBirthdayDialogEvent(UserInfoBean mUserInfoBean) {
        this.mUserInfoBean = mUserInfoBean;
    }

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.mUserInfoBean = userInfoBean;
    }
}
