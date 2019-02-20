package com.zxcx.zhizhe.ui.circle.circlequestion

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/2/20
 * @Description :
 */
class CircleQuestionContract{

    interface View:GetPostView<QuestionBean,QuestionBean>{
        fun pushQuestionSuccess()
    }

    interface Presenter:IGetPostPresenter<QuestionBean,QuestionBean>{
        fun pushQuestionSuccess()
    }
}