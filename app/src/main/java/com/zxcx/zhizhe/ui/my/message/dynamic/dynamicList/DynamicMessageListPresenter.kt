package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import com.zxcx.zhizhe.mvpBase.BasePresenter

class DynamicMessageListPresenter(view: DynamicMessageListContract.View) : BasePresenter<DynamicMessageListContract.View>(), DynamicMessageListContract.Presenter {


	private val mModel: DynamicMessageListModel

	init {
		attachView(view)
		mModel = DynamicMessageListModel(this)
	}

	fun getDynamicMessageList(messageType: Int, page: Int, pageSize: Int) {
		mModel.getDynamicMessageList(messageType, page, pageSize)
	}

	fun deleteDynamicMessageList(messageType: Int) {
		mModel.deleteDynamicMessageList(messageType)
	}

	override fun getDataSuccess(bean: List<DynamicMessageListBean>) {
		mView.getDataSuccess(bean)
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

