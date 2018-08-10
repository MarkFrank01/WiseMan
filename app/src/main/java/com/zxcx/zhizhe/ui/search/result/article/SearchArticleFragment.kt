package com.zxcx.zhizhe.ui.search.result.article

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.article.ArticleItemDecoration
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.search.result.card.SearchCardContract
import com.zxcx.zhizhe.ui.search.result.card.SearchCardPresenter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_result.*

/**
 *搜索长文结果页面
 */

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
		val bean = adapter.data[position] as CardBean
		mActivity.startActivity(ArticleDetailsActivity::class.java) {
			it.putExtra("cardBean", bean)
		}
	}

	private fun initRecyclerView() {
		mAdapter = SearchArticleAdapter(ArrayList())
		mAdapter.onItemClickListener = this
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_search_result)
		rv_search_result.setBackgroundResource(R.color.strip)
		rv_search_result.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_search_result.adapter = mAdapter
		rv_search_result.addItemDecoration(ArticleItemDecoration())
		val emptyView = EmptyView.getEmptyView(mActivity, "暂无内容，换个关键词试试", R.drawable.iv_need_login)
		mAdapter.emptyView = emptyView
	}
}