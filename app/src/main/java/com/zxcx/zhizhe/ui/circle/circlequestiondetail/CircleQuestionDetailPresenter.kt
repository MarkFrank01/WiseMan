package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/13
 * @Description :
 */
class CircleQuestionDetailPresenter(view: CircleQuestionDetailContract.View) : BasePresenter<CircleQuestionDetailContract.View>(), CircleQuestionDetailContract.Presenter {


    private val mModel: CircleQuestionDetailModel

    init {
        attachView(view)
        mModel = CircleQuestionDetailModel(this)
    }

    fun likeQA(qaId: Int) {
        mModel.likeQA(qaId)
    }

    fun unlikeQA(qaId: Int) {
        mModel.unlikeQA(qaId)
    }

    fun getAnswerList(qaId: Int, page: Int) {
        mModel.getAnswerList(qaId, page)
    }

    fun getQuestionInfo(qaId: Int) {
        mModel.getQuestionInfo(qaId)
    }

    fun likeQAOrQAComment_comment(qaId: Int, commentId: Int) {
        mModel.likeQAOrQAComment_comment(qaId, commentId)
    }

    fun unlikeQAOrQAComment_comment(qaId: Int, commentId: Int) {
        mModel.unlikeQAOrQAComment_comment(qaId, commentId)
    }

    override fun likeSuccess() {
        if (mView != null) {
            mView.likeSuccess()
        }
    }

    override fun unlikeSuccess() {
        if (mView != null) {
            mView.unlikeSuccess()
        }
    }

    override fun likeCreateSuccess() {
        if (mView != null) {
            mView.likeCreateSuccess()
        }
    }

    override fun unlikeCreateSuccess() {
        if (mView != null) {
            mView.unlikeCreateSuccess()
        }
    }

    override fun getBasicQuestionSuccess(bean: CircleDetailBean) {
        if (mView != null) {
            mView.getBasicQuestionSuccess(bean)
        }
    }

    override fun getCommentBeanSuccess(bean: MutableList<CircleCommentBean>) {
        if (mView != null) {
            mView.getCommentBeanSuccess(bean)
        }
    }

    override fun postSuccess(bean: CircleCommentBean) {
        mView.postSuccess(bean)
    }

    override fun postFail(msg: String?) {
    }


    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CircleCommentBean>) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }
}