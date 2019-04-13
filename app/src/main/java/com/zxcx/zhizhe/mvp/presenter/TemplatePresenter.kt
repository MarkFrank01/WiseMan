package com.zxcx.zhizhe.mvp.presenter

import com.zxcx.zhizhe.mvp.contract.TemplateContract
import com.zxcx.zhizhe.mvp.model.TemplateModel
import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2018/12/6
 * @Description :
 */
class TemplatePresenter(view:TemplateContract.View):BasePresenter<TemplateContract.View>(),TemplateContract.Presenter {

    private val mModel : TemplateModel

    init {
        attachView(view)
        mModel = TemplateModel(this)
    }

    fun ChooseSuccess(){
        postSuccess()
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun postSuccess() {
        mView.postSuccess()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataFail(msg: String?) {
        mView.toastFail(msg)
    }

    override fun postFail(msg: String?) {
        mView.postFail(msg)
    }

    override fun getDataSuccess(bean: List<CardBean>) {
        mView.getDataSuccess(listOf())
    }

    override fun startLogin() {
    }
}