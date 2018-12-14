package com.zxcx.zhizhe.ui.my.daily

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyContract {

    interface View : GetView<MutableList<CardBean>>

    interface Presenter : IGetPresenter<MutableList<CardBean>>

}