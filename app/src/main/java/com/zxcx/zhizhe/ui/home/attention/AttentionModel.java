package com.zxcx.zhizhe.ui.home.attention;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.home.hot.HotBean;

import java.util.List;

public class AttentionModel extends BaseModel<AttentionContract.Presenter> {

    public AttentionModel(@NonNull AttentionContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getAttentionCard(int page, int pageSize){
        mDisposable = AppClient.getAPIService().getAttentionCard(page,pageSize)
                .compose(BaseRxJava.INSTANCE.io_main())
                .compose(BaseRxJava.INSTANCE.handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotBean>>(mPresenter) {
                    @Override
                    public void onNext(List<HotBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


