package com.zxcx.zhizhe.ui.article

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.cardList.CardCategoryBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class HomeArticlePresenter(view:HomeArticleContract.View):BasePresenter<HomeArticleContract.View>(),HomeArticleContract.Presenter{

    private val mModel:HomeArticleModel

    init {
        attachView(view)
        mModel = HomeArticleModel(this)
    }

    fun getAD(lastOpenedTime: Long, lastOpenedAdId: Long){
        mModel.getAD(lastOpenedTime, lastOpenedAdId)
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        mView.getADSuccess(list)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CardCategoryBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}