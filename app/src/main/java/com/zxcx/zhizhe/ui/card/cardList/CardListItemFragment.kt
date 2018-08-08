package com.zxcx.zhizhe.ui.card.cardList

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
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AddCardDetailsListEvent
import com.zxcx.zhizhe.event.UpdateCardListEvent
import com.zxcx.zhizhe.event.UpdateCardListPositionEvent
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardAdapter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_card_list_item.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private const val ARG_ID = "categoryId"

class CardListItemFragment : BaseFragment(), IGetPresenter<MutableList<CardBean>>,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
		OnRefreshListener {

	private lateinit var mAdapter: CardAdapter
	private var mPage = 0
	private var mCurrentPosition = 0

	companion object {
		@JvmStatic
		fun newInstance(id: Int) =
				CardListItemFragment().apply {
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
		getCardListForCategory(categoryId, mPage)
	}

	override fun onDestroyView() {
		super.onDestroyView()
	}

	override fun setUserVisibleHint(isVisibleToUser: Boolean) {
		super.setUserVisibleHint(isVisibleToUser)
		if (isVisibleToUser) {
			EventBus.getDefault().register(this)
		} else {
			EventBus.getDefault().unregister(this)
		}
	}

	private fun initRecyclerView() {
		val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		mAdapter = CardAdapter(arrayListOf())
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_card_list_item)
		mAdapter.onItemClickListener = this
		rv_card_list_item.layoutManager = layoutManager
		rv_card_list_item.adapter = mAdapter
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
		mActivity.startActivity(intent, bundle)
		mCurrentPosition = position
	}

	override fun onRefresh(refreshLayout: RefreshLayout?) {
		mPage = 0
		getCardListForCategory(categoryId, mPage)
	}

	override fun onLoadMoreRequested() {
		getCardListForCategory(categoryId, mPage)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UpdateCardListPositionEvent) {
		if (this::class.java.name == event.sourceName) {
			if (event.currentPosition == mAdapter.data.size - 1) {
				getCardListForCategory(categoryId, mPage)
			}
			mCurrentPosition = event.currentPosition
			rv_card_list_item.scrollToPosition(event.currentPosition)
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UpdateCardListEvent) {
		mAdapter.data[event.currentPosition] = event.cardBean
		mAdapter.notifyItemChanged(event.currentPosition)
	}

	override fun getDataSuccess(list: MutableList<CardBean>) {
		if (mPage == 0) {
			refresh_layout.finishRefresh()
			mAdapter.setNewData(list)
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

	override fun getDataFail(msg: String?) {
		refresh_layout.finishRefresh()
		mAdapter.loadMoreFail()
		toastFail(msg)
	}

	private fun getCardListForCategory(cardCategoryId: Int, page: Int) {
		mDisposable = AppClient.getAPIService().getCardListForCategory(cardCategoryId, page, Constants.PAGE_SIZE)
				.compose(BaseRxJava.handleArrayResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<MutableList<CardBean>>(this) {
					override fun onNext(t: MutableList<CardBean>) {
						getDataSuccess(t)
					}
				})
		addSubscription(mDisposable)
	}

	public fun onActivityReenter() {
		rv_card_list_item.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
			override fun onPreDraw(): Boolean {
				rv_card_list_item.viewTreeObserver.removeOnPreDrawListener(this)
				rv_card_list_item.requestLayout()
				mActivity.startPostponedEnterTransition()
				return true
			}
		})
	}

	fun getSharedView(names: MutableList<String>): MutableMap<String, View> {
		val sharedElements = mutableMapOf<String, View>()
		val cardImg = mAdapter.getViewByPosition(mCurrentPosition, R.id.iv_item_card_icon)
		val cardTitle = mAdapter.getViewByPosition(mCurrentPosition, R.id.tv_item_card_title)
		val cardCategory = mAdapter.getViewByPosition(mCurrentPosition, R.id.tv_item_card_category)
		val cardLabel = mAdapter.getViewByPosition(mCurrentPosition, R.id.tv_item_card_label)
		cardImg?.let { sharedElements[cardImg.transitionName] = it }
		cardTitle?.let { sharedElements[cardTitle.transitionName] = it }
		cardCategory?.let { sharedElements[cardCategory.transitionName] = it }
		cardLabel?.let { sharedElements[cardLabel.transitionName] = it }
		return sharedElements
	}
}
