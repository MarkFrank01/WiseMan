package com.zxcx.zhizhe.ui.circle.circlemanlist

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
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.layout_circle_man_list.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleManListActivity : RefreshMvpActivity<CircleManListPresenter>(), CircleManListContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var circleID: Int = 0
    private var page = 0

    private lateinit var mAdapter: CircleManListAdapter
    private lateinit var mDialog: UnFollowConfirmDialog
    private lateinit var mDialog1: UnFollowConfirmDialog

    private lateinit var createBean:CircleUserBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_circle_man_list)
        EventBus.getDefault().register(this)

        initData()
        initView()
        mDialog = UnFollowConfirmDialog()
        mDialog1 = UnFollowConfirmDialog()

        onRefresh()
//        mPresenter.getCircleMemberByCircleId(0)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        onRefresh()
    }

    override fun createPresenter(): CircleManListPresenter {
        return CircleManListPresenter(this)
    }

    override fun getCircleMemberByCircleIdSuccess(list: MutableList<SearchUserBean>) {
        mRefreshLayout.finishRefresh()
        if (page == 0){
            mAdapter.setNewData(list)
        }else{
            mAdapter.addData(list)
        }
        page++
        if (list.size<Constants.PAGE_SIZE){
            mAdapter.loadMoreEnd(false)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }

        tv_man_list_num.text = "成员(${list.size})"
    }

    override fun mFollowUserSuccess(bean: SearchUserBean) {
        val position = mAdapter.data.indexOf(bean)
        mAdapter.data[position].isFollow = bean.isFollow
        mAdapter.notifyItemChanged(position)
        EventBus.getDefault().post(FollowUserRefreshEvent())
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
        val position = mAdapter.data.indexOf(bean)
        mAdapter.data[position].isFollow = false
        mAdapter.notifyItemChanged(position)
        EventBus.getDefault().post(FollowUserRefreshEvent())
    }

    override fun postSuccess(bean: CircleBean?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UnFollowConfirmEvent) {
        mPresenter.unFollowUser(event.userId)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when(view.id){
            R.id.cb_item_search_user_follow ->{
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
        }
    }

    override fun onLoadMoreRequested() {
    }

    private fun initData() {
        circleID = intent.getIntExtra("circleID", 0)
        LogCat.e("circleID $circleID")
        createBean = intent.getParcelableExtra("create")
        LogCat.e("SBBB ${createBean.avatar}")
    }

    private fun initView() {
        initToolBar("圈子成员列表")
        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.iv_toolbar_more)

        val cb1:CheckBox = cb_item_search_user_follow_2

        ImageLoader.load(this,createBean.avatar,R.drawable.default_card,iv_item_search_user)
        tv_item_search_user_name.text = createBean.name
        tv_item_search_user_level.text = this.getString(R.string.tv_level, createBean.level)
        tv_item_search_user_card.text = createBean.cardNum.toString()
        tv_item_search_user_fans.text =  createBean.fansNum.toString()
        tv_item_search_user_like.text = createBean.likeNum.toString()
        tv_item_search_user_collect.text = createBean.collectNum.toString()
        cb_item_search_user_follow_2.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
        cb_item_search_user_follow_2.setOnClickListener {
//            cb1.isChecked = !cb1.isChecked
//            if (cb1.isChecked){
//                val bundle = Bundle()
//                bundle.putInt("userId", createBean.id)
//                mDialog1.arguments = bundle
//                mDialog1.show(mActivity.supportFragmentManager, "")
//            }else{
//                mPresenter.followUser(createBean.id)
//            }
        }

        mAdapter = CircleManListAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this

        rv_circle_man.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_circle_man.adapter = mAdapter
    }

    fun onRefresh() {
        page = 0
        getCircleMember()
    }

    private fun getCircleMember() {
        mPresenter.getCircleMemberByCircleId(0, circleID, page, Constants.PAGE_SIZE)
    }
}