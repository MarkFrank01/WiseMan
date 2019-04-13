package com.zxcx.zhizhe.ui.circle.circlesearch.inside.qa

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideQaContract {
    interface View:GetView<MutableList<CircleBean>>{
        fun getCircleQAByCircleIdSuccess(bean:MutableList<CircleDetailBean>)
    }

    interface Presenter:IGetPresenter<MutableList<CircleBean>>{
        fun getCircleQAByCircleIdSuccess(bean:MutableList<CircleDetailBean>)
    }
}