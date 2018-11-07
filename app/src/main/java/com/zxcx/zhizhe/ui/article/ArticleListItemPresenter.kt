package com.zxcx.zhizhe.ui.article

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class ArticleListItemPresenter(view: ArticleListItemContract.View):BasePresenter<ArticleListItemContract.View>(),ArticleListItemContract.Presenter{

    private val mModel:ArticleListItemModel

    init {
        attachView(view)
        mModel = ArticleListItemModel(this)
    }

    fun getAD(position:Int){
        mModel.getAD(position)
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

    override fun getDataSuccess(bean: MutableList<ArticleAndSubjectBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}