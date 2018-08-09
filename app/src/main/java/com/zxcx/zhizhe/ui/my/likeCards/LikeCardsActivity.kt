package com.zxcx.zhizhe.ui.my.likeCards

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.UnLikeEvent
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardDetails.SingleCardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.SortWindow
import com.zxcx.zhizhe.ui.my.readCards.MyCardItemDecoration
import com.zxcx.zhizhe.ui.my.readCards.MyCardsAdapter
import com.zxcx.zhizhe.ui.my.readCards.ReadCardsContract
import com.zxcx.zhizhe.ui.my.readCards.ReadCardsPresenter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_read_cards.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的-点赞文章页面
 */

class LikeCardsActivity : MvpActivity<ReadCardsPresenter>(), ReadCardsContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, SwipeMenuClickListener,
		SortWindow.SortListener {

	private var mPage = 0
	private var mSortType = 0
	private val mTabType = 1 //tabType 标签类型 0阅读 1点赞 3收藏
	private var mPageSize = Constants.PAGE_SIZE
	private lateinit var mAdapter: MyCardsAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_read_cards)
		EventBus.getDefault().register(this)
		initToolBar("点赞")
		initView()
		initLoadSir()
		mPresenter.getLikeCard(mSortType, mPage, mPageSize)
		mPresenter.getEmptyRecommendCard(mTabType)
	}

	override fun onDestroy() {
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	private fun initLoadSir() {
		loadService = LoadSir.getDefault().register(rv_read_card, this)
	}

	override fun createPresenter(): ReadCardsPresenter {
		return ReadCardsPresenter(this)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UnLikeEvent) {
		mPage = 0
		mPresenter.getLikeCard(mSortType, mPage, mPageSize)
	}

	override fun onReload(v: View?) {
		super.onReload(v)
		mPage = 0
		mPresenter.getLikeCard(mSortType, mPage, mPageSize)
	}

	override fun getEmptyRecommendCardSuccess(bean: CardBean) {
		val emptyView = EmptyView.getEmptyViewAndCard(mActivity, "暂无内容", R.drawable.no_data, bean)
		mAdapter.emptyView = emptyView
	}

	override fun getDataSuccess(list: List<CardBean>) {
		if (mPage == 0) {
			loadService.showSuccess()
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

	override fun toastFail(msg: String) {
		super.toastFail(msg)
		mAdapter.loadMoreFail()
		if (mPage == 0) {
			loadService.showCallback(NetworkErrorCallback::class.java)
		}
	}

	override fun postSuccess() {
		//删除成功
	}

	override fun postFail(msg: String?) {
		toastError(msg)
	}

	override fun onLoadMoreRequested() {
		mPresenter.getLikeCard(mSortType, mPage, mPageSize)
	}

	override fun defaultSort() {
		mSortType = 0
		mPage = 0
		mPresenter.getLikeCard(mSortType, mPage, mPageSize)
	}

	override fun earliestSort() {
		mSortType = 1
		mPage = 0
		mPresenter.getLikeCard(mSortType, mPage, mPageSize)
	}

	override fun onContentClick(position: Int) {
		val bean = mAdapter.data[position] as CardBean
		if (bean.cardType == 1) {
			mActivity.startActivity(SingleCardDetailsActivity::class.java) {
				it.putExtra("cardBean", bean)
			}
		} else {
			mActivity.startActivity(ArticleDetailsActivity::class.java) {
				it.putExtra("cardBean", bean)
			}
		}
	}

	override fun onDeleteClick(position: Int) {
		mPresenter.deleteLikeCard(mAdapter.data[position].id)
	}

	private fun initView() {
		iv_toolbar_right.visibility = View.VISIBLE
		iv_toolbar_right.setImageResource(R.drawable.iv_toolbar_more)

		mAdapter = MyCardsAdapter(ArrayList())
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_read_card)
		mAdapter.mListener = this
		rv_read_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_read_card.adapter = mAdapter
		rv_read_card.addItemDecoration(MyCardItemDecoration())
	}

	override fun setListener() {
		iv_toolbar_right.setOnClickListener {
			val sortWindow = SortWindow(mActivity)
			sortWindow.mListener = this
			sortWindow.sortType = mSortType
			sortWindow.showAsDropDown(iv_toolbar_right, 0, -ScreenUtils.dip2px(20f))
		}
	}
}