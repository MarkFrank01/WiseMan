package com.zxcx.zhizhe.ui.circle.circlemanlist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean
import com.zxcx.zhizhe.ui.circle.circlemanlist.detail.CircleManDetailActivity
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.BottomListPopup.CirclePopup
import com.zxcx.zhizhe.widget.CustomLoadMoreView
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

    private lateinit var createBean: CircleUserBean

    private var orderType = 0

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

    override fun setListener() {
        iv_toolbar_right.setOnClickListener {
            orderman()
        }
    }

    override fun getCircleMemberByCircleIdSuccess(list: MutableList<SearchUserBean>) {
        mRefreshLayout.finishRefresh()
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

        tv_man_list_num.text = "成员(${list.size})"
    }

    override fun mFollowUserSuccess(bean: SearchUserBean) {
        val position = mAdapter.data.indexOf(bean)
        if (position != -1) {
            mAdapter.data[position].followType = 1
            mAdapter.notifyItemChanged(position + mAdapter.headerLayoutCount)
            EventBus.getDefault().post(FollowUserRefreshEvent())
            toastShow("关注成功")
        } else {
            bean.followType = 1
            cb_item_search_user_follow_2.text = "已关注"
            cb_item_search_user_follow_2.setTextColor(getColorForKotlin(R.color.text_color_3))
        }
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
        val position = mAdapter.data.indexOf(bean)

        if (position != -1) {
            mAdapter.data[position].followType = 0
            mAdapter.notifyItemChanged(position + mAdapter.headerLayoutCount)
            EventBus.getDefault().post(FollowUserRefreshEvent())
            toastShow("取消关注成功")
        } else {
            bean.followType = 0
            cb_item_search_user_follow_2.text = "关注"
            cb_item_search_user_follow_2.setTextColor(getColorForKotlin(R.color.button_blue))

        }
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
//        mPresenter.unFollowUser(event.userId)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.cb_item_search_user_follow -> {
                val cb = view as CheckBox
                cb.isChecked = !cb.isChecked
                if (checkLogin()) {
                    val bean = adapter.data[position] as SearchUserBean
                    if (cb.isChecked) {
//                        val bundle = Bundle()
//                        bundle.putInt("userId", bean.id)
//                        mDialog.arguments = bundle
//                        mDialog.show(mActivity.supportFragmentManager, "")
                        mPresenter.unFollowUser(bean.id)
                    } else {
                        mPresenter.followUser(bean.id)
                    }
                }
            }
            R.id.iv_item_search_user -> {
                //去个人的信息
                val bean = adapter.data[position] as SearchUserBean
                mActivity.startActivity(CircleManDetailActivity::class.java){
                    it.putExtra("userId",bean.id)
                }
            }
        }
    }

    override fun onLoadMoreRequested() {
        getCircleMember()
    }

    private fun initData() {
        circleID = intent.getIntExtra("circleID", 0)
        createBean = intent.getParcelableExtra("create")
    }

    private fun initView() {
        initToolBar("圈子成员列表")
        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.iv_toolbar_more)


        ImageLoader.load(this, createBean.avatar, R.drawable.default_card, iv_item_search_user)
        tv_item_search_user_name.text = createBean.name
        tv_item_search_user_level.text = this.getString(R.string.tv_level, createBean.level)
        tv_item_search_user_card.text = createBean.cardNum.toString()
        tv_item_search_user_fans.text = createBean.fansNum.toString()
        tv_item_search_user_like.text = createBean.likeNum.toString()
        tv_item_search_user_collect.text = createBean.collectNum.toString()
        cb_item_search_user_follow_2.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
        cb_item_search_user_follow_2.setOnClickListener {

            if (createBean.followType == 0) {
                mPresenter.followUser(createBean.id)
            } else if (createBean.followType == 1) {
                mPresenter.unFollowUser(createBean.id)
            }

        }

        when (createBean.followType) {
            0 -> {
                cb_item_search_user_follow_2.text = "关注"
                cb_item_search_user_follow_2.setTextColor(getColorForKotlin(R.color.button_blue))
            }

            1 -> {
                cb_item_search_user_follow_2.text = "已关注"
                cb_item_search_user_follow_2.setTextColor(getColorForKotlin(R.color.text_color_3))
            }

            2 -> {
                cb_item_search_user_follow_2.text = "相互关注"
                cb_item_search_user_follow_2.setTextColor(getColorForKotlin(R.color.text_color_3))
            }

        }


        mAdapter = CircleManListAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this

        rv_circle_man.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_circle_man.layoutManager = object :LinearLayoutManager(mActivity){
            override fun canScrollVertically() = false
        }

        rv_circle_man.adapter = mAdapter

        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_circle_man)
    }

    fun onRefresh() {
        page = 0
        getCircleMember()
    }

    private fun getCircleMember() {
        mPresenter.getCircleMemberByCircleId(orderType, circleID, page, Constants.PAGE_SIZE)
    }

    //排序人群
    private fun orderman() {
        XPopup.Builder(mActivity)
                .asCustom(CirclePopup(this, "排序类型", arrayOf("默认排序", "最多创作", "最多关注", "最多点赞"),
                        null, orderType,
                        OnSelectListener { position, text ->
                            orderType = position
                            page = 0
                            onRefresh()
                        })
                ).show()
    }
}