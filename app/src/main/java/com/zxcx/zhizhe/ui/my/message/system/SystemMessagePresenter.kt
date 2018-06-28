package com.zxcx.zhizhe.ui.my.message.system

import com.zxcx.zhizhe.mvpBase.BasePresenter

class SystemMessagePresenter(view: SystemMessageContract.View) : BasePresenter<SystemMessageContract.View>(), SystemMessageContract.Presenter {

	private val mModel: SystemMessageModel

	init {
		attachView(view)
		mModel = SystemMessageModel(this)
	}

	fun getSystemMessage(page: Int, pageSize: Int) {
		mModel.getSystemMessage(page, pageSize)
	}

	fun getRejectDetails(cardId: Int) {
		mModel.getRejectDetails(cardId)
	}

	override fun getDataSuccess(list: List<SystemMessageBean>) {
		mView.getDataSuccess(list)
	}

	override fun getDataFail(msg: String) {
		mView.toastFail(msg)
	}

	override fun getCardSuccess(cardId: Int) {
		mView.getCardSuccess(cardId)
	}

	override fun getCardNoFound() {
		mView.getCardNoFound()
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

