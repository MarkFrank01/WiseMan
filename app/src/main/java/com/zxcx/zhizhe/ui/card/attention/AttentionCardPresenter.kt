package com.zxcx.zhizhe.ui.card.attention

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.classify.ClassifyBean

class AttentionCardPresenter(view: AttentionCardContract.View) : BasePresenter<AttentionCardContract.View>(), AttentionCardContract.Presenter {

	private val mModel: AttentionCardModel

	init {
		attachView(view)
		mModel = AttentionCardModel(this)
	}

	fun getAttentionCard(page: Int, pageSize: Int) {
		mModel.getAttentionCard(page, pageSize)
	}

	fun getClassify() {
		mModel.getClassify()
	}

	override fun getDataSuccess(list: List<CardBean>) {
		mView.getDataSuccess(list)
	}

	fun changeAttentionList(idList: MutableList<Int>) {
		mModel.changeAttentionList(idList)
	}

	override fun getClassifySuccess(list: List<ClassifyBean>) {
		mView.getClassifySuccess(list)
	}

	override fun getDataFail(msg: String) {
		mView.toastFail(msg)
	}

	override fun postSuccess() {
		mView.postSuccess()
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

