package com.zxcx.shitang.ui.home.attention;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;
import com.zxcx.shitang.ui.home.hot.HotCardBean;

import java.util.List;

public class AttentionModel extends BaseModel<AttentionContract.Presenter> {

    public AttentionModel(@NonNull AttentionContract.Presenter present) {
        this.mPresent = present;
    }

    public void getAttentionCard(int page, int pageSize){
        mDisposable = AppClient.getAPIService().getAttentionCard(page,pageSize)
                .compose(this.<BaseArrayBean<HotCardBean>>io_main())
                .compose(this.<HotCardBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotCardBean>>(mPresent) {
                    @Override
                    public void onNext(List<HotCardBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void getAttentionCardBag(){
        mDisposable = AppClient.getAPIService().getAttentionCardBag()
                .compose(this.<BaseArrayBean<HotCardBagBean>>io_main())
                .compose(this.<HotCardBagBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<HotCardBagBean>>(mPresent) {
                    @Override
                    public void onNext(List<HotCardBagBean> list) {
                        mPresent.getHotCardBagSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


