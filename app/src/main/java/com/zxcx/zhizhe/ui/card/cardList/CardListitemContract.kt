package com.zxcx.zhizhe.ui.card.cardList

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
interface CardListitemContract {

    interface View : GetView<MutableList<CardBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
    }

    interface Presenter : IGetPresenter<MutableList<CardBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
    }
}