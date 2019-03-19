package com.zxcx.zhizhe.ui.circle.circlequestion.circleanwser

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlequestion.QuestionBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleAnswerChildPresenter(view: CircleAnswerChildContract.View) : BasePresenter<CircleAnswerChildContract.View>(), CircleAnswerChildContract.Presenter {

    private val mModel: CircleAnswerChildModel

    init {
        attachView(view)
        mModel = CircleAnswerChildModel(this)
    }

    fun createAnswer(circleId: Int, qaId: Int, qaCommentId: Int, description: String) {
        mModel.createAnswer(circleId, qaId, qaCommentId, description)
    }

    override fun createAnswerSuccess() {
        if (mView != null){
            mView.createAnswerSuccess()
        }
    }

    override fun showLoading() {
    }

    override fun postSuccess(bean: QuestionBean?) {
    }

    override fun hideLoading() {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: QuestionBean?) {
    }

    override fun startLogin() {
    }

}