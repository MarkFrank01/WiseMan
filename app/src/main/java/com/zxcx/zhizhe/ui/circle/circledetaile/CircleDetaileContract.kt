package com.zxcx.zhizhe.ui.circle.circledetaile

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.my.money.MoneyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleDetaileContract{
    interface View:NullGetPostView<MutableList<CircleBean>>{
        fun getCircleBasicInfoSuccess(bean:CircleBean)
        fun getCircleMemberByCircleIdSuccess(bean:MutableList<CircleBean>)
        fun getCircleQAByCircleIdSuccess(bean:MutableList<CircleDetailBean>)
        fun reportCircleSuccess()
        fun setQAFixTopSuccess()
        fun deleteQaSuccess()
        fun joinCircleByZzbForAndroidSuccess()
        fun getAccountDetailsSuccess(bean: MoneyBean)
    }

    interface Presenter:INullGetPostPresenter<MutableList<CircleBean>>{
        fun getCircleBasicInfoSuccess(bean:CircleBean)
        fun getCircleMemberByCircleIdSuccess(bean:MutableList<CircleBean>)
        fun getCircleQAByCircleIdSuccess(bean:MutableList<CircleDetailBean>)
        fun reportCircleSuccess()
        fun setQAFixTopSuccess()
        fun deleteQaSuccess()
        fun joinCircleByZzbForAndroidSuccess()
        fun getAccountDetailsSuccess(bean:MoneyBean)
    }
}