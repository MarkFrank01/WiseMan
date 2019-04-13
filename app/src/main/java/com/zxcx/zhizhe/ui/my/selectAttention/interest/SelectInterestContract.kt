package com.zxcx.zhizhe.ui.my.selectAttention.interest

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

interface SelectInterestContract {

	interface View : NullGetPostView<InterestRecommendBean>{
        fun mFollowUserSuccess(bean: SearchUserBean)
        fun unFollowUserSuccess(bean: SearchUserBean)
    }

	interface Presenter : INullGetPostPresenter<InterestRecommendBean>{
        fun mFollowUserSuccess(bean: SearchUserBean)
        fun unFollowUserSuccess(bean: SearchUserBean)
    }
}

