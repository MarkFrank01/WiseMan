package com.zxcx.zhizhe.ui.circle.circleowner.owneradd.addnext

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/15
 * @Description :
 */
class OwnerAddNextContract {

    interface View:GetView<MutableList<CardBean>>{
        fun setArcSuccess(bean: MutableList<CardBean>)
        fun checkCircleArticleBalanceSuccess(bean:BalanceBean)
    }

    interface Presenter:IGetPresenter<MutableList<CardBean>>{
        fun setArcSuccess(bean: MutableList<CardBean>)
        fun checkCircleArticleBalanceSuccess(bean:BalanceBean)

    }
}