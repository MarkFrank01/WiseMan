package com.zxcx.zhizhe.ui.my.creation.creationDetails;

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
        mDisposable = AppClient.getAPIService().getRejectDetails(RejectId,2)
                .compose(BaseRxJava.INSTANCE.io_main())
                .compose(BaseRxJava.INSTANCE.handleResult())
                .subscribeWith(new BaseSubscriber<RejectDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(RejectDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void getReviewDetails(int RejectId){
        mDisposable = AppClient.getAPIService().getRejectDetails(RejectId,1)
                .compose(BaseRxJava.INSTANCE.io_main())
                .compose(BaseRxJava.INSTANCE.handleResult())
                .subscribeWith(new BaseSubscriber<RejectDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(RejectDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }
}


