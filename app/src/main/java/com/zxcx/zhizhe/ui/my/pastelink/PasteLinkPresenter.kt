package com.zxcx.zhizhe.ui.my.pastelink

import com.zxcx.zhizhe.mvpBase.BasePresenter

class PasteLinkPresenter(view:PasteLinkContract.View):BasePresenter<PasteLinkContract.View>(),PasteLinkContract.Presenter{

    private val mModel : PasteModel

    init {
        attachView(view)
        mModel = PasteModel(this)
    }

    //上传链接操作,暂未处理
    fun pushLinkList(articleLinks: List<String>){
        mModel.pushLinkList(articleLinks)
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

    override fun getDataSuccess(bean: List<PastLinkBean>?) {
//        mView.getDataSuccess(bean)
    }

    override fun startLogin() {
        mView.startLogin()
    }

    override fun detachView() {
        super.detachView()
        mModel.onDestroy()
    }
}