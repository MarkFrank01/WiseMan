package com.zxcx.zhizhe.ui.my.feedback.feedback;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

public class FeedbackModel extends BaseModel<FeedbackContract.Presenter> {
    public FeedbackModel(@NonNull FeedbackContract.Presenter present) {
        this.mPresenter = present;
    }

    public void feedback(String content, String contact,int appType, String appChannel, String appVersion){
        mDisposable = AppClient.getAPIService().feedback(content,contact,appType,appChannel,appVersion)
                .compose(BaseRxJava.INSTANCE.handlePostResult())
                .compose(BaseRxJava.INSTANCE.<BaseBean>io_main_loading(mPresenter))
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


