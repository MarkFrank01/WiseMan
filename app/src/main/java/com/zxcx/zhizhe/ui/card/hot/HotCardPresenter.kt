package com.zxcx.zhizhe.ui.card.hot

import com.zxcx.zhizhe.mvpBase.BasePresenter

class HotCardPresenter(view: HotCardContract.View) : BasePresenter<HotCardContract.View>(), HotCardContract.Presenter {

	private val mModel: HotCardModel

	init {
		attachView(view)
		mModel = HotCardModel(this)
	}

	fun getHotCard(lastRefresh: String, page: Int) {
		mModel.getHotCard(lastRefresh, page)
	}

	override fun getDataSuccess(s: MutableList<CardBean>) {
		mView.getDataSuccess(s)
	}

	override fun getDataFail(msg: String) {
		mView.toastFail(msg)
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

