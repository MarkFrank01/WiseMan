package com.zxcx.zhizhe.ui.my.userInfo;

import com.zxcx.zhizhe.mvpBase.GetPostView;
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter;

public interface UserInfoContract {

    interface View extends GetPostView<OSSTokenBean,UserInfoBean> {

    }

    interface Presenter extends IGetPostPresenter<OSSTokenBean,UserInfoBean> {

    }
}

