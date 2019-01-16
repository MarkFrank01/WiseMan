package com.zxcx.zhizhe.ui.search.result.label

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SearchLabelPresenter(view:SearchLabelContract.View):BasePresenter<SearchLabelContract.View>(),SearchLabelContract.Presenter {

    private val mModel: SearchLabelModel

    init {
        attachView(view)
        mModel = SearchLabelModel(this)
    }

    fun searchLabel(keyword:String,page:Int){
        mModel.searchLabel(keyword,page)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun postSuccess(bean: SearchLabelBean?) {
        mView.postSuccess(bean)
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

    override fun getDataSuccess(bean: List<SearchLabelBean>?) {
        mView.getDataSuccess(bean)
    }

    override fun startLogin() {
    }

}