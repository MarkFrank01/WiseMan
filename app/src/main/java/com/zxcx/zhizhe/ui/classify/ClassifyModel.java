package com.zxcx.zhizhe.ui.classify;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.List;

public class ClassifyModel extends BaseModel<ClassifyContract.Presenter> {

    public ClassifyModel(@NonNull ClassifyContract.Presenter present) {
        mPresenter = present;
    }

    public void getClassify(){
        mDisposable = AppClient.getAPIService().getClassify()
                .compose(BaseRxJava.<BaseArrayBean<ClassifyBean>>io_main())
                .compose(BaseRxJava.<ClassifyBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<ClassifyBean>>(mPresenter) {
                    @Override
                    public void onNext(List<ClassifyBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}

