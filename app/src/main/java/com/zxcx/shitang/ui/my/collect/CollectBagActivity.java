package com.zxcx.shitang.ui.my.collect;

import android.os.Bundle;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;

public class CollectBagActivity extends MvpActivity<CollectBagPresenter> implements CollectBagContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_bag);

    }

    @Override
    protected CollectBagPresenter createPresenter() {
        return new CollectBagPresenter(this);
    }

    @Override
    public void getDataSuccess(CollectBagBean bean) {

    }
}
