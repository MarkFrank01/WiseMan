package com.zxcx.zhizhe.ui.my.userInfo;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.PostSubscriber;

public class UserInfoModel extends BaseModel<UserInfoContract.Presenter> {
    public UserInfoModel(@NonNull UserInfoContract.Presenter present) {
        this.mPresenter = present;
    }

    public void changeImageUrl(String imageUrl){
        mDisposable = AppClient.getAPIService().changeUserInfo(imageUrl, null, null, null)
                .compose(BaseRxJava.<UserInfoBean>handleResult())
                .compose(BaseRxJava.<UserInfoBean>io_main_loading(mPresenter))
                .subscribeWith(new PostSubscriber<UserInfoBean>(mPresenter) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        mPresenter.changeImageSuccess(bean);
                    }
                });
    }

    public void changeBirth(String birth){
        mDisposable = AppClient.getAPIService().changeUserInfo(null, null, null, birth)
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


