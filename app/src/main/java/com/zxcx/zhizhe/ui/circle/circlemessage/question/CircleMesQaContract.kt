package com.zxcx.zhizhe.ui.circle.circlemessage.question

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.circlemessage.MyCircleTabBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class CircleMesQaContract {
    interface View : GetView<MyCircleTabBean> {
        fun getQuestionMessageListSuccess(bean: MutableList<MyCircleTabBean>)
    }

    interface Presenter:IGetPresenter<MyCircleTabBean>{
        fun getQuestionMessageListSuccess(bean: MutableList<MyCircleTabBean>)
    }
}