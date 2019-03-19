package com.zxcx.zhizhe.ui.circle.circlequestion.circleanwser

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlequestion.QuestionBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleAnswerPresenter(view: CircleAnswerContract.View) : BasePresenter<CircleAnswerContract.View>(), CircleAnswerContract.Presenter {

    private val mModel: CircleAnswerModel

    init {
        attachView(view)
        mModel = CircleAnswerModel(this)
    }

    fun createAnswer(circleId: Int, qaId: Int,  description: String, askImage: List<String>) {
        mModel.createAnswer(circleId, qaId, description, askImage)
    }

    override fun createAnswerSuccess() {
        if (mView != null) {
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