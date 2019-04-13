package com.zxcx.zhizhe.ui.circle.circlemessage

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleMessageContract {

    interface View:GetView<MyCircleTabBean>{
        fun getRedPointStatusSuccess(bean:MyCircleTabBean)
        fun getCommentMessageListSuccess(bean:MutableList<MyCircleTabBean>)
    }


    interface Presenter:IGetPresenter<MyCircleTabBean>{
        fun getRedPointStatusSuccess(bean:MyCircleTabBean)
        fun getCommentMessageListSuccess(bean:MutableList<MyCircleTabBean>)
    }
}