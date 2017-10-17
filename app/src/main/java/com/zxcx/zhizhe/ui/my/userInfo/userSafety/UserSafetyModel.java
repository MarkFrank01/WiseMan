package com.zxcx.zhizhe.ui.my.userInfo.userSafety;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;

public class UserSafetyModel extends BaseModel<UserSafetyContract.Presenter> {
    public UserSafetyModel(@NonNull UserSafetyContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void channelBinding(int channelType, int type, String openId){
        mDisposable = AppClient.getAPIService().channelBinding(channelType,type,openId)
                .compose(BaseRxJava.<UserInfoBean>handleResult())
                .compose(BaseRxJava.<UserInfoBean>io_main_loading(mPresenter))
                .subscribeWith(new PostSubscriber<UserInfoBean>(mPresenter) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        mPresenter.postSuccess(bean);
                    }
                });
    }
}


