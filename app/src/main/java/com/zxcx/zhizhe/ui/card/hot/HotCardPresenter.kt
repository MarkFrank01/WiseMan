package com.zxcx.zhizhe.ui.card.hot

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.welcome.ADBean

class HotCardPresenter(view: HotCardContract.View) : BasePresenter<HotCardContract.View>(), HotCardContract.Presenter {

	private val mModel: HotCardModel

	init {
		attachView(view)
		mModel = HotCardModel(this)
	}

	fun getHotCard(lastRefresh: String, page: Int) {
		mModel.getHotCard(lastRefresh, page)
	}

    fun getAD(){
        mModel.getAD()
    }

	override fun getDataSuccess(s: MutableList<CardBean>) {
		mView.getDataSuccess(s)
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

