package com.zxcx.zhizhe.ui.card.hot

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.welcome.ADBean

interface HotCardContract {

    interface View : GetView<MutableList<CardBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
    }

    interface Presenter : IGetPresenter<MutableList<CardBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
    }
}

