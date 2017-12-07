package com.zxcx.zhizhe.ui.rank

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.home.rank.UserRankBean

class RankPresenter(view: RankContract.View) : BasePresenter<RankContract.View>(), RankContract.Presenter {

    private val mModel: RankModel

    init {
        attachView(view)
        mModel = RankModel(this)
    }

    fun getTopHundredRank(page : Int, pageSize : Int) {
        mModel.getTopHundredRank(page,pageSize)
    }

    override fun getDataSuccess(bean: List<UserRankBean>) {
        mView.getDataSuccess(bean)
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

