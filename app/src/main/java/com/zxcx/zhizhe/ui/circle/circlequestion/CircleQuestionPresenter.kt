package com.zxcx.zhizhe.ui.circle.circlequestion

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/2/20
 * @Description :
 */
class CircleQuestionPresenter(view:CircleQuestionContract.View):BasePresenter<CircleQuestionContract.View>(),CircleQuestionContract.Presenter{


    private val mModel:CircleQuestionModel

    init {
        attachView(view)
        mModel = CircleQuestionModel(this)
    }

    fun pushQuestion(circleId:Int,title:String,description:String,askImage:List<String>){
        mModel.pushQuestion(circleId, title, description, askImage)
    }

    override fun pushQuestionSuccess() {
        if(mView!=null) {
            mView.pushQuestionSuccess()
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