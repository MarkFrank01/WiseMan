package com.zxcx.zhizhe.ui.my.invite

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
class MyInvitePresenter(view: MyInviteContract.View) : BasePresenter<MyInviteContract.View>(), MyInviteContract.Presenter {

    private val mModel: MyInviteModel

    init {
        attachView(view)
        mModel = MyInviteModel(this)
    }

    fun getInvitationHistory() {
        mModel.getInvitationHistory()
    }

    override fun getInvitationHistorySuccess(list: MutableList<InviteBean>) {
        if (mView != null) {
            mView.getInvitationHistorySuccess(list)
        }
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<InviteBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}