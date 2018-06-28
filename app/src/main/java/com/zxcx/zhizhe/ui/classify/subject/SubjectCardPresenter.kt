package com.zxcx.zhizhe.ui.classify.subject

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

class SubjectCardPresenter(view: SubjectCardContract.View) : BasePresenter<SubjectCardContract.View>(), SubjectCardContract.Presenter {

	private val mModel: SubjectCardModel

	init {
		attachView(view)
		mModel = SubjectCardModel(this)
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

