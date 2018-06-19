package com.zxcx.zhizhe.ui.my.likeCards

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface LikeCardsContract {

    interface View : NullGetPostView<List<MyCardsBean>>{
        fun getEmptyRecommendCardSuccess(bean: CardBean)
    }

    interface Presenter : INullGetPostPresenter<List<MyCardsBean>>{
        fun getEmptyRecommendCardSuccess(bean: CardBean)
    }
}

