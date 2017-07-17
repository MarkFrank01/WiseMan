package com.zxcx.shitang.ui.loginAndRegister.login.bindingPhone;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class BindingPhoneModel extends BaseModel<BindingPhoneContract.Presenter> {
    public BindingPhoneModel(@NonNull BindingPhoneContract.Presenter present) {
        this.mPresent = present;
    }
}


