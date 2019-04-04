package com.zxcx.zhizhe.ui.circle.circlemore

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.circle.bean.CheckBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/4
 * @Description :
 */
class CirclePingFenContract {
    //evaluationCircle

    interface View:NullGetPostView<CheckBean> {
        fun evaluationCircleSuccess()
    }

    interface Presenter:INullGetPostPresenter<CheckBean>{
        fun evaluationCircleSuccess()
    }
}