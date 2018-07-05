package com.zxcx.zhizhe.ui.article

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.UpdateCardListPositionEvent
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.article.subject.SubjectArticleActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_card_list_item.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ArticleListItemFragment : BaseFragment(), IGetPresenter<MutableList<ArticleAndSubjectBean>>,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
		OnRefreshListener, SubjectOnClickListener {

	private lateinit var mAdapter: ArticleAndSubjectAdapter
	private var mPage = 0

	companion object {
		const val ARG_ID = "categoryId"
		@JvmStatic
		fun newInstance(id: Int) =
				ArticleListItemFragment().apply {
					arguments = Bundle().apply {
						putInt(ARG_ID, id)
					}
				}
	}

	private var categoryId: Int = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			categoryId = it.getInt(ARG_ID)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_card_list_item, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initRecyclerView()
		refresh_layout.setOnRefreshListener(this)
		getArticleListForCategory(categoryId, mPage)
	}

	private fun initRecyclerView() {
		val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		mAdapter = ArticleAndSubjectAdapter(arrayListOf(), this)
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_card_list_item)
		mAdapter.onItemClickListener = this
		rv_card_list_item.layoutManager = layoutManager
		rv_card_list_item.adapter = mAdapter
		rv_card_list_item.addItemDecoration(ArticleItemDecoration())
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		val bean = adapter.data[position] as ArticleAndSubjectBean
		if (bean.itemType == ArticleAndSubjectBean.TYPE_ARTICLE) {
			val articleImg = view.findViewById<ImageView>(R.id.iv_item_card_icon)
			val articleTitle = view.findViewById<TextView>(R.id.tv_item_card_title)
			val articleCategory = view.findViewById<TextView>(R.id.tv_item_card_category)
			val articleLabel = view.findViewById<TextView>(R.id.tv_item_card_label)
			val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
					Pair.create(articleImg, articleImg.transitionName),
					Pair.create(articleTitle, articleTitle.transitionName),
					Pair.create(articleCategory, articleCategory.transitionName),
					Pair.create(articleLabel, articleLabel.transitionName)).toBundle()
			val intent = Intent(mActivity, ArticleDetailsActivity::class.java)
			intent.putExtra("cardBean", bean.cardBean)
			mActivity.startActivity(intent, bundle)
		}
	}

	override fun onRefresh(refreshLayout: RefreshLayout?) {
		mPage = 0
		getArticleListForCategory(categoryId, mPage)
	}

	override fun onLoadMoreRequested() {
		getArticleListForCategory(categoryId, mPage)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UpdateCardListPositionEvent) {
		if (this::class.java.name == event.sourceName) {
			if (event.currentPosition == mAdapter.data.size - 1) {
				getArticleListForCategory(categoryId, mPage)
			}
			rv_card_list_item.scrollToPosition(event.currentPosition)
		}
	}

	override fun getDataSuccess(list: MutableList<ArticleAndSubjectBean>) {
		if (mPage == 0) {
			refresh_layout.finishRefresh()
			mAdapter.setNewData(list)
		} else {
			mAdapter.addData(list)
		}
		mPage++
		if (list.isEmpty()) {
			mAdapter.loadMoreEnd(false)
		} else {
			mAdapter.loadMoreComplete()
			mAdapter.setEnableLoadMore(false)
			mAdapter.setEnableLoadMore(true)
		}
	}

	override fun getDataFail(msg: String?) {
		refresh_layout.finishRefresh()
		mAdapter.loadMoreFail()
		toastFail(msg)
	}

	private fun getArticleListForCategory(cardCategoryId: Int, page: Int) {
		mDisposable = AppClient.getAPIService().getArticleListForCategory(cardCategoryId, page, Constants.PAGE_SIZE)
				.compose(BaseRxJava.handleArrayResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<MutableList<ArticleAndSubjectBean>>(this) {
					override fun onNext(t: MutableList<ArticleAndSubjectBean>) {
						getDataSuccess(t)
					}
				})
		addSubscription(mDisposable)
	}

	override fun articleOnClick(bean: CardBean) {
		mActivity.startActivity(ArticleDetailsActivity::class.java) {
			it.putExtra("cardBean", bean)
		}
	}

	override fun subjectOnClick(bean: SubjectBean) {
		val intent = Intent(mActivity, SubjectArticleActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		mActivity.startActivity(intent)
	}
}
