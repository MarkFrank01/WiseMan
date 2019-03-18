package com.zxcx.zhizhe.ui.circle.circledetaile.recommend

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/18
 * @Description :
 */
class CircleRecommendPresenter(view: CircleRecommendContract.View) : BasePresenter<CircleRecommendContract.View>(), CircleRecommendContract.Presenter {


    private val mModel: CircleRecommendModel

    init {
        attachView(view)
        mModel = CircleRecommendModel(this)
    }

    fun getArticleByCircleId(circleId:Int,orderType:Int,pageIndex:Int,pageSize:Int) {
        mModel.getArticleByCircleId(circleId, orderType, pageIndex, pageSize)
    }

    override fun getRecommendArcSuccess(bean: MutableList<CardBean>) {
        if (mView != null) {
            mView.getRecommendArcSuccess(bean)
        }
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }
}