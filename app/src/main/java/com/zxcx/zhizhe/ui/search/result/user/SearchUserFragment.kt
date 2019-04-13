package com.zxcx.zhizhe.ui.search.result.user

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_search_result.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 搜索用户结果页面
 */

class SearchUserFragment : MvpFragment<SearchUserPresenter>(), SearchUserContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

	private var mPage = 0
	private lateinit var mAdapter: SearchUserAdapter
	private lateinit var mDialog: UnFollowConfirmDialog

	var mKeyword = ""
		set(value) {
			field = value
			mPage = 0
			mPresenter?.searchUser(mKeyword, mPage)
		}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_search_result, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		EventBus.getDefault().register(this)
		initRecyclerView()
		mPresenter?.searchUser(mKeyword, mPage)
		mDialog = UnFollowConfirmDialog()
	}

	override fun onDestroyView() {
		EventBus.getDefault().unregister(this)
		super.onDestroyView()
	}

	override fun createPresenter(): SearchUserPresenter {
		return SearchUserPresenter(this)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UnFollowConfirmEvent) {
		mPresenter.unFollowUser(event.userId)
	}

	override fun getDataSuccess(list: List<SearchUserBean>) {
        mAdapter.mKeyword = mKeyword
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

	override fun postSuccess(bean: SearchUserBean) {
		val position = mAdapter.data.indexOf(bean)
//		mAdapter.data[position].isFollow = bean.isFollow
        mAdapter.data[position].followType = 1
		mAdapter.notifyItemChanged(position)
		EventBus.getDefault().post(FollowUserRefreshEvent())
	}

	override fun postFail(msg: String?) {
		toastShow(msg)
	}

	override fun unFollowUserSuccess(bean: SearchUserBean) {
		val position = mAdapter.data.indexOf(bean)
//		mAdapter.data[position].isFollow = false
        mAdapter.data[position].followType = 0
		mAdapter.notifyItemChanged(position)
		EventBus.getDefault().post(FollowUserRefreshEvent())
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
		val bean = adapter.data[position] as SearchUserBean
		val intent = Intent(mActivity, OtherUserActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		startActivity(intent)
	}

	override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
		val cb = view as CheckBox
		cb.isChecked = !cb.isChecked
		if (checkLogin()) {
			val bean = adapter.data[position] as SearchUserBean
			if (cb.isChecked) {
				val bundle = Bundle()
				bundle.putInt("userId", bean.id)
				mDialog.arguments = bundle
				mDialog.show(mActivity.supportFragmentManager, "")
			} else {
				mPresenter.followUser(bean.id)
			}
		}
	}

	override fun onLoadMoreRequested() {
		mPresenter?.searchUser(mKeyword, mPage)
	}

	private fun initRecyclerView() {
		mAdapter = SearchUserAdapter(ArrayList())
		mAdapter.onItemChildClickListener = this
		mAdapter.onItemClickListener = this
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_search_result)
		rv_search_result.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_search_result.adapter = mAdapter
		val emptyView = EmptyView.getEmptyView(mActivity, "暂无内容，换个关键词试试", R.drawable.iv_need_login)
		mAdapter.emptyView = emptyView
	}
}