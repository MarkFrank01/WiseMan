package com.zxcx.zhizhe.ui.my.followUser

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.ZhiZheUtils
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_follow_user.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FansActivity : RefreshMvpActivity<FollowUserPresenter>(), FollowUserContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener{

    private val mFollowType = 1
    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: FollowUserAdapter
    private lateinit var mDialog: UnFollowConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_user)
        EventBus.getDefault().register(this)
        initRecyclerView()
        mPresenter.getFollowUser(mFollowType,mPage,mPageSize)
        mDialog = UnFollowConfirmDialog()
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
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

    override fun getEmptyFollowUserSuccess(list: MutableList<FollowUserBean>) {}

    override fun getDataSuccess(list: MutableList<FollowUserBean>) {
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

    override fun postSuccess(bean: FollowUserBean) {
        val position = mAdapter.data.indexOf(bean)
        mAdapter.data[position].followType = bean.followType
        mAdapter.notifyItemChanged(position)
        EventBus.getDefault().post(FollowUserRefreshEvent())
    }

    override fun postFail(msg: String?) {
        toastShow(msg)
    }

    override fun unFollowUserSuccess(bean: FollowUserBean) {
        bean.id = bean.targetUserId
        val position = mAdapter.data.indexOf(bean)
        mAdapter.data[position].followType = bean.followType
        mAdapter.notifyItemChanged(position)
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
        if (cb.isChecked) {
            val bundle = Bundle()
            bundle.putInt("userId", bean.id ?: 0)
            mDialog.arguments = bundle
            mDialog.show(mActivity.fragmentManager, "")
        }else{
            mPresenter.followUser(bean.id ?: 0)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        mPresenter.getFollowUser(mFollowType,mPage,mPageSize)
    }

    override fun onLoadMoreRequested() {
        mPresenter.getFollowUser(mFollowType,mPage,mPageSize)
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
        val emptyView = EmptyView.getEmptyViewAndClick(mActivity,"你还没有“小粉丝”","点击创作 让更多的人知道你",R.color.button_blue,View.OnClickListener {
            if (ZhiZheUtils.isWriter(mActivity)) {
                val intent = Intent(mActivity, NewCreationTitleActivity::class.java)
                startActivity(intent)
            }
        })
        mAdapter.emptyView = emptyView
        val header = LayoutInflater.from(mActivity).inflate(R.layout.layout_header_title, null)
        header.findViewById<TextView>(R.id.tv_header_title).text = "关注"
        mAdapter.addHeaderView(header)
    }
}