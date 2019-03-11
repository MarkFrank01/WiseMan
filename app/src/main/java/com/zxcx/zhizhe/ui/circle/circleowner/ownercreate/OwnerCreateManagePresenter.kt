package com.zxcx.zhizhe.ui.circle.circleowner.ownercreate

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerCreateManagePresenter(view: OwnerCreateManageContract.View) : BasePresenter<OwnerCreateManageContract.View>(), OwnerCreateManageContract.Presenter {

    private val mModel: OwnerCreateManageModel

    init {
        attachView(view)
        mModel = OwnerCreateManageModel(this)
    }

    fun getLockableArticleForCreate(styleType: Int, classifyId: Int, pageIndex: Int, pageSize: Int) {
        mModel.getLockableArticleForCreate(styleType, classifyId, pageIndex, pageSize)
    }


    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
        if (mView != null) {
            mView?.getDataSuccess(bean)
        }
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}