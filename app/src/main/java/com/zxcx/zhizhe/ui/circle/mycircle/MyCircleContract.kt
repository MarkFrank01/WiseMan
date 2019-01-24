package com.zxcx.zhizhe.ui.circle.mycircle

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class MyCircleContract{
    interface View : GetView<MutableList<CircleBean>>{
        fun getMyJoinSuccess(list: MutableList<CircleBean>)
        fun getMyCreateSuccess(list: MutableList<CircleBean>)
    }

    interface Presenter : IGetPresenter<MutableList<CircleBean>>{
        fun getMyJoinSuccess(list: MutableList<CircleBean>)
        fun getMyCreateSuccess(list: MutableList<CircleBean>)
    }
}