package com.zxcx.zhizhe.ui.home.hot;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.List;

public class HotModel extends BaseModel<HotContract.Presenter> {

    public HotModel(@NonNull HotContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getHotCard(int page){
        mDisposable = AppClient.getAPIService().getHot(page)
                .compose(BaseRxJava.INSTANCE.handleArrayResult())
                .compose(BaseRxJava.INSTANCE.io_main())
                .subscribeWith(new BaseSubscriber<List<HotBean>>(mPresenter) {
                    @Override
                    public void onNext(List<HotBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


