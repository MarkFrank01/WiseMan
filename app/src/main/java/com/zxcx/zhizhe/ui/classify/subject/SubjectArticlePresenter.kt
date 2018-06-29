package com.zxcx.zhizhe.ui.classify.subject

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

class SubjectArticlePresenter(view: SubjectArticleContract.View) : BasePresenter<SubjectArticleContract.View>(), SubjectArticleContract.Presenter {

	private val mModel: SubjectArticleModel

	init {
		attachView(view)
		mModel = SubjectArticleModel(this)
	}

	fun getSubjectCardList(id: Int, page: Int, pageSize: Int) {
		mModel.getSubjectCardList(id, page, pageSize)
	}

	override fun getDataSuccess(bean: List<CardBean>) {
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

