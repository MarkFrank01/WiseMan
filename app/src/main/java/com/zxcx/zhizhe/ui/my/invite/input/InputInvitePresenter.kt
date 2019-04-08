package com.zxcx.zhizhe.ui.my.invite.input

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.my.invite.InviteBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/2
 * @Description :
 */
class InputInvitePresenter(view: InputInviteContract.View) : BasePresenter<InputInviteContract.View>(), InputInviteContract.Presenter {

    private val mModel: InputInviteModel

    init {
        attachView(view)
        mModel = InputInviteModel(this)
    }

    fun inputInvitationCode(code: String) {
        mModel.inputInvitationCode(code)
    }

    fun receiveMineInvitationUser() {
        mModel.receiveMineInvitationUser()
    }

    override fun receiveMineInvitationUser(bean: InviteBean) {
        if (mView != null) {
            mView.receiveMineInvitationUser(bean)
        }
    }

    override fun inputInvitationCodeSuccess(bean: InviteBean) {
        if (mView != null) {
            mView.inputInvitationCodeSuccess(bean)
        }
    }

    override fun errormsg(msg: String) {
        if (mView != null) {
            mView.errormsg(msg)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: InviteBean?) {
    }

    override fun getDataFail(msg: String?) {

    }

    override fun startLogin() {
    }


}