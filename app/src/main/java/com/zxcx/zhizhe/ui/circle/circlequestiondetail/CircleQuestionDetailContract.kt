package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/13
 * @Description :
 */
class CircleQuestionDetailContract {
    interface View: GetPostView<MutableList<CircleCommentBean>,CircleCommentBean> {
        fun getCommentBeanSuccess(bean: MutableList<CircleCommentBean>)
        fun getBasicQuestionSuccess(bean:CircleDetailBean)
        fun likeSuccess()
        fun unlikeSuccess()

        fun likeCreateSuccess()
        fun unlikeCreateSuccess()
    }

    interface Presenter: IGetPostPresenter<MutableList<CircleCommentBean>,CircleCommentBean> {
        fun getCommentBeanSuccess(bean: MutableList<CircleCommentBean>)
        fun getBasicQuestionSuccess(bean:CircleDetailBean)
        fun likeSuccess()
        fun unlikeSuccess()

        fun likeCreateSuccess()
        fun unlikeCreateSuccess()
    }
}