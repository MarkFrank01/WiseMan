package com.zxcx.zhizhe.ui.card.label

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
import com.zxcx.zhizhe.ui.article.ArticleAndSubjectBean
import com.zxcx.zhizhe.ui.article.SubjectBean
import com.zxcx.zhizhe.ui.article.SubjectOnClickListener
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.article.subject.SubjectArticleActivity
import com.zxcx.zhizhe.ui.card.cardDetails.SingleCardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_card_bag.*
import java.util.*

class LabelActivity : RefreshMvpActivity<LabelPresenter>(), LabelContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
		SubjectOnClickListener {

	private lateinit var mAdapter: LabelAdapter
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

	override fun createPresenter(): LabelPresenter {
		return LabelPresenter(this)
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

	override fun getDataSuccess(list: List<ArticleAndSubjectBean>) {
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
		initToolBar(name)
	}

	private fun initView() {
		mAdapter = LabelAdapter(ArrayList(), this)
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_card_bag_card)
		mAdapter.onItemClickListener = this
		val view = EmptyView.getEmptyView(mActivity, "暂无内容", R.drawable.no_data)
		mAdapter.emptyView = view

		rv_card_bag_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_card_bag_card.adapter = mAdapter
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		val bean = adapter.data[position] as ArticleAndSubjectBean
		if (bean.itemType == ArticleAndSubjectBean.TYPE_ARTICLE) {
			val cardBean = bean.cardBean
			if (cardBean.cardType == 1) {
				mActivity.startActivity(SingleCardDetailsActivity::class.java) {
					it.putExtra("cardBean", cardBean)
				}
			} else {
				mActivity.startActivity(ArticleDetailsActivity::class.java) {
					it.putExtra("cardBean", cardBean)
				}
			}
		}
	}

	override fun articleOnClick(bean: CardBean) {
		startActivity(ArticleDetailsActivity::class.java) {
			it.putExtra("cardBean", bean)
		}
	}

	override fun subjectOnClick(bean: SubjectBean) {
		val intent = Intent(this, SubjectArticleActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		startActivity(intent)
	}

}
