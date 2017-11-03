package com.zxcx.zhizhe.ui.home.attention;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.home.hot.HotCardBagBean;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;

import java.util.List;

public class AttentionModel extends BaseModel<AttentionContract.Presenter> {

    public AttentionModel(@NonNull AttentionContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getAttentionCard(int page, int pageSize){
        mDisposable = AppClient.getAPIService().getAttentionCard(page,pageSize)
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

    public void getAttentionCardBag(){
        mDisposable = AppClient.getAPIService().getAttentionCardBag()
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

