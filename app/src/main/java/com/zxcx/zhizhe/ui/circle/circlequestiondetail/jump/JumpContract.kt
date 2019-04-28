package com.zxcx.zhizhe.ui.circle.circlequestiondetail.jump

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.circle.circlequestiondetail.CircleCommentBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/28
 * @Description :
 */
class JumpContract {
    interface View:GetPostView<MutableList<CircleCommentBean>,CircleCommentBean>{
        fun likeSuccess()
        fun unlikeSuccess()
    }

    interface Presenter:IGetPostPresenter<MutableList<CircleCommentBean>,CircleCommentBean>{
        fun likeSuccess()
        fun unlikeSuccess()
    }
}