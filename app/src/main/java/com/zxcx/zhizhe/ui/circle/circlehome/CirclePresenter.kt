package com.zxcx.zhizhe.ui.circle.circlehome

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CirclePresenter(view: CircleContract.View):BasePresenter<CircleContract.View>(), CircleContract.Presenter {

    private val mModel: CircleModel

    init {
        attachView(view)
        mModel = CircleModel(this)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String?) {
        mView.toastFail(msg)
    }

    override fun startLogin() {
    }

}