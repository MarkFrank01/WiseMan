package com.zxcx.zhizhe.pay.wx

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/3/25
 * @Description :
 */
class WXPresenter(view: WXEntryContract.View) : BasePresenter<WXEntryContract.View>(), WXEntryContract.Presenter {

    private val mModel: WXModel

    init {
        attachView(view)
        mModel = WXModel(this)
    }

    fun getWxOrderPay(circleId:Int) {
        mModel.getWxOrderPay(circleId)
    }

    override fun getWxOrderPaySuccess(bean: WXBean) {
        if (mView != null) {
            mView.getWxOrderPaySuccess(bean)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: WXBean?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}