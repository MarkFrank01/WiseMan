package com.zxcx.zhizhe.ui.card.cardBag

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.LoadingCallback
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.article.attention.SubjectBean
import com.zxcx.zhizhe.ui.article.attention.SubjectOnClickListener
import com.zxcx.zhizhe.ui.card.hot.CardAdapter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.classify.subject.SubjectArticleActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_card_bag.*
import java.util.*

class CardBagActivity : RefreshMvpActivity<CardBagPresenter>(), CardBagContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
		SubjectOnClickListener {

	private lateinit var mAdapter: CardAdapter
	private var mId: Int = 0
	private var page = 0
	private var name = ""

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_card_bag)
		ButterKnife.bind(this)
		initData()
		initView()
		onRefresh()

		initLoadSir()
	}

	override fun createPresenter(): CardBagPresenter {
		return CardBagPresenter(this)
	}

	private fun initLoadSir() {
		val loadSir = LoadSir.Builder()
				.addCallback(LoadingCallback())
				.addCallback(NetworkErrorCallback())
				.addCallback(LoginTimeoutCallback())
				.setDefaultCallback(LoadingCallback::class.java)
				.build()
		loadService = loadSir.register(mRefreshLayout, this)
	}

	override fun onReload(v: View) {
		loadService.showCallback(LoadingCallback::class.java)
		onRefresh()
	}

	override fun onRefresh(refreshLayout: RefreshLayout) {
		onRefresh()
	}

	fun onRefresh() {
		page = 0
		getCardBagCardList()
	}

	override fun onLoadMoreRequested() {
		getCardBagCardList()
	}

	override fun getDataSuccess(list: List<CardBean>) {
		loadService.showSuccess()
		mRefreshLayout.finishRefresh()
		if (page == 0) {
			mAdapter.setNewData(list)
		} else {
			mAdapter.addData(list)
		}
		page++
		if (list.size < Constants.PAGE_SIZE) {
			mAdapter.loadMoreEnd(false)
		} else {
			mAdapter.loadMoreComplete()
			mAdapter.setEnableLoadMore(false)
			mAdapter.setEnableLoadMore(true)
		}
	}

	override fun toastFail(msg: String) {
		if (page == 0) {
			loadService.showCallback(NetworkErrorCallback::class.java)
		}
		super.toastFail(msg)
	}

	private fun getCardBagCardList() {
		mPresenter.getCardBagCardList(mId, page, Constants.PAGE_SIZE)
	}

	private fun initData() {
		mId = intent.getIntExtra("id", 0)
		name = intent.getStringExtra("name")
		tv_header_title.text = name
	}

	private fun initView() {
		mAdapter = CardAdapter(ArrayList())
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_card_bag_card)
		mAdapter.onItemClickListener = this
		val view = EmptyView.getEmptyView(mActivity, "暂无内容", R.drawable.no_data)
		mAdapter.emptyView = view

		rv_card_bag_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_card_bag_card.adapter = mAdapter
	}

	override fun setListener() {
		iv_common_close.setOnClickListener {
			onBackPressed()
		}
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		/*val CardBean = adapter.data[position] as CardBean
		if (CardBean.itemType == CardBean.TYPE_CARD) {
			val bean = CardBean.cardBean
			val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
					Pair.create(view.findViewById(R.id.iv_item_card_icon), "cardImage"),
					Pair.create(view.findViewById(R.id.tv_item_card_title), "cardTitle"),
					Pair.create(view.findViewById(R.id.tv_item_card_category), "cardBag")).toBundle()
			val intent = Intent(this, ArticleDetailsActivity::class.java)
			intent.putExtra("id", bean.id)
			intent.putExtra("name", bean.name)
			intent.putExtra("imageUrl", bean.imageUrl)
			intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
			intent.putExtra("authorName", bean.authorName)
			startActivity(intent, bundle)
		}*/
	}

	override fun articleOnClick(bean: CardBean) {
		val intent = Intent(this, ArticleDetailsActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		intent.putExtra("imageUrl", bean.imageUrl)
		intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
		intent.putExtra("authorName", bean.authorName)
		startActivity(intent)
	}

	override fun subjectOnClick(bean: SubjectBean) {
		val intent = Intent(this, SubjectArticleActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		startActivity(intent)
	}

}
