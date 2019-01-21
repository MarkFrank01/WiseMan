package com.zxcx.zhizhe.ui.circle.circlehome

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
interface CircleContract {

    interface View : GetView<MutableList<CircleBean>> {

    }

    interface Presenter : IGetPresenter<MutableList<CircleBean>> {

    }

}