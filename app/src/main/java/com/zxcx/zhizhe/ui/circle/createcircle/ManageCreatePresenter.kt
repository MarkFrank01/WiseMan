package com.zxcx.zhizhe.ui.circle.createcircle

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class ManageCreatePresenter(view:ManageCreateContract.View):BasePresenter<ManageCreateContract.View>(),ManageCreateContract.Presenter{

    private val mModel : ManageCreateModel

    init {
        attachView(view)
        mModel = ManageCreateModel(this)
    }

    fun getPushArc(page:Int,pageSize:Int){
        mModel.getPublishableArticle(page, pageSize)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CardBean>) {
        if (mView!=null) {
            mView.getDataSuccess(bean)
        }
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}