package com.zxcx.zhizhe.ui.search.result.card

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import com.zxcx.zhizhe.widget.UnFollowConfirmDialog
import kotlinx.android.synthetic.main.fragment_follow_user.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FansFragment : MvpFragment<FollowUserPresenter>(), FollowUserContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener{

    private val mFollowType = 1
    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: FollowUserAdapter
    private lateinit var mDialog: UnFollowConfirmDialog

    var mSortType = 1//0倒序 1正序
        set(value) {
            field = value
            mPage = 0
            mPresenter.getFollowUser(mFollowType,mSortType,mPage,mPageSize)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follow_user, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initRecyclerView()
        mPresenter.getFollowUser(mFollowType,mSortType,mPage,mPageSize)
        mDialog = UnFollowConfirmDialog()
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
        val position = mAdapter.getParentPosition(bean)
        mAdapter.data[position].followType = bean.followType
        mAdapter.notifyItemChanged(position)
    }

    override fun postFail(msg: String?) {
        toastShow(msg)
    }

    override fun unFollowUserSuccess(bean: FollowUserBean) {
        val position = mAdapter.getParentPosition(bean)
        mAdapter.data[position].followType = bean.followType
        mAdapter.notifyItemChanged(position)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        val bean = adapter.data[position] as FollowUserBean
        val bundle = Bundle()
        bundle.putInt("userId",bean.id?:0)
        mDialog.arguments = bundle
        mDialog.show(mActivity.fragmentManager,"")
    }

    override fun onLoadMoreRequested() {
        mPresenter.getFollowUser(mFollowType,mSortType,mPage,mPageSize)
    }

    private fun initRecyclerView() {
        mAdapter = FollowUserAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_follow_user)
        rv_follow_user.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_follow_user.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity,"暂时没有更多信息","点击创作来吸引你的粉丝",R.color.button_blue,View.OnClickListener {
            val intent = Intent(mActivity,NewCreationTitleActivity::class.java)
            startActivity(intent)
        })
        mAdapter.emptyView = emptyView
    }
}