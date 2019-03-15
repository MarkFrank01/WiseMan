package com.zxcx.zhizhe.ui.circle.circledetaile

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleDetaileContract{
    interface View:GetView<MutableList<CircleBean>>{
        fun getCircleBasicInfoSuccess(bean:CircleBean)
        fun getCircleMemberByCircleIdSuccess(bean:MutableList<CircleBean>)
        fun getCircleQAByCircleIdSuccess(bean:MutableList<CircleDetailBean>)
        fun reportCircleSuccess()
        fun setQAFixTopSuccess()
        fun deleteQaSuccess()
    }

    interface Presenter:IGetPresenter<MutableList<CircleBean>>{
        fun getCircleBasicInfoSuccess(bean:CircleBean)
        fun getCircleMemberByCircleIdSuccess(bean:MutableList<CircleBean>)
        fun getCircleQAByCircleIdSuccess(bean:MutableList<CircleDetailBean>)
        fun reportCircleSuccess()
        fun setQAFixTopSuccess()
        fun deleteQaSuccess()
    }
}