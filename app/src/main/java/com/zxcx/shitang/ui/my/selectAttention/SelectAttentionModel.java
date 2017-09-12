package com.zxcx.shitang.ui.my.selectAttention;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.PostSubscriber;

import java.util.List;

public class SelectAttentionModel extends BaseModel<SelectAttentionContract.Presenter> {

    public SelectAttentionModel(@NonNull SelectAttentionContract.Presenter present) {
        this.mPresent = present;
    }

    public void getAttentionList(int userId){
        subscription = AppClient.getAPIService().getAttentionList(userId, 1,1000)
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

    public void changeAttentionList(int userId, List<Integer> idList){
        subscription = AppClient.getAPIService().changeAttentionList(userId, idList)
                .compose(this.<BaseBean<PostBean>>io_main())
                .compose(this.<PostBean>handleResult())
                .subscribeWith(new PostSubscriber<PostBean>(mPresent) {
                    @Override
                    public void onNext(PostBean bean) {
                        mPresent.postSuccess(bean);
                    }
                });
        addSubscription(subscription);
    }
}


