package com.zxcx.zhizhe.ui.circle.circleowner.ownermanage

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerManageContentPresenter(view: OwnerManageContentContract.View) : BasePresenter<OwnerManageContentContract.View>(), OwnerManageContentContract.Presenter {

    private val mModel: OwnerManageContentModel

    init {
        attachView(view)
        mModel = OwnerManageContentModel(this)
    }

    fun getArticleByCircleId(circleId: Int, orderType: Int, pageIndex: Int, pageSize: Int) {
        mModel.getArticleByCircleId(circleId, orderType, pageIndex, pageSize)
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