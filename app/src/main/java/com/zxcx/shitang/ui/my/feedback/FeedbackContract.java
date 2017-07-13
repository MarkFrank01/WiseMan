package com.zxcx.shitang.ui.my.feedback;

import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.ui.my.feedback.FeedbackBean;

public interface FeedbackContract {

    interface View extends MvpView<FeedbackBean> {

    }

    interface Presenter extends IBasePresenter<FeedbackBean> {

    }
}

