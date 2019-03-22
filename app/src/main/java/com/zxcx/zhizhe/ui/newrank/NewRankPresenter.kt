package com.zxcx.zhizhe.ui.newrank

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.rank.UserRankBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class NewRankPresenter(view: NewRankContract.View) : BasePresenter<NewRankContract.View>(), NewRankContract.Presenter {

    private val mModel: NewRankModel

    init {
        attachView(view)
        mModel = NewRankModel(this)
    }

    fun getMyRank() {
        mModel.getMyRank()
    }

    fun getTopTenRank() {
        mModel.getTopTenRank()
    }

    override fun getMyRankSuccess(bean: UserRankBean) {
        if (mView != null) {
            mView.getMyRankSuccess(bean)
        }
    }

    override fun getTopTenRankSuccess(bean: List<UserRankBean>) {
        if (mView != null) {
            mView.getTopTenRankSuccess(bean)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: List<UserRankBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}