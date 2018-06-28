package com.zxcx.zhizhe.ui.otherUser

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

class OtherUserPresenter(view: OtherUserContract.View) : BasePresenter<OtherUserContract.View>(), OtherUserContract.Presenter {

	private val mModel: OtherUserModel

	init {
		attachView(view)
		mModel = OtherUserModel(this)
	}

	fun getOtherUserInfo(id: Int) {
		mModel.getOtherUserInfo(id)
	}

	fun getOtherUserCreation(id: Int, sortType: Int, page: Int, pageSize: Int) {
		mModel.getOtherUserCreation(id, sortType, page, pageSize)
	}

	override fun getDataSuccess(bean: OtherUserInfoBean) {
		mView.getDataSuccess(bean)
	}

	override fun getOtherUserCreationSuccess(list: MutableList<CardBean>) {
		mView.getOtherUserCreationSuccess(list)
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

