package com.zxcx.zhizhe.ui.circle.createcircle

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.adapter.ManageCreateCircleAdapter
import com.zxcx.zhizhe.utils.Constants
import kotlinx.android.synthetic.main.activity_manage_circle_create.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class ManageCreateCircleActivity : RefreshMvpActivity<ManageCreatePresenter>(),ManageCreateContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var mPage = 0
    private lateinit var mAdapter: ManageCreateCircleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_circle_create)

        initView()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
    }

    override fun createPresenter(): ManageCreatePresenter {
        return ManageCreatePresenter(this)
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
    }

    private fun initView(){
        initToolBar("挑选作品(1/2)")

        mAdapter = ManageCreateCircleAdapter(ArrayList())

        rv_manage_circle_create.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_manage_circle_create.adapter = mAdapter
    }

    fun onRefresh(){
        getPushArc()
    }

    private fun getPushArc(){
        mPresenter.getPushArc(mPage,Constants.PAGE_SIZE)
    }
}