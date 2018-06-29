package com.zxcx.zhizhe.ui.search.result.article

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.search.result.card.SearchCardContract
import com.zxcx.zhizhe.ui.search.result.card.SearchCardPresenter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_result.*

class SearchArticleFragment : MvpFragment<SearchCardPresenter>(), SearchCardContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

	var mPage = 0
	var cardType = 1 //卡片类型 0卡片 1为长文
	private lateinit var mAdapter: SearchArticleAdapter

	var mKeyword = ""
		set(value) {
			field = value
			mPage = 0
			mPresenter?.searchCard(mKeyword, cardType, mPage)
		}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_search_result, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initRecyclerView()
		mPresenter?.searchCard(mKeyword, cardType, mPage)
	}

	override fun createPresenter(): SearchCardPresenter {
		return SearchCardPresenter(this)
	}

	override fun getDataSuccess(list: MutableList<CardBean>) {
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
		mPresenter.searchCard(mKeyword, cardType, mPage)
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		//todo 长文详情
	}

	private fun initRecyclerView() {
		mAdapter = SearchArticleAdapter(ArrayList())
		mAdapter.onItemClickListener = this
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_search_result)
		rv_search_result.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_search_result.adapter = mAdapter
		val emptyView = EmptyView.getEmptyViewAndClick(mActivity, "暂无搜索长文", "换个关键词试试", R.drawable.search_no_data, View.OnClickListener {

		})
		mAdapter.emptyView = emptyView
	}
}