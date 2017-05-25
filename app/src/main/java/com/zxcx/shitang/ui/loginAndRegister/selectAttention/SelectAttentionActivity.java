package com.zxcx.shitang.ui.loginAndRegister.selectAttention;

import android.os.Bundle;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;

public class SelectAttentionActivity extends MvpActivity<SelectAttentionPresenter> implements SelectAttentionContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_attention);
        initView();
    }

    private void initView() {

    }

    @Override
    protected SelectAttentionPresenter createPresenter() {
        return new SelectAttentionPresenter(this);
    }

    @Override
    public void getDataSuccess(SelectAttentionBean bean) {

    }

    @Override
    public void postSuccess() {

    }

    @Override
    public void postFail() {

    }
}
