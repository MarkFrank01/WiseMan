package com.zxcx.zhizhe.ui.card.hot

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AddCardDetailsListEvent
import com.zxcx.zhizhe.event.HomeClickRefreshEvent
import com.zxcx.zhizhe.event.UpdateCardListPositionEvent
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback
import com.zxcx.zhizhe.loadCallback.HomeNetworkErrorCallback
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.DefaultRefreshHeader
import kotlinx.android.synthetic.main.fragment_hot.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class HotCardFragment : RefreshMvpFragment<HotCardPresenter>(), HotCardContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

	private lateinit var mAdapter: CardAdapter
	private var mPage = 0
	private var mHidden = true
	private var mLastDate = Date()
	private var mIsReturning = false
	private var mCurrentPosition = 0

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val root = inflater.inflate(R.layout.fragment_hot, container, false)
		mRefreshLayout = root.findViewById(R.id.refresh_layout)
		val loadSir = LoadSir.Builder()
				.addCallback(HomeLoadingCallback())
				.addCallback(LoginTimeoutCallback())
				.addCallback(HomeNetworkErrorCallback())
				.setDefaultCallback(HomeLoadingCallback::class.java)
				.build()
		loadService = loadSir.register(root) { v ->
			loadService.showCallback(HomeLoadingCallback::class.java)
			getHotCard()
		}
		return loadService.loadLayout
	}

	override fun createPresenter(): HotCardPresenter {
		return HotCardPresenter(this)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		EventBus.getDefault().register(this)
		initRecyclerView()
		getHotCard()
		mHidden = false
	}

	override fun onHiddenChanged(hidden: Boolean) {
		super.setUserVisibleHint(hidden)
		mHidden = hidden
	}

	override fun onPause() {
		super.onPause()
		rv_hot_card.itemAnimator = null
	}

	override fun onDestroyView() {
		EventBus.getDefault().unregister(this)
		super.onDestroyView()
	}

	override fun clearLeaks() {
		loadService = null
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: HomeClickRefreshEvent) {
		if (!mHidden) {
			rv_hot_card.scrollToPosition(0)
			mPage = 0
			getHotCard()
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UpdateCardListPositionEvent) {
		if (this::class.java.name == event.sourceName) {
			if (event.currentPosition == mAdapter.data.size - 1) {
				getHotCard()
			}
			mCurrentPosition = event.currentPosition
			rv_hot_card.scrollToPosition(event.currentPosition)
		}
	}

	override fun onRefresh(refreshLayout: RefreshLayout?) {
		mPage = 0
		getHotCard()
	}

	override fun onLoadMoreRequested() {
		getHotCard()
	}

	override fun getDataSuccess(list: MutableList<CardBean>) {
		loadService.showSuccess()
		if (mPage == 0) {
			(mRefreshLayout.refreshHeader as DefaultRefreshHeader).setText(
					getString(R.string.tv_refresh_text, list.size))
			(mRefreshLayout.refreshHeader as DefaultRefreshHeader).setSuccess(true)
			mRefreshLayout.finishRefresh()
			mAdapter.setNewData(list)
			rv_hot_card.scrollToPosition(0)
		} else {
			mAdapter.addData(list)
			val event = AddCardDetailsListEvent(this::class.java.name, list as ArrayList<CardBean>)
			EventBus.getDefault().post(event)
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

	override fun toastFail(msg: String) {
		super.toastFail(msg)
		mAdapter.loadMoreFail()
		if (mPage == 0) {
			(mRefreshLayout.refreshHeader as DefaultRefreshHeader).setText("刷新失败")
			(mRefreshLayout.refreshHeader as DefaultRefreshHeader).setSuccess(false)
			mRefreshLayout.finishRefresh()
			loadService.showCallback(HomeNetworkErrorCallback::class.java)
		}
	}

	private fun initRecyclerView() {
		val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		mAdapter = CardAdapter(arrayListOf())
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_hot_card)
		mAdapter.onItemClickListener = this
		rv_hot_card.layoutManager = layoutManager
		rv_hot_card.adapter = mAdapter
	}

	private fun getHotCard() {
		mPresenter.getHotCard(mLastDate.time.toString(), mPage)
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
		val intent = Intent(mActivity, CardDetailsActivity::class.java)
		intent.putExtra("list", mAdapter.data as ArrayList)
		intent.putExtra("currentPosition", position)
		intent.putExtra("sourceName", this::class.java.name)
		mActivity.startActivityFromFragment(this, intent, 0, bundle)
	}

	public fun onActivityReenter() {
		mIsReturning = true
		postponeEnterTransition()
		rv_hot_card.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
			override fun onPreDraw(): Boolean {
				rv_hot_card.viewTreeObserver.removeOnPreDrawListener(this)
				rv_hot_card.requestLayout()
				startPostponedEnterTransition()
				return true
			}
		})
	}
}