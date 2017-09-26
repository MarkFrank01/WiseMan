package com.zxcx.shitang.ui.my.selectAttention;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.NullPostSubscriber;

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
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresent.postSuccess();
                    }
                });
        addSubscription(subscription);
    }
}


