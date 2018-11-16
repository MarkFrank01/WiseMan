package com.zxcx.zhizhe.ui.my.followUser

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_follow_user.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的-粉丝页面
 */

class FansActivity : MvpActivity<FollowUserPresenter>(), FollowUserContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

	private val mFollowType = 1
	private var mPage = 0
	private var mPageSize = Constants.PAGE_SIZE
	private lateinit var mAdapter: FollowUserAdapter
	private lateinit var mDialog: UnFollowConfirmDialog

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_follow_user)
		EventBus.getDefault().register(this)
		initToolBar("粉丝")
		initRecyclerView()
		initLoadSir()
		mPresenter.getFollowUser(mFollowType, mPage, mPageSize)
		mDialog = UnFollowConfirmDialog()
	}

	private fun initLoadSir() {
		loadService = LoadSir.getDefault().register(rv_follow_user, this)
	}

	override fun setListener() {
	}

	override fun onDestroy() {
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	override fun createPresenter(): FollowUserPresenter {
		return FollowUserPresenter(this)
	}

	override fun onReload(v: View?) {
		super.onReload(v)
		mPage = 0
		mPresenter.getFollowUser(mFollowType, mPage, mPageSize)
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: UnFollowConfirmEvent) {
		mPresenter.unFollowUser(event.userId)
	}

	override fun getEmptyFollowUserSuccess(list: MutableList<SearchUserBean>) {}

	override fun getDataSuccess(list: MutableList<SearchUserBean>) {
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

	override fun postSuccess(bean: SearchUserBean) {
		val position = mAdapter.data.indexOf(bean)
		mAdapter.data[position].isFollow = true
		mAdapter.notifyItemChanged(position)
		EventBus.getDefault().post(FollowUserRefreshEvent())
	}

	override fun postFail(msg: String?) {
		toastShow(msg)
	}

	override fun unFollowUserSuccess(bean: SearchUserBean) {
		val position = mAdapter.data.indexOf(bean)
		mAdapter.data[position].isFollow = false
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

	override fun onLoadMoreRequested() {
		mPresenter.getFollowUser(mFollowType, mPage, mPageSize)
	}

	private fun initRecyclerView() {
		mAdapter = FollowUserAdapter(ArrayList())
		mAdapter.onItemChildClickListener = this
		mAdapter.onItemClickListener = this
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_follow_user)
		rv_follow_user.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_follow_user.adapter = mAdapter
		val emptyView = EmptyView.getEmptyViewAndClick(mActivity, "暂无内容", "", R.drawable.no_data, View.OnClickListener {
			if (ZhiZheUtils.isWriter(mActivity)) {
				val intent = Intent(mActivity, CreationEditorActivity::class.java)
				startActivity(intent)
			}
		})
		mAdapter.emptyView = emptyView
	}
}