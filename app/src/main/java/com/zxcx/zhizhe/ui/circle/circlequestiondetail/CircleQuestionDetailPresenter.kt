package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/13
 * @Description :
 */
class CircleQuestionDetailPresenter(view:CircleQuestionDetailContract.View):BasePresenter<CircleQuestionDetailContract.View>(),CircleQuestionDetailContract.Presenter {

    private val mModel: CircleQuestionDetailModel

    init {
        attachView(view)
        mModel = CircleQuestionDetailModel(this)
    }

    fun getQuestionInfo(qaId: Int) {
        mModel.getQuestionInfo(qaId)
    }

    override fun getBasicQuestionSuccess(bean: CircleDetailBean) {
        mView.getBasicQuestionSuccess(bean)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: CircleDetailBean?) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }
}