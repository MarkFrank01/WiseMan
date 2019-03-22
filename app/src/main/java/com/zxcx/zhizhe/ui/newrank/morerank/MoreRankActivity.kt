package com.zxcx.zhizhe.ui.newrank.morerank

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.activity_more_rank.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class MoreRankActivity : MvpActivity<MoreRankPresenter>(), MoreRankContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var page = 0
    private lateinit var mAdapter: MoreRankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_rank)
        initToolBar("查看更多榜单")

        initRecyclerView()
        onRefresh()
    }

    override fun createPresenter(): MoreRankPresenter {
        return MoreRankPresenter(this)
    }

    override fun followUserSuccess(bean: SearchUserBean) {
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
    }

    override fun getMoreRankSuccess(list: List<UserRankBean>) {
        if (page == 0) {
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }

        page++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getDataSuccess(bean: List<UserRankBean>?) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
        onRefresh()
    }

    private fun initRecyclerView() {
        mAdapter = MoreRankAdapter(arrayListOf())
        mAdapter.onItemChildClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_more_rank)
        rv_more_rank.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_more_rank.adapter = mAdapter
    }

    private fun onRefresh() {
        mPresenter.getMoreRank(page, Constants.PAGE_SIZE)
    }
}