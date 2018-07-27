package com.zxcx.zhizhe.ui.my.creation.fragment

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface CreationContract {

	interface View : INullGetPostPresenter<List<CardBean>>

	interface Presenter : INullGetPostPresenter<List<CardBean>>
}

