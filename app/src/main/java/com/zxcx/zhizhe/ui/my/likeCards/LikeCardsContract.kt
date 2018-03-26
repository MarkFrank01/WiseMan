package com.zxcx.zhizhe.ui.my.likeCards

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView

interface LikeCardsContract {

    interface View : NullGetPostView<List<MyCardsBean>>

    interface Presenter : INullGetPostPresenter<List<MyCardsBean>>
}

