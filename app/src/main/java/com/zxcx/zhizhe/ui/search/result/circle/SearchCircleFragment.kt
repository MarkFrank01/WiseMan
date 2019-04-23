package com.zxcx.zhizhe.ui.search.result.circle

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetaileActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_result.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SearchCircleFragment : MvpFragment<SearchCirclePresenter>(), SearchCircleContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    var mPage = 0
    private lateinit var mAdapter:SearchCircleAdapter

    var mKeyword = ""
        set(value) {
            field = value
            mPage = 0
            mPresenter?.searchCircle(mKeyword, mPage)
        }

    override fun createPresenter(): SearchCirclePresenter {
        return SearchCirclePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mPresenter?.searchCircle(mKeyword,mPage)
    }

    override fun getDataSuccess(list: MutableList<CircleBean>) {
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无内容，换个关键词试试",R.drawable.iv_need_login)
        mAdapter.emptyView = emptyView

        mAdapter.mKeyword = mKeyword
        if (mPage == 0) {
            mAdapter.setNewData(list)
        } else {
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

    override fun onLoadMoreRequested() {
        mPresenter?.searchCircle(mKeyword,mPage)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when(view.id){
            R.id.cb_item_select_join_circle2,R.id.con_click -> {
                val bean = adapter.data[position] as CircleBean
                mActivity.startActivity(CircleDetaileActivity::class.java){
                    it.putExtra("circleID",bean.id)
                }

            }
        }
    }

    private fun initRecyclerView() {
        mAdapter = SearchCircleAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_search_result)
        rv_search_result.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_search_result.adapter = mAdapter

    }
}