package com.zxcx.shitang.ui.my.userInfo;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.PostSubscriber;

public class UserInfoModel extends BaseModel<UserInfoContract.Presenter> {
    public UserInfoModel(@NonNull UserInfoContract.Presenter present) {
        this.mPresent = present;
    }

    public void getOSS(String uuid){
        subscription = AppClient.getAPIService().getOSS(uuid)
                .compose(this.<BaseBean<OSSTokenBean>>io_main())
                .compose(this.<OSSTokenBean>handleResult())
                .subscribeWith(new BaseSubscriber<OSSTokenBean>(mPresent) {
                    @Override
                    public void onNext(OSSTokenBean bean) {
                        mPresent.getDataSuccess(bean);
                    }
                });
        addSubscription(subscription);
    }

    public void changeImageUrl(String imageUrl){
        subscription = AppClient.getAPIService().changeUserInfo(imageUrl, null, null, null)
                .compose(this.<BaseBean<UserInfoBean>>io_main())
                .compose(this.<UserInfoBean>handleResult())
                .subscribeWith(new PostSubscriber<UserInfoBean>(mPresent) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        mPresent.postSuccess(bean);
                    }
                });
    }
}


