package com.zxcx.zhizhe.ui.article.subject

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.article.ArticleItemDecoration
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_subject.*

class SubjectArticleActivity : MvpActivity<SubjectArticlePresenter>(), SubjectArticleContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

	var mPage = 0
	var mId = 0
	var mPageSize = Constants.PAGE_SIZE
	var name = ""
	lateinit var mAdapter: SubjectArticleAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_subject)
		initData()
		initRecyclerView()
		mPresenter?.getSubjectCardList(mId, mPage, mPageSize)
	}

	private fun initData() {
		mId = intent.getIntExtra("id", 0)
		name = intent.getStringExtra("name")
		initToolBar(name)
	}

	override fun createPresenter(): SubjectArticlePresenter {
		return SubjectArticlePresenter(this)
	}

	override fun getDataSuccess(list: List<CardBean>) {
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
		mPresenter?.getSubjectCardList(mId, mPage, mPageSize)
	}

	private fun initRecyclerView() {
		mAdapter = SubjectArticleAdapter(ArrayList())
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.onItemClickListener = this
		mAdapter.setOnLoadMoreListener(this, rv_subject)
		rv_subject.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_subject.adapter = mAdapter
		rv_subject.addItemDecoration(ArticleItemDecoration())
		val emptyView = EmptyView.getEmptyView(mActivity, "暂无内容", R.drawable.no_data)
		mAdapter.emptyView = emptyView
	}

	override fun setListener() {
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		val bean = adapter.data[position] as CardBean
		val cardImg = view.findViewById<ImageView>(R.id.iv_item_card_icon)
		val cardTitle = view.findViewById<TextView>(R.id.tv_item_card_title)
		val cardCategory = view.findViewById<TextView>(R.id.tv_item_card_category)
		val cardLabel = view.findViewById<TextView>(R.id.tv_item_card_label)
		val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
				Pair.create(cardImg, cardImg.transitionName),
				Pair.create(cardTitle, cardTitle.transitionName),
				Pair.create(cardCategory, cardCategory.transitionName),
				Pair.create(cardLabel, cardLabel.transitionName)).toBundle()
		val intent = Intent(mActivity, ArticleDetailsActivity::class.java)
		intent.putExtra("cardBean", bean)
		mActivity.startActivity(intent, bundle)
	}
}
