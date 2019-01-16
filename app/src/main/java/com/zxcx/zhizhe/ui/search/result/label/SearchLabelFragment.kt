package com.zxcx.zhizhe.ui.search.result.label

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.UpdateLabelPositionEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_result.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SearchLabelFragment : MvpFragment<SearchLabelPresenter>(), SearchLabelContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {


    private var mPage = 0
    private lateinit var mAdapter: SearchLabelAdapter

    var mKeyword = ""
        set(value) {
            field = value
            mPage = 0
            mPresenter?.searchLabel(mKeyword, mPage)
        }

    override fun createPresenter(): SearchLabelPresenter {
        return SearchLabelPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initRecyclerView()
        mPresenter?.searchLabel(mKeyword, mPage)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun getDataSuccess(list: List<SearchLabelBean>) {
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

    override fun postSuccess(bean: SearchLabelBean?) {
    }

    override fun postFail(msg: String?) {
        toastShow(msg)
    }


    override fun onLoadMoreRequested() {
        mPresenter?.searchLabel(mKeyword,mPage)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event:UpdateLabelPositionEvent){
        if (this::class.java.name == event.sourceName) {
            if (event.currentPosition == mAdapter.data.size - 1) {
                mPresenter?.searchLabel(mKeyword, mPage)
            }
            rv_search_result.scrollToPosition(event.currentPosition)
        }
    }

    private fun initRecyclerView() {
        mAdapter = SearchLabelAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_search_result)
        rv_search_result.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_search_result.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity, "暂无内容，换个关键词试试", R.drawable.iv_need_login)
        mAdapter.emptyView = emptyView
    }
}