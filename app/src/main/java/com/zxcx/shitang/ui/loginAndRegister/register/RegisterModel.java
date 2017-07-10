package com.zxcx.shitang.ui.loginAndRegister.register;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class RegisterModel extends BaseModel<RegisterContract.Presenter> {
    public RegisterModel(@NonNull RegisterContract.Presenter present) {
        this.mPresent = present;
    }
}


