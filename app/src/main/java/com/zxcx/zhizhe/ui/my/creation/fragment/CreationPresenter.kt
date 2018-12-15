package com.zxcx.zhizhe.ui.my.creation.fragment

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

class CreationPresenter(view: CreationContract.View) : BasePresenter<CreationContract.View>(), CreationContract.Presenter {

	private val mModel: CreationModel

	init {
		attachView(view)
		mModel = CreationModel(this)
	}

	fun getCreation(passType: Int, page: Int, pageSize: Int) {
		mModel.getCreation(passType, page, pageSize)
	}

	fun deleteCard(cardId: Int) {
		mModel.deleteCard(cardId)
	}

    fun deleteLink(cardId: Int){
        mModel.deleteLink(cardId)
    }

	override fun getDataSuccess(bean: List<CardBean>) {
		mView.getDataSuccess(bean)
	}

	override fun getDataFail(msg: String) {
		mView.getDataFail(msg)
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

