package com.zxcx.shitang.ui.my.selectAttention;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.PostSubscriber;

import java.util.List;

public class SelectAttentionModel extends BaseModel<SelectAttentionContract.Presenter> {

    public SelectAttentionModel(@NonNull SelectAttentionContract.Presenter present) {
        this.mPresent = present;
    }

    public void getAttentionList(){
        subscription = AppClient.getAPIService().getAttentionList()
                .compose(this.<BaseArrayBean<SelectAttentionBean>>io_main())
                .compose(this.<SelectAttentionBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<SelectAttentionBean>>(mPresent) {
                    @Override
                    public void onNext(List<SelectAttentionBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(subscription);
    }

    public void changeAttentionList(List<Integer> idList){
        subscription = AppClient.getAPIService().changeAttentionList(idList)
                .compose(this.<BaseArrayBean<PostBean>>io_main())
                .compose(this.<PostBean>handleArrayResult())
                .subscribeWith(new PostSubscriber<List<PostBean>>(mPresent) {
                    @Override
                    public void onNext(List<PostBean> bean) {
                        mPresent.postSuccess(new PostBean());
                    }
                });
        addSubscription(subscription);
    }
}


