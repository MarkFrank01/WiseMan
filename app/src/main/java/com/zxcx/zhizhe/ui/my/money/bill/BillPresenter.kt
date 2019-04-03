package com.zxcx.zhizhe.ui.my.money.bill

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillPresenter(view: BillContract.View) : BasePresenter<BillContract.View>(), BillContract.Presenter {

    private val mModel: BillModel

    init {
        attachView(view)
        mModel = BillModel(this)
    }

    fun getBillingDetails() {
        mModel.getBillingDetails()
    }

    fun getCashWithdrawalDetails() {
        mModel.getCashWithdrawalDetails()
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: MutableList<BillBean>?) {
        if (mView != null) {
            mView.getDataSuccess(bean)
        }
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}