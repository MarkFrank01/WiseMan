package com.zxcx.zhizhe.ui.my.intelligenceValue

import com.zxcx.zhizhe.mvpBase.BasePresenter

class IntelligenceValuePresenter(view: IntelligenceValueContract.View) : BasePresenter<IntelligenceValueContract.View>(), IntelligenceValueContract.Presenter {

	private val mModel: IntelligenceValueModel

	init {
		attachView(view)
		mModel = IntelligenceValueModel(this)
	}

	fun getIntelligenceValue() {
		mModel.getIntelligenceValue()
	}

	override fun getDataSuccess(bean: IntelligenceValueBean) {
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

