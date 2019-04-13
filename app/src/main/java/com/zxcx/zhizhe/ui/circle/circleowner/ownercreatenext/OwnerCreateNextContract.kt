package com.zxcx.zhizhe.ui.circle.circleowner.ownercreatenext

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerCreateNextContract {

    interface View: GetView<MutableList<CardBean>>{
        fun createCircleSuccess(bean:CircleBean)
    }

    interface Presenter: IGetPresenter<MutableList<CardBean>>{
        fun createCircleSuccess(bean: CircleBean)
    }
}