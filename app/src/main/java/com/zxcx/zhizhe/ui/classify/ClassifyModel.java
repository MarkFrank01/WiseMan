package com.zxcx.zhizhe.ui.classify;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.List;

public class ClassifyModel extends BaseModel<ClassifyContract.Presenter> {

    public ClassifyModel(@NonNull ClassifyContract.Presenter present) {
        mPresent = present;
    }

    public void getClassify(){
        mDisposable = AppClient.getAPIService().getClassify()
                .compose(this.<BaseArrayBean<ClassifyBean>>io_main())
                .compose(this.<ClassifyBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<ClassifyBean>>(mPresent) {
                    @Override
                    public void onNext(List<ClassifyBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


