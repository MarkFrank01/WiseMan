package com.zxcx.zhizhe.ui.my.userInfo;

import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.mvpBase.PostView;

public interface UserInfoContract {

    interface View extends PostView<UserInfoBean> {
        void changeImageSuccess(UserInfoBean bean);
    }

    interface Presenter extends IPostPresenter<UserInfoBean> {
        void changeImageSuccess(UserInfoBean bean);
    }
}

