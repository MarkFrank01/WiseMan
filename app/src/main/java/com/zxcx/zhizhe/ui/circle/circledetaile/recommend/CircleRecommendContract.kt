package com.zxcx.zhizhe.ui.circle.circledetaile.recommend

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/18
 * @Description :
 */
class CircleRecommendContract {
    interface View:GetView<MutableList<CardBean>>{
        fun getRecommendArcSuccess(bean:MutableList<CardBean>)
    }

    interface Presenter:IGetPresenter<MutableList<CardBean>>{
        fun getRecommendArcSuccess(bean:MutableList<CardBean>)
    }
}