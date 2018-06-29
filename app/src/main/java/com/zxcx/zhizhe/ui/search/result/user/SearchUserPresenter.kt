package com.zxcx.zhizhe.ui.search.result.user

import com.zxcx.zhizhe.mvpBase.BasePresenter

class SearchUserPresenter(view: SearchUserContract.View) : BasePresenter<SearchUserContract.View>(), SearchUserContract.Presenter {

	private val mModel: SearchUserModel

	init {
		attachView(view)
		mModel = SearchUserModel(this)
	}

	fun searchUser(keyword: String, page: Int) {
		mModel.searchUser(keyword, page)
	}

	fun followUser(authorId: Int) {
		mModel.followUser(authorId)
	}

	fun unFollowUser(authorId: Int) {
		mModel.unFollowUser(authorId)
	}

	override fun getDataSuccess(bean: List<SearchUserBean>) {
		mView.getDataSuccess(bean)
	}

	override fun getDataFail(msg: String) {
		mView.toastFail(msg)
	}

	override fun postSuccess(bean: SearchUserBean) {
		mView.postSuccess(bean)
	}

	override fun unFollowUserSuccess(bean: SearchUserBean) {
		mView.unFollowUserSuccess(bean)
	}

	override fun postFail(msg: String) {
		mView.postFail(msg)
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

