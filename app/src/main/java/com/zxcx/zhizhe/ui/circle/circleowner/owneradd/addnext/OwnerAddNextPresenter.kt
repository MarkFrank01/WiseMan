package com.zxcx.zhizhe.ui.circle.circleowner.owneradd.addnext

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/15
 * @Description :
 */
class OwnerAddNextPresenter(view: OwnerAddNextContract.View) : BasePresenter<OwnerAddNextContract.View>(), OwnerAddNextContract.Presenter {

    private val mModel: OwnerAddNextModel

    init {
        attachView(view)
        mModel = OwnerAddNextModel(this)
    }

    fun setCircleArticle(circleId: Int, auditArticleList: List<Int>, privateArticleList: List<Int>) {
        mModel.setCircleArticle(circleId, auditArticleList, privateArticleList)
    }

    fun checkCircleArticleBalance(circleId: Int) {
        mModel.checkCircleArticleBalance(circleId)
    }

    override fun setArcSuccess(bean: MutableList<CardBean>) {
        if (mView!=null){
            mView.setArcSuccess(bean)
        }
    }

    override fun checkCircleArticleBalanceSuccess(bean: BalanceBean) {
        if (mView!=null){
            mView.checkCircleArticleBalanceSuccess(bean)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}