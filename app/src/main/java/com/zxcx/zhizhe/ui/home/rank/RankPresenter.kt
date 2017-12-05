package com.zxcx.zhizhe.ui.home.rank

import com.zxcx.zhizhe.mvpBase.BasePresenter

class RankPresenter(view: RankContract.View) : BasePresenter<RankContract.View>(), RankContract.Presenter {

    private val mModel: RankModel

    init {
        attachView(view)
        mModel = RankModel(this)
    }

    fun getMyRank(){
        mModel.getMyRank()
    }

    override fun getDataSuccess(beanMy: RankBean) {
        mView.getDataSuccess(beanMy)
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

