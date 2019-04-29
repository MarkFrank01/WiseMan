package com.zxcx.zhizhe.ui.my.money.bill

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_bill_2.*

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillSecondFragment : RefreshMvpFragment<BillPresenter>(), BillContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private var mPage = 0
    private lateinit var mAdapter: BillDescAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bill_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRefreshLayout = refresh_layout
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
//        onRefresh()
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        onRefresh()
    }

    override fun createPresenter(): BillPresenter {
        return BillPresenter(this)
    }

    override fun getDataSuccess(list: MutableList<BillBean>) {
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无更多明细",R.drawable.no_more2)
        mAdapter.emptyView = emptyView

        mRefreshLayout.finishRefresh()

        if (mPage == 0) {
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }

        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun onLoadMoreRequested() {
        onRefresh()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    private fun onRefresh() {
        mPresenter.getCashWithdrawalDetails(mPage, Constants.PAGE_SIZE)
    }

    private fun initRecyclerView() {
        mAdapter = BillDescAdapter(ArrayList())

        rv_bill_second.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_bill_second.adapter = mAdapter

        mAdapter.onItemChildClickListener = this

        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_bill_second)
    }
}