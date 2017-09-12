package com.zxcx.shitang.ui.my.feedback.feedback;

import com.zxcx.shitang.mvpBase.IPostPresenter;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.mvpBase.PostView;

public interface FeedbackContract {

    interface View extends PostView<PostBean> {

    }

    interface Presenter extends IPostPresenter<PostBean> {

    }
}

