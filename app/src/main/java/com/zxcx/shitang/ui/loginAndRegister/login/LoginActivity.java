package com.zxcx.shitang.ui.loginAndRegister.login;

import android.os.Bundle;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {

    }
}
