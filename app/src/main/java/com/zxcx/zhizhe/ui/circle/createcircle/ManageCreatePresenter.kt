package com.zxcx.zhizhe.ui.circle.createcircle

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.LogCat

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
        LogCat.e("FUCK")
        mModel.getPublishableArticle(page, pageSize)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CardBean>) {
        LogCat.e("MODEL FUCK BACK")
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}