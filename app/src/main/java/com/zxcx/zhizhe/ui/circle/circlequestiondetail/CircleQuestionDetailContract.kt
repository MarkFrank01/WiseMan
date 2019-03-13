package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/13
 * @Description :
 */
class CircleQuestionDetailContract {
    interface View:GetView<CircleDetailBean>{
        fun getCommentBeanSuccess(bean: MutableList<CircleCommentBean>)
        fun getBasicQuestionSuccess(bean:CircleDetailBean)
        fun likeSuccess()
        fun unlikeSuccess()
    }

    interface Presenter:IGetPresenter<CircleDetailBean>{
        fun getCommentBeanSuccess(bean: MutableList<CircleCommentBean>)
        fun getBasicQuestionSuccess(bean:CircleDetailBean)
        fun likeSuccess()
        fun unlikeSuccess()
    }
}