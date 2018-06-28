package com.zxcx.zhizhe.ui.home.rank

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.welcome.ADBean

class RankPresenter(view: RankContract.View) : BasePresenter<RankContract.View>(), RankContract.Presenter {

	private val mModel: RankModel

	init {
		attachView(view)
		mModel = RankModel(this)
	}

	fun getMyRank() {
		mModel.getMyRank()
	}

	fun getAD() {
		mModel.getAD()
	}

	fun getTopTenRank() {
		mModel.getTopTenRank()
	}

	override fun getMyRankSuccess(bean: UserRankBean) {
		mView.getMyRankSuccess(bean)
	}

	override fun getDataSuccess(beanMy: List<UserRankBean>) {
		mView.getDataSuccess(beanMy)
	}

	override fun getADSuccess(list: MutableList<ADBean>) {
		mView.getADSuccess(list)
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

