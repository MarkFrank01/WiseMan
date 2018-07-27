package com.zxcx.zhizhe.ui.my.creation.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.CommitCardReviewEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.creation.creationDetails.ReviewCardDetailsActivity
import com.zxcx.zhizhe.ui.my.creation.creationDetails.ReviewDetailsActivity
import com.zxcx.zhizhe.ui.my.followUser.FansItemDecoration
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_creation.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CreationInReviewFragment : RefreshMvpFragment<CreationPresenter>(), CreationContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

	private var mPage = 0
	private val mPassType = 0
	private val mPageSize = Constants.PAGE_SIZE
	private lateinit var mAdapter: CreationAdapter

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

	override fun createPresenter(): CreationPresenter {
		return CreationPresenter(this)
	}

	override fun onDestroy() {
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: CommitCardReviewEvent) {
		mPage = 0
		mPresenter.getCreation(mPassType, mPage, mPageSize)
	}

	override fun getDataSuccess(list: List<CardBean>) {
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

	override fun getDataFail(msg: String?) {
		toastError(msg)
	}

	override fun postSuccess() {
		//删除成功
	}

	override fun postFail(msg: String?) {
		toastError(msg)
	}

	override fun onRefresh(refreshLayout: RefreshLayout?) {
		mPage = 0
		mPresenter.getCreation(mPassType, mPage, mPageSize)
	}

	override fun onLoadMoreRequested() {
		mPresenter.getCreation(mPassType, mPage, mPageSize)
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		val bean = adapter.data[position] as CardBean
		if (bean.cardType == 1) {
			mActivity.startActivity(ReviewCardDetailsActivity::class.java) {
				it.putExtra("cardBean", bean)
			}
		} else {
			mActivity.startActivity(ReviewDetailsActivity::class.java) {
				it.putExtra("cardBean", bean)
			}
		}
	}

	private fun initRecyclerView() {
		mAdapter = CreationAdapter(ArrayList())
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