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
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.activity_follow_user.*
import kotlinx.android.synthetic.main.layout_no_data.view.*
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

    override fun getEmptyFollowUserSuccess(list: MutableList<FollowUserBean>) {
        // todo 修改占位图
        val emptyView = LayoutInflater.from(mActivity).inflate(R.layout.layout_no_data_and_user, null)
        emptyView.tv_no_data_1.text = "暂无内容"
        emptyView.iv_no_data.setImageResource(R.drawable.no_banner)
        mAdapter.addHeaderView(emptyView)
        mAdapter.setNewData(list)
        mAdapter.loadMoreEnd(false)
    }

    override fun getDataSuccess(list: MutableList<FollowUserBean>) {
        if (list.isEmpty()){
            mPresenter.getEmptyFollowUser()
            return
        }
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
        val header = LayoutInflater.from(mActivity).inflate(R.layout.layout_header_title, null)
        header.findViewById<TextView>(R.id.tv_header_title).text = "关注"
        mAdapter.addHeaderView(header)
    }
}