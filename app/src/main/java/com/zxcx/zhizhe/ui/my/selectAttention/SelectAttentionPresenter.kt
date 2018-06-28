package com.zxcx.zhizhe.ui.my.selectAttention

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.classify.ClassifyBean

class SelectAttentionPresenter(view: SelectAttentionContract.View) : BasePresenter<SelectAttentionContract.View>(), SelectAttentionContract.Presenter {

	private val mModel: SelectAttentionModel

	init {
		attachView(view)
		mModel = SelectAttentionModel(this)
	}

	fun getClassify() {
		mModel.getClassify()
	}

	fun changeAttentionList(idList: MutableList<Int>) {
		mModel.changeAttentionList(idList)
	}

	override fun getDataSuccess(bean: MutableList<ClassifyBean>) {
		mView.getDataSuccess(bean)
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

