package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter

interface FollowUserContract {

    interface View : GetPostView<List<FollowUserBean>,FollowUserBean> {
        fun unFollowUserSuccess(bean: FollowUserBean)
    }

    interface Presenter : IGetPostPresenter<List<FollowUserBean>,FollowUserBean> {
        fun unFollowUserSuccess(bean: FollowUserBean)
    }
}

