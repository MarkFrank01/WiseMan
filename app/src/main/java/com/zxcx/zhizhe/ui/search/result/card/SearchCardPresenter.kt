package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

class SearchCardPresenter(view: SearchCardContract.View) : BasePresenter<SearchCardContract.View>(), SearchCardContract.Presenter {

	private val mModel: SearchCardModel

	init {
		attachView(view)
		mModel = SearchCardModel(this)
	}

	fun searchCard(keyword: String, cardType: Int, page: Int) {
		mModel.searchCard(keyword, cardType, page)
	}

	override fun getDataSuccess(bean: MutableList<CardBean>) {
		mView.getDataSuccess(bean)
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

