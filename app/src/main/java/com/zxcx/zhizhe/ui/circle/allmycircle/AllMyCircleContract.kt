package com.zxcx.zhizhe.ui.circle.allmycircle

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class AllMyCircleContract {
    interface View : GetView<MutableList<CircleBean>>{
        fun emptyCircle(bean:MutableList<CircleBean>)
    }

    interface Presenter : IGetPresenter<MutableList<CircleBean>>{
        fun emptyCircle(bean:MutableList<CircleBean>)
    }
}