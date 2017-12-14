package com.zxcx.zhizhe.ui.my.likeCards

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface LikeCardsContract {

    interface View : GetView<List<LikeCardsBean>>

    interface Presenter : IGetPresenter<List<LikeCardsBean>>
}

