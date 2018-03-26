package com.zxcx.zhizhe.ui.my.collect

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.my.likeCards.MyCardsBean

interface CollectCardContract {

    interface View : NullGetPostView<List<MyCardsBean>>

    interface Presenter : INullGetPostPresenter<List<MyCardsBean>>
}

