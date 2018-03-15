package com.zxcx.zhizhe.ui.classify;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.List;

class ClassifyModel extends BaseModel<ClassifyContract.Presenter> {

    ClassifyModel(@NonNull ClassifyContract.Presenter present) {
        mPresenter = present;
    }

    void getClassify(){
        mDisposable = AppClient.getAPIService().getClassify()
                .compose(BaseRxJava.INSTANCE.<BaseArrayBean<ClassifyBean>>io_main())
                .compose(BaseRxJava.INSTANCE.<ClassifyBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<ClassifyBean>>(mPresenter) {
                    @Override
                    public void onNext(List<ClassifyBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


