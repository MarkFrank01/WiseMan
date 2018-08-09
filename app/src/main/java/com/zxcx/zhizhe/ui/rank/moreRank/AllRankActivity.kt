package com.zxcx.zhizhe.ui.rank.moreRank

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.rank.RankAdapter
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.activity_all_rank.*

/**
 * 本周智者榜单页面
 */

class AllRankActivity : MvpActivity<RankPresenter>(), RankContract.View, BaseQuickAdapter.OnItemClickListener,
		BaseQuickAdapter.RequestLoadMoreListener {

	private lateinit var mAdapter: RankAdapter
	private var page: Int = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_all_rank)
		initToolBar("本周智者榜单")
		initRecyclerView()
		onRefresh()
	}

	override fun createPresenter(): RankPresenter {
		return RankPresenter(this)
	}

	private fun onRefresh() {
		page = 0
		mPresenter.getTopHundredRank(page, Constants.PAGE_SIZE)
	}

	override fun onLoadMoreRequested() {
		mPresenter.getTopHundredRank(page, Constants.PAGE_SIZE)
	}

	override fun getDataSuccess(list: List<UserRankBean>) {
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

	override fun toastFail(msg: String?) {
		if (page == 0) {
			loadService.showCallback(NetworkErrorCallback::class.java)
		}
		super.toastFail(msg)
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		val bean = adapter.data[position] as UserRankBean
		val intent = Intent(mActivity, OtherUserActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		startActivity(intent)
	}

	private fun initRecyclerView() {
		mAdapter = RankAdapter(ArrayList())
		mAdapter.onItemClickListener = this
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_rank_user)
		rv_rank_user.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_rank_user.adapter = mAdapter
	}
}
