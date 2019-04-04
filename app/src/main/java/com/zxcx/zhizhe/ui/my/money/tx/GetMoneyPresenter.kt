package com.zxcx.zhizhe.ui.my.money.tx

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.my.money.MoneyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class GetMoneyPresenter(view: GetMoneyContract.View) : BasePresenter<GetMoneyContract.View>(), GetMoneyContract.Presenter {


    private val mModel: GetMoneyModel

    init {
        attachView(view)
        mModel = GetMoneyModel(this)
    }

    fun applyForWithdrawal(money: String) {
        mModel.applyForWithdrawal(money)
    }

    override fun applyForWithdrawalSuccess() {
        if (mView != null) {
            mView.applyForWithdrawalSuccess()
        }
    }

    override fun nomoreMoney() {
        if (mView!=null){
            mView.nomoreMoney()
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

    override fun getDataSuccess(bean: MoneyBean?) {
    }

    override fun startLogin() {
    }

}