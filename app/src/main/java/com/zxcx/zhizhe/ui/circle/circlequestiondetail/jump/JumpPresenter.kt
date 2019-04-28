package com.zxcx.zhizhe.ui.circle.circlequestiondetail.jump

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlequestiondetail.CircleCommentBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/28
 * @Description :
 */
class JumpPresenter(view:JumpContract.View):BasePresenter<JumpContract.View>(),JumpContract.Presenter {

    private val mModel:JumpModel

    init {
        attachView(view)
        mModel = JumpModel(this)
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

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun postSuccess(bean: CircleCommentBean?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: MutableList<CircleCommentBean>?) {
    }

    override fun startLogin() {
    }

}