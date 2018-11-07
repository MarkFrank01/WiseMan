package com.zxcx.zhizhe.ui.article.attention

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.article.ArticleAndSubjectBean
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean

class AttentionArticlePresenter(view: AttentionArticleContract.View) : BasePresenter<AttentionArticleContract.View>(), AttentionArticleContract.Presenter {

    private val mModel: AttentionArticleModel

	init {
		attachView(view)
		mModel = AttentionArticleModel(this)
	}

	fun getAttentionCard(page: Int) {
		mModel.getAttentionCard(page)
	}

	fun getClassify() {
		mModel.getClassify()
	}


	override fun getDataSuccess(list: List<ArticleAndSubjectBean>) {
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

