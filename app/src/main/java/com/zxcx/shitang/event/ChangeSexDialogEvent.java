package com.zxcx.shitang.event;

import com.zxcx.shitang.ui.my.userInfo.UserInfoBean;

/**
 * Created by anm on 2017/7/4.
 */

public class ChangeSexDialogEvent {
    private UserInfoBean mUserInfoBean;

    public ChangeSexDialogEvent(UserInfoBean mUserInfoBean) {
        this.mUserInfoBean = mUserInfoBean;
    }

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.mUserInfoBean = userInfoBean;
    }
}
