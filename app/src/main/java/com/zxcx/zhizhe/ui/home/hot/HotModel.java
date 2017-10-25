package com.zxcx.zhizhe.ui.home.hot;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.List;

public class HotModel extends BaseModel<HotContract.Presenter> {

    public HotModel(@NonNull HotContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getHotCard(int page, int pageSize){
        mDisposable = AppClient.getAPIService().getHotCard(page,pageSize)
                .compose(BaseRxJava.<BaseArrayBean<HotCardBean>>io_main())
                .compose(BaseRxJava.<HotCardBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotCardBean>>(mPresenter) {
                    @Override
                    public void onNext(List<HotCardBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void getHotCardBag(){
        mDisposable = AppClient.getAPIService().getHotCardBag()
                .compose(BaseRxJava.<BaseArrayBean<HotCardBagBean>>io_main())
                .compose(BaseRxJava.<HotCardBagBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotCardBagBean>>(mPresenter) {
                    @Override
                    public void onNext(List<HotCardBagBean> list) {
                        mPresenter.getHotCardBagSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


