package com.zxcx.zhizhe.ui.my.money

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class MyMoneyPresenter (view:MyMoneyContract.View):BasePresenter<MyMoneyContract.View>(),MyMoneyContract.Presenter{

    private val mModel:MyMoneyModel

    init {
        attachView(view)
        mModel = MyMoneyModel(this)
    }

    fun getAccountDetails(){
        mModel.getAccountDetails()
    }

    override fun getAccountDetailsSuccess(bean: MoneyBean) {
        if (mView!=null){
            mView.getAccountDetailsSuccess(bean)
        }
    }

    override fun applyForWithdrawalSuccess() {
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

    override fun getDataSuccess(bean: MoneyBean?) {
    }

    override fun startLogin() {
    }
}