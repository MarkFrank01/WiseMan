package com.zxcx.zhizhe.ui.comment

import com.zxcx.zhizhe.mvpBase.BasePresenter

class CommentPresenter(view: CommentContract.View) : BasePresenter<CommentContract.View>(), CommentContract.Presenter {

	private val mModel: CommentModel

	init {
		attachView(view)
		mModel = CommentModel(this)
	}

	fun getComment(articleId: Int, page: Int) {
		mModel.getComment(articleId, page)
	}

	fun sendComment(articleId: Int, parentCommentId: Int?, content: String) {
		mModel.sendComment(articleId, parentCommentId, content)
	}

	fun likeComment(commentId: Int) {
		mModel.likeComment(commentId)
	}

	fun unlikeComment(commentId: Int) {
		mModel.unlikeComment(commentId)
	}

	override fun getDataSuccess(list: MutableList<CommentBean>) {
		mView.getDataSuccess(list)
	}

	override fun getDataFail(msg: String) {
		mView.toastFail(msg)
	}

	override fun postSuccess() {
		mView.postSuccess()
	}

	override fun postFail(msg: String?) {
		mView.postFail(msg)
	}

	override fun likeSuccess() {
		mView.likeSuccess()
	}

	override fun unlikeSuccess() {
		mView.unlikeSuccess()
	}

	override fun showLoading() {
		mView.showLoading()
	}

	override fun hideLoading() {
		mView.hideLoading()
	}

	override fun startLogin() {
		mView.startLogin()
	}

	override fun detachView() {
		super.detachView()
		mModel.onDestroy()
	}
}

