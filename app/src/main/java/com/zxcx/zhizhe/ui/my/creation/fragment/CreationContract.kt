package com.zxcx.zhizhe.ui.my.creation.fragment

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface CreationContract {

	interface View : GetView<List<CardBean>>

	interface Presenter : IGetPresenter<List<CardBean>>
}

