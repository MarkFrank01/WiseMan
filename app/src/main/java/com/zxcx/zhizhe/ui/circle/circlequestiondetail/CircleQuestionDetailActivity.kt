package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/13
 * @Description :
 */
class CircleQuestionDetailActivity : RefreshMvpActivity<CircleQuestionDetailPresenter>(), CircleQuestionDetailContract.View,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
    }

    override fun createPresenter(): CircleQuestionDetailPresenter {
        return CircleQuestionDetailPresenter(this)
    }

    override fun getBasicQuestionSuccess(bean: CircleDetailBean) {
    }

    override fun getDataSuccess(bean: CircleDetailBean?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
    }

}