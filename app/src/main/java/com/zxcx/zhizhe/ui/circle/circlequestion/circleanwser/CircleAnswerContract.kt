package com.zxcx.zhizhe.ui.circle.circlequestion.circleanwser

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.circle.circlequestion.QuestionBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleAnswerContract {
    interface View:GetPostView<QuestionBean,QuestionBean>{
        fun createAnswerSuccess()
    }

    interface Presenter: IGetPostPresenter<QuestionBean, QuestionBean> {
        fun createAnswerSuccess()
    }
}