package com.zxcx.zhizhe.ui.my.followUser

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.search.result.card.FollowUserAdapter
import com.zxcx.zhizhe.ui.search.result.card.FollowUserBean
import com.zxcx.zhizhe.ui.search.result.card.FollowUserContract
import com.zxcx.zhizhe.ui.search.result.card.FollowUserPresenter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_follow_user.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FollowUserActivity : RefreshMvpActivity<FollowUserPresenter>(), FollowUserContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener{

    private val mFollowType = 0
    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: FollowUserAdapter
    private lateinit var mDialog: UnFollowConfirmDialog

    var mSortType = 0//0倒序 1正序
        set(value) {
            field = value
            mPage = 0
            mPresenter?.getFollowUser(mFollowType,mSortType,mPage,mPageSize)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_user)
        initToolBar("我关注的")
        EventBus.getDefault().register(this)
        initRecyclerView()
        mPresenter.getFollowUser(mFollowType,mSortType,mPage,mPageSize)
        mDialog = UnFollowConfirmDialog()
        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.iv_order_sequence)
    }

    override fun setListener() {
        iv_toolbar_right.setOnClickListener {
            if (mSortType == 1) {
                mSortType = 0
                iv_toolbar_right.setImageResource(R.drawable.iv_order_sequence)
            } else if (mSortType == 0) {
                mSortType = 1
                iv_toolbar_right.setImageResource(R.drawable.iv_order_inverted)
            }
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun createPresenter(): FollowUserPresenter {
        return FollowUserPresenter(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UnFollowConfirmEvent) {
        mPresenter.unFollowUser(event.userId)
    }

    override fun getDataSuccess(list: List<FollowUserBean>) {
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

    override fun postSuccess(bean: FollowUserBean?) {
        //关注成功回调，我关注的界面不会触发
    }

    override fun postFail(msg: String?) {
        toastShow(msg)
    }

    override fun unFollowUserSuccess(bean: FollowUserBean) {
        bean.id = bean.targetUserId
        mAdapter.remove(mAdapter.data.indexOf(bean))
        EventBus.getDefault().post(FollowUserRefreshEvent())
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        val bean = adapter.data[position] as FollowUserBean
        val intent = Intent(mActivity, OtherUserActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        val cb = view as CheckBox
        cb.isChecked = !cb.isChecked
        val bean = adapter.data[position] as FollowUserBean
        val bundle = Bundle()
        bundle.putInt("userId",bean.id?:0)
        mDialog.arguments = bundle
        mDialog.show(mActivity.fragmentManager,"")
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        mPresenter.getFollowUser(mFollowType,mSortType,mPage,mPageSize)
    }

    override fun onLoadMoreRequested() {
        mPresenter.getFollowUser(mFollowType,mSortType,mPage,mPageSize)
    }

    private fun initRecyclerView() {
        mAdapter = FollowUserAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_follow_user)
        rv_follow_user.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_follow_user.adapter = mAdapter
        rv_follow_user.addItemDecoration(FansItemDecoration())
        //todo 修改占位图
        val emptyView = EmptyView.getEmptyView(mActivity,"你还没有喜欢的作者",R.drawable.no_banner)
        mAdapter.emptyView = emptyView
    }
}