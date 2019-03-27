package com.zxcx.zhizhe.ui.circle.circlemanlist.detail

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/27
 * @Description :
 */
class CircleManDetailContract {

    interface View:GetView<CircleUserBean>{
        fun getCircleListByAuthorIdSuccess(list:MutableList<CircleBean>)
        fun getAuthorInfoSuccess(bean:CircleUserBean)
        fun getOtherUserCreationSuccess(list: MutableList<CardBean>)
        fun followSuccess()
    }

    interface Presenter:IGetPresenter<CircleUserBean>{
        fun getCircleListByAuthorIdSuccess(list:MutableList<CircleBean>)
        fun getAuthorInfoSuccess(bean:CircleUserBean)
        fun getOtherUserCreationSuccess(list: MutableList<CardBean>)
        fun followSuccess()
    }
}