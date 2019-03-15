package com.zxcx.zhizhe.ui.circle.circleowner.ownermanage

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AANotingEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circleowner.owneradd.OwnerAddActivity
import com.zxcx.zhizhe.ui.my.readCards.MyCardItemDecoration
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.getColorForKotlin
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CommentLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import com.zxcx.zhizhe.widget.bottominfopopup.BottomInfoPopup
import kotlinx.android.synthetic.main.activity_owner_manage_content.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description : 管理圈子里的文章
 */
class OwnerManageContentActivity : RefreshMvpActivity<OwnerManageContentPresenter>(), OwnerManageContentContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener,
        SwipeMenuClickListener2 {


    private var mPage = 0
    private var type = 0
    private val mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: OwnerManageAdapter

    private var circleID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_owner_manage_content)
        mRefreshLayout = refresh_layout_content
        initData()
        initRecycleView()
        initToolBar("内容管理")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "添加"
        tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))

        mPresenter.getArticleByCircleId(circleID, type, mPage, mPageSize)
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            startActivity(OwnerAddActivity::class.java){
                it.putExtra("circleId",circleID)

            }
        }

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(evnet: AANotingEvent) {

    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        mPresenter.getArticleByCircleId(circleID, type, mPage, mPageSize)

    }

    override fun createPresenter(): OwnerManageContentPresenter {
        return OwnerManageContentPresenter(this)
    }

    override fun removeArticleSuccess() {
        toastShow("移除成功")
    }

    override fun setArticleFixTopSuccess(hint: String) {
        toastShow(hint)
    }

    override fun getDataSuccess(list: MutableList<CardBean>) {
        mRefreshLayout.finishRefresh()
        if (mPage == 0) {
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }
        mPage++
        if (list.size<Constants.PAGE_SIZE){
            mAdapter.loadMoreEnd(false)
            mAdapter.isUseEmpty(true)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
        mPresenter.getArticleByCircleId(circleID, type, mPage, mPageSize)
    }

    override fun onDeleteClick(position: Int) {
        deleteHint(position)
    }

    override fun onContentClick(position: Int) {
    }

    override fun onTopClick(position: Int) {
        rv_content.scrollToPosition(0)
        mPresenter.setArticleFixTop(circleID,mAdapter.data[position].id,1)

        mAdapter.add(0,mAdapter.data[position])
        mAdapter.remove(position+1)

//        mPage = 0
//        mPresenter.getArticleByCircleId(circleID, type, mPage, mPageSize)
//        toastShow("置顶ing")
    }

    override fun onCancelTopClick(position: Int) {
        mPresenter.setArticleFixTop(circleID,mAdapter.data[position].id,0)
        mPage = 0
        mPresenter.getArticleByCircleId(circleID, type, mPage, mPageSize)
//        toastShow("取消置顶ing")

    }

    //拿ID
    private fun initData() {
        circleID = intent.getIntExtra("circleId", 0)
    }

    private fun initRecycleView() {
        mAdapter = OwnerManageAdapter(ArrayList())
        mAdapter.mListener = this
        mAdapter.setLoadMoreView(CommentLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_content)
        rv_content.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_content.adapter = mAdapter
        rv_content.addItemDecoration(MyCardItemDecoration())
        val emptyView = EmptyView.getEmptyView(mActivity, "暂无内容", R.drawable.no_data)
        mAdapter.emptyView = emptyView
        mAdapter.isUseEmpty(false)

    }

    //移除时的提示
    private fun deleteHint(index:Int){
        XPopup.get(mActivity)
                .asCustom(BottomInfoPopup(this,"移除后将导致圈内可阅读作品减少，是否继续？",-1,
                       OnSelectListener { position, text ->
                            if (position == 2){
                                mPresenter.removeArticle(circleID,index)
                            }
                       })
                ).show()
    }
}