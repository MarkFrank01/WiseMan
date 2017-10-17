package com.zxcx.zhizhe.ui.my.userInfo.userSafety;


import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.mvpBase.PostView;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean;

public interface UserSafetyContract {

    interface View extends PostView<UserInfoBean> {

    }

    interface Presenter extends IPostPresenter<UserInfoBean> {

    }
}

