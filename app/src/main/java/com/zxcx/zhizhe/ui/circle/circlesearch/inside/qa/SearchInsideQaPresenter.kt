package com.zxcx.zhizhe.ui.circle.circlesearch.inside.qa

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideQaPresenter(view: SearchInsideQaContract.View) : BasePresenter<SearchInsideQaContract.View>(), SearchInsideQaContract.Presenter {

    private val mModel: SearchInsideQaModel

    init {
        attachView(view)
        mModel = SearchInsideQaModel(this)
    }

    fun searchCircleQA(page: Int, pageSize: Int, circleId: Int, keyword: String) {
        mModel.searchCircleQA(page, pageSize, circleId, keyword)
    }

    override fun getCircleQAByCircleIdSuccess(bean: MutableList<CircleDetailBean>) {
        mView.getCircleQAByCircleIdSuccess(bean)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}