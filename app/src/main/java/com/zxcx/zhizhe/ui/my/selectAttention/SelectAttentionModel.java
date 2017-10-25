package com.zxcx.zhizhe.ui.my.selectAttention;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

import java.util.List;

public class SelectAttentionModel extends BaseModel<SelectAttentionContract.Presenter> {

    public SelectAttentionModel(@NonNull SelectAttentionContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getAttentionList(){
        mDisposable = AppClient.getAPIService().getAttentionList()
                .compose(BaseRxJava.<BaseArrayBean<SelectAttentionBean>>io_main())
                .compose(BaseRxJava.<SelectAttentionBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<SelectAttentionBean>>(mPresenter) {
                    @Override
                    public void onNext(List<SelectAttentionBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void changeAttentionList(List<Integer> idList){
        mDisposable = AppClient.getAPIService().changeAttentionList(idList)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.<BaseBean>io_main_loading(mPresenter))
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


