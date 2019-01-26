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
import com.zxcx.zhizhe.utils.LogCat
import kotlinx.android.synthetic.main.activity_manage_circle_create.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class ManageCreateCircleActivity : RefreshMvpActivity<ManageCreatePresenter>(), ManageCreateContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var mPage = 0
    private lateinit var mAdapter: ManageCreateCircleAdapter

    private var isFirstLong = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_circle_create)

        initView()
        onRefresh1()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        onRefresh1()
    }

    override fun createPresenter(): ManageCreatePresenter {
        return ManageCreatePresenter(this)
    }

    override fun getDataSuccess(list: MutableList<CardBean>) {
        LogCat.e("MyManage ${list.size}")
        mRefreshLayout.finishRefresh()
        if (mPage == 0) {
            list[0].showTitle = "卡片"
            for (index in list.indices){
                if (list[index].cardType == 2&&isFirstLong){
                    isFirstLong = false
                    list[index].showTitle = "长文"
                }
            }

            mAdapter.setNewData(list)
        } else {
            for (index in list.indices){
                if (list[index].cardType == 2&&isFirstLong){
                    isFirstLong = false
                    list[index].showTitle = "长文"
                }
            }

            mAdapter.addData(list)
        }

        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
    }

    private fun initView() {
        initToolBar("作品审核")

        mAdapter = ManageCreateCircleAdapter(ArrayList())

        rv_manage_circle_create.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_manage_circle_create.adapter = mAdapter
    }

    fun onRefresh1() {
        getPushArc()
    }

    private fun getPushArc() {
        mPresenter.getPushArc(mPage, Constants.PAGE_SIZE)
    }
}