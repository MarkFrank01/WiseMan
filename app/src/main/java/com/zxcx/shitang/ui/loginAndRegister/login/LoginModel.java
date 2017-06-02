package com.zxcx.shitang.ui.loginAndRegister.login;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class LoginModel extends BaseModel<LoginContract.Presenter> {

    public LoginModel(@NonNull LoginContract.Presenter present) {
        this.mPresent = present;
    }
}


