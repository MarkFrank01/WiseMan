package com.zxcx.zhizhe.ui.search.result.circle

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SearchCirclePresenter(view: SearchCircleContract.View) : BasePresenter<SearchCircleContract.View>(), SearchCircleContract.Presenter {

    private val mModel: SearchCircleModel

    init {
        attachView(view)
        mModel = SearchCircleModel(this)
    }

    fun searchCircle(keyword: String, page: Int) {
        mModel.searchCircle(keyword, page)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
        if (mView != null) {
            mView.getDataSuccess(bean)
        }
    }


    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }


}