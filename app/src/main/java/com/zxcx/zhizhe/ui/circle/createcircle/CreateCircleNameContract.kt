package com.zxcx.zhizhe.ui.circle.createcircle

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/1
 * @Description :
 */
class CreateCircleNameContract {

    interface View: GetPostView<CircleBean, CircleBean> {
        fun checkSuccess()
        fun checkCanUse()
    }

    interface Presenter: IGetPostPresenter<CircleBean, CircleBean> {
        fun checkSuccess()
        fun checkCanUse()
    }
}