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
import kotlinx.android.synthetic.main.fragment_bill.*

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillFristFragment : RefreshMvpFragment<BillPresenter>(), BillContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private var mPage = 0
    private lateinit var mAdapter:BillAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bill,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRefreshLayout = refresh_layout
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    override fun onLoadMoreRequested() {
        onRefresh()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }


    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        onRefresh()
    }

    override fun createPresenter(): BillPresenter {
        return BillPresenter(this)
    }

    override fun getDataSuccess(list: MutableList<BillBean>) {
        mRefreshLayout.finishRefresh()
        if (mPage == 0){
            mAdapter.setNewData(list)
        }else{
            mAdapter.addData(list)
        }

        mPage++
        if (list.size<Constants.PAGE_SIZE){
            mAdapter.loadMoreEnd(false)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    private fun onRefresh(){
        mPresenter.getBillingDetails(mPage,Constants.PAGE_SIZE)
    }


    private fun initRecyclerView(){
        mAdapter = BillAdapter(ArrayList())

        rv_bill_first.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_bill_first.adapter = mAdapter

        mAdapter.onItemChildClickListener = this

        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_bill_first)
    }
}