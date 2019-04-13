package com.zxcx.zhizhe.ui.circle.circlemore

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/12
 * @Description :
 */
class CircleEditContract {

    interface View: GetPostView<CircleBean, CircleBean> {
        fun checkSuccess()
    }

    interface Presenter: IGetPostPresenter<CircleBean, CircleBean> {
        fun checkSuccess()
    }
}