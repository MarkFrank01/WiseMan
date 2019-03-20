package com.zxcx.zhizhe.ui.circle.circlesearch.inside.card

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsidePresenter(view: SearchInsideContract.View) : BasePresenter<SearchInsideContract.View>(), SearchInsideContract.Presenter {

    private val mModel: SearchInsideModel

    init {
        attachView(view)
        mModel = SearchInsideModel(this)
    }

    fun searchCard(page: Int, pageSize: Int, circleId: Int, cardType: Int, keyword: String) {
        mModel.searchCard(page, pageSize, circleId, cardType, keyword)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
        if (mView != null) {
            mView.getDataSuccess(bean)
        }
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}

