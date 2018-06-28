package com.zxcx.zhizhe.ui.my.creation.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.event.SaveDraftSuccessEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.my.creation.creationDetails.DraftDetailsActivity
import com.zxcx.zhizhe.ui.my.followUser.FansItemDecoration
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_creation.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CreationDraftsFragment : RefreshMvpFragment<CreationPresenter>(), CreationContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

	private var mPage = 0
	private val mPassType = 3
	private val mPageSize = Constants.PAGE_SIZE
	private lateinit var mAdapter: ReviewCreationAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_creation, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		mRefreshLayout = refresh_layout
		super.onViewCreated(view, savedInstanceState)
		EventBus.getDefault().register(this)
		initRecyclerView()
		mPresenter.getCreation(mPassType, mPage, mPageSize)
	}

	override fun onDestroy() {
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	override fun createPresenter(): CreationPresenter {
		return CreationPresenter(this)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: SaveDraftSuccessEvent) {
		mPage = 0
		mPresenter.getCreation(mPassType, mPage, mPageSize)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: CommitCardReviewEvent) {
		mPage = 0
		mPresenter.getCreation(mPassType, mPage, mPageSize)
	}

	override fun getDataSuccess(list: List<CreationBean>) {
		mRefreshLayout.finishRefresh()
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

	override fun onRefresh(refreshLayout: RefreshLayout?) {
		mPage = 0
		mPresenter.getCreation(mPassType, mPage, mPageSize)
	}

	override fun onLoadMoreRequested() {
		mPresenter.getCreation(mPassType, mPage, mPageSize)
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		val bean = adapter.data[position] as CreationBean
		val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
				Pair.create(view.findViewById(R.id.iv_item_card_icon), "cardImage"),
				Pair.create(view.findViewById(R.id.tv_item_card_title), "cardTitle"),
				Pair.create(view.findViewById(R.id.tv_item_card_category), "cardBag")).toBundle()
		val intent = Intent(mActivity, DraftDetailsActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		intent.putExtra("imageUrl", bean.imageUrl)
		intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
		intent.putExtra("authorName", bean.author)
		mActivity.startActivity(intent, bundle)
	}

	private fun initRecyclerView() {
		mAdapter = ReviewCreationAdapter(ArrayList())
		mAdapter.onItemClickListener = this
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_creation)
		rv_creation.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_creation.adapter = mAdapter
		rv_creation.addItemDecoration(FansItemDecoration())
		val emptyView = EmptyView.getEmptyView(mActivity, "暂无内容", R.drawable.no_data)
		mAdapter.emptyView = emptyView
	}
}