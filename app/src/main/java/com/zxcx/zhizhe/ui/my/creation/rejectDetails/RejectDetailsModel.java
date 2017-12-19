package com.zxcx.zhizhe.ui.my.creation.rejectDetails;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

public class RejectDetailsModel extends BaseModel<RejectDetailsContract.Presenter> {
    public RejectDetailsModel(@NonNull RejectDetailsContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getRejectDetails(int RejectId){
        mDisposable = AppClient.getAPIService().getRejectDetails(RejectId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(new BaseSubscriber<RejectDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(RejectDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }
}


