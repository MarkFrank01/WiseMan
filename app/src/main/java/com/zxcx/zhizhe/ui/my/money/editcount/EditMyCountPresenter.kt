package com.zxcx.zhizhe.ui.my.money.editcount

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.my.invite.InviteBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/2
 * @Description :
 */
class EditMyCountPresenter(view: EditMyCountContract.View) : BasePresenter<EditMyCountContract.View>(), EditMyCountContract.Presenter {

    private val mModel: EditMyCountModel

    init {
        attachView(view)
        mModel = EditMyCountModel(this)
    }

    fun bindingAlipay(withdrawalAmount: String, contact: String, phone: String) {
        mModel.bindingAlipay(withdrawalAmount, contact, phone)
    }

    override fun bindingAlipaySuccess() {
        if (mView != null) {
            mView.bindingAlipaySuccess()
        }
    }

    override fun showLoading() {
    }

    override fun postSuccess() {
    }

    override fun hideLoading() {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: InviteBean?) {
    }

    override fun startLogin() {
    }


}