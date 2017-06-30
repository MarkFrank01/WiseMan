package com.zxcx.shitang.ui.my.userInfo;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class UserInfoModel extends BaseModel<UserInfoContract.Presenter> {
    public UserInfoModel(@NonNull UserInfoContract.Presenter present) {
        this.mPresent = present;
    }
}


