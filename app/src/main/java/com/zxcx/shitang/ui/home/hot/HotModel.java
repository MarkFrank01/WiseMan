package com.zxcx.shitang.ui.home.hot;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;

import java.util.List;

public class HotModel extends BaseModel<HotContract.Presenter> {

    public HotModel(@NonNull HotContract.Presenter present) {
        this.mPresent = present;
    }

    public void getHotCard(int page, int pageSize){
        subscription = AppClient.getAPIService().getHotCard(page,pageSize)
                .compose(this.<BaseArrayBean<HotCardBean>>io_main())
                .compose(this.<HotCardBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotCardBean>>(mPresent) {
                    @Override
                    public void onNext(List<HotCardBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(subscription);
    }

    public void getHotCardBag(){
        subscription = AppClient.getAPIService().getHotCardBag(1,1000)
                .compose(this.<BaseArrayBean<HotCardBagBean>>io_main())
                .compose(this.<HotCardBagBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotCardBagBean>>(mPresent) {
                    @Override
                    public void onNext(List<HotCardBagBean> list) {
                        mPresent.getHotCardBagSuccess(list);
                    }
                });
        addSubscription(subscription);
    }
}


