package com.zxcx.zhizhe.ui.circle.circleowner.ownercreatenext

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerCreateNextPresenter(view: OwnerCreateNextContract.View) : BasePresenter<OwnerCreateNextContract.View>(), OwnerCreateNextContract.Presenter {


    private val mModel: OwnerCreateNextModel

    init {
        attachView(view)
        mModel = OwnerCreateNextModel(this)
    }

    fun setCircleArticle(circleId: Int, auditArticleList: List<Int>, privateArticleList: List<Int>) {
        mModel.setCircleArticle(circleId, auditArticleList, privateArticleList)
    }

    fun createCircleNew(title: String, titleImage: String, classifyId: Int, sign: String, levelType: Int, limitedTimeType: Int) {
        mModel.createCircleNew(title, titleImage, classifyId, sign, levelType, limitedTimeType)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun createCircleSuccess(bean:CircleBean) {
        if (mView!=null){
            mView?.createCircleSuccess(bean)
        }
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
        if (mView != null) {
            mView?.getDataSuccess(bean)
        }
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}