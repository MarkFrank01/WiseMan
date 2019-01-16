package com.zxcx.zhizhe.ui.search.result.circle

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SearchCirclePresenter(view:SearchCircleContract.View):BasePresenter<SearchCircleContract.View>(),SearchCircleContract.Presenter {

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