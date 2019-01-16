package com.zxcx.zhizhe.ui.my.selectAttention.interest

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView

interface SelectInterestContract {

	interface View : NullGetPostView<InterestRecommendBean>{
    }

	interface Presenter : INullGetPostPresenter<InterestRecommendBean>{
    }
}

