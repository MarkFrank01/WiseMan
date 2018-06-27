package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface SearchCardContract {

    interface View : GetView<MutableList<CardBean>>

    interface Presenter : IGetPresenter<MutableList<CardBean>>
}

