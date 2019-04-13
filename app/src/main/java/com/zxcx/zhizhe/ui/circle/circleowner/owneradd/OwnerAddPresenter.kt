package com.zxcx.zhizhe.ui.circle.circleowner.owneradd

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/12
 * @Description :
 */
class OwnerAddPresenter(view: OwnerAddContract.View) : BasePresenter<OwnerAddContract.View>(), OwnerAddContract.Presenter {

    private val mModel: OwnerAddModel

    init {
        attachView(view)
        mModel = OwnerAddModel(this)
    }

    fun getLockableArticleForAdd(styleType: Int, circleId: Int, pageIndex: Int, pageSize: Int) {
        mModel.getLockableArticleForAdd(styleType, circleId, pageIndex, pageSize)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CardBean>) {
        if (mView != null) {
            mView.getDataSuccess(bean)
        }
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}