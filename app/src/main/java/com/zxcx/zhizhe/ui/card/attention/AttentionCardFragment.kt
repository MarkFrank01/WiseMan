package com.zxcx.zhizhe.ui.card.attention

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
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.*
import com.zxcx.zhizhe.loadCallback.AttentionNeedLoginCallback
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback
import com.zxcx.zhizhe.loadCallback.HomeNetworkErrorCallback
import com.zxcx.zhizhe.loadCallback.SelectAttentionCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardAdapter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_attention.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class AttentionCardFragment : RefreshMvpFragment<AttentionCardPresenter>(), AttentionCardContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

	private lateinit var mAdapter: CardAdapter
	private var page = 0
	private var mHidden = true

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val root = inflater.inflate(R.layout.fragment_attention, container, false)
		mRefreshLayout = root.findViewById(R.id.refresh_layout)
		val loadSir = LoadSir.Builder()
				.addCallback(AttentionNeedLoginCallback())
				.addCallback(HomeLoadingCallback())
				.addCallback(HomeNetworkErrorCallback())
				.setDefaultCallback(HomeLoadingCallback::class.java)
				.build()
		loadService = loadSir.register(root) { v ->
			if (checkLogin()) {
				loadService.showCallback(HomeLoadingCallback::class.java)
				onRefresh()
			}
		}
		return loadService.loadLayout
	}

	override fun createPresenter(): AttentionCardPresenter {
		return AttentionCardPresenter(this)
	}

	override fun onHiddenChanged(hidden: Boolean) {
		mHidden = hidden
		super.onHiddenChanged(hidden)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		EventBus.getDefault().register(this)
		initView()
		mHidden = false
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
			rv_attention_card.scrollToPosition(0)
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: LoginEvent) {
		loadService.showCallback(HomeLoadingCallback::class.java)
		onRefresh()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: LogoutEvent) {
		loadService.showCallback(AttentionNeedLoginCallback::class.java)
		mAdapter.data.clear()
		mAdapter.notifyDataSetChanged()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: SelectAttentionEvent) {
		loadService.showCallback(HomeLoadingCallback::class.java)
		onRefresh()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: FollowUserRefreshEvent) {
		loadService.showCallback(HomeLoadingCallback::class.java)
		onRefresh()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: ChangeAttentionEvent) {
		val idList = mutableListOf<Int>()
		event.list.forEach {
			idList.add(it.id)
		}
		mPresenter.changeAttentionList(idList)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UpdateCardListPositionEvent) {
		if (this::class.java.name == event.sourceName) {
			if (event.currentPosition == mAdapter.data.size - 1) {
				getAttentionCard()
			}
			rv_attention_card.scrollToPosition(event.currentPosition)
		}
	}

	override fun onRefresh(refreshLayout: RefreshLayout?) {
		onRefresh()
	}

	private fun onRefresh() {
		page = 0
		getAttentionCard()
	}

	override fun onLoadMoreRequested() {
		getAttentionCard()
	}

	override fun getDataSuccess(list: List<CardBean>) {
		loadService.showSuccess()
		if (list.isEmpty() && page == 0) {
			mPresenter.getClassify()
			return
		}
		if (page == 0) {
			mRefreshLayout.finishRefresh()
			mAdapter.setNewData(list)
			rv_attention_card.scrollToPosition(0)
		} else {
			mAdapter.addData(list)
			val event = AddCardDetailsListEvent(this::class.java.name, list as ArrayList<CardBean>)
			EventBus.getDefault().post(event)
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

	override fun postSuccess() {
		onRefresh()
	}

	override fun postFail(msg: String) {
		toastFail(msg)
	}

	override fun getClassifySuccess(list: List<ClassifyBean>) {
		val selectAttentionCallback = SelectAttentionCallback(list)
		loadService.loadLayout.setupCallback(selectAttentionCallback)
		loadService.showCallback(SelectAttentionCallback::class.java)
	}

	override fun toastFail(msg: String) {
		super.toastFail(msg)
		mAdapter.loadMoreFail()
		if (page == 0) {
			loadService.showCallback(HomeNetworkErrorCallback::class.java)
		}
	}

	override fun startLogin() {
		ZhiZheUtils.logout()
		toastShow(R.string.login_timeout)
		startActivity(Intent(mActivity, LoginActivity::class.java))
		if (loadService != null) {
			loadService.showCallback(AttentionNeedLoginCallback::class.java)
		}
	}

	private fun initView() {
		val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		mAdapter = CardAdapter(ArrayList())
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_attention_card)
		mAdapter.onItemClickListener = this
		rv_attention_card.layoutManager = layoutManager
		rv_attention_card.adapter = mAdapter
		if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
			onRefresh()
		} else {
			loadService.showCallback(AttentionNeedLoginCallback::class.java)
		}
	}

	private fun getAttentionCard() {
		mPresenter.getAttentionCard(page, Constants.PAGE_SIZE)
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
}