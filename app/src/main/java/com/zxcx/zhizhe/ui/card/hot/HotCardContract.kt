package com.zxcx.zhizhe.ui.card.hot

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface HotCardContract {

	interface View : GetView<MutableList<CardBean>>

	interface Presenter : IGetPresenter<MutableList<CardBean>>
}

