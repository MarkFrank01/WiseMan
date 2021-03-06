package com.zxcx.zhizhe.ui.circle.classify

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.QuitCircleConfirmEvent
import com.zxcx.zhizhe.loadCallback.LoadingCallback
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.circle.adapter.CircleClassifyAdapter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetaileActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.BottomListPopup.CirclePopup
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_circle_classify.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/1/23
 * @Description : 分类进来的圈子的列表
 */
class CircleClassifyActivity : RefreshMvpActivity<CircleClassifyPresenter>(), CircleClassifyContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {


    //分类的ID
    private var classifyID: Int = 0
    private var classifyTitle: String = ""
    private var page = 0

    private lateinit var mAdapter: CircleClassifyAdapter
    private lateinit var mDialog: QuitCircleConfirmDialog

    private var mSelectPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_classify)
        EventBus.getDefault().register(this)
        initData()
        initView()
        onRefresh()

        initLoadSir()
        mDialog = QuitCircleConfirmDialog()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: QuitCircleConfirmEvent) {
        mPresenter.setjoinCircle(event.circleID, 1)
    }

    override fun onReload(v: View) {
        loadService.showCallback(LoadingCallback::class.java)
        onRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    override fun createPresenter(): CircleClassifyPresenter {
        return CircleClassifyPresenter(this)
    }

    override fun postSuccess(bean: MutableList<CircleBean>) {
    }

    override fun postFail(msg: String?) {
    }

    override fun toastFail(msg: String) {
        if (page == 0) {
            loadService.showCallback(NetworkErrorCallback::class.java)
        }
        super.toastFail(msg)
    }

    override fun JoinCircleSuccess(bean: MutableList<CircleBean>) {
        toastShow("圈子加入成功")
    }

    override fun QuitCircleSuccess(bean: MutableList<CircleBean>) {
        toastShow("圈子退出成功")
    }


    override fun getDataSuccess(list: MutableList<CircleBean>) {
        LogCat.e("圈子分类Activity Data ${list.size}")
        loadService.showSuccess()
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
    }

    override fun setListener() {
        iv_toolbar_right.setOnClickListener {
            test()
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.cb_item_select_join_circle -> {
                val bean = adapter.data[position] as CircleBean
                mActivity.startActivity(CircleDetaileActivity::class.java){
                    it.putExtra("circleID",bean.id)
                }

            }
//            R.id.cb_item_select_join_circle -> {
//                val bean = adapter.data[position] as CircleBean
//                bean.hasJoin = !bean.hasJoin
//                mAdapter.notifyItemChanged(position)
//
//                if (bean.hasJoin) {
//                    //加入圈子
//                    LogCat.e("Join" + bean.id)
////                    mPresenter.setjoinCircle(bean.id, 0)
//                } else {
//                    //取消加入
//                    LogCat.e("Cancel+" + bean.id)
////                    mPresenter.setjoinCircle(bean.id,1)
//                    val bundle = Bundle()
//                    bundle.putInt("circleId", bean.id)
//                    mDialog.arguments = bundle
//                    mDialog.show(mActivity.supportFragmentManager, "")
//                }
//            }
        }
    }

    override fun onLoadMoreRequested() {
        getCircleListByClassifyId()
    }

    private fun initLoadSir() {
        val loadSir = LoadSir.Builder()
                .addCallback(LoadingCallback())
                .addCallback(NetworkErrorCallback())
                .addCallback(LoginTimeoutCallback())
                .setDefaultCallback(LoadingCallback::class.java)
                .build()
        loadService = loadSir.register(mRefreshLayout, this)
    }

    private fun initView() {
        initToolBar(classifyTitle)
        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.c_more_2)

        mAdapter = CircleClassifyAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_circle_classify)
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this

        val view = EmptyView.getEmptyView(mActivity, "暂无圈子", R.drawable.no_circle_data)
        mAdapter.emptyView = view

        rv_circle_classify.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_circle_classify.adapter = mAdapter
    }

    private fun initData() {
        classifyID = intent.getIntExtra("circleClassifyActivityID", 0)
        classifyTitle = intent.getStringExtra("circleClassifyActivityTitle")
    }

    private fun getCircleListByClassifyId() {
        mPresenter.getCircleListByClassifyId(classifyID, mSelectPosition, page, Constants.PAGE_SIZE)
    }

    fun onRefresh() {
        page = 0
        getCircleListByClassifyId()
    }

    fun test() {
//        XPopup.get(mActivity).asBottomList("", arrayOf("默认排序","最多加入","最多话题","最多点赞"),
//                null,mSelectPosition,
//                object : OnSelectListener {
//                    override fun onSelect(position: Int, text: String?) {
//                        mSelectPosition = position
//                    }
//                }).show()

        //目前
        XPopup.get(mActivity)
                .asCustom(CirclePopup(this, "排序选项", arrayOf("默认排序", "最高星级", "最多加入", "最多话题"),
                        null, mSelectPosition,
                        OnSelectListener { position, text ->
                            mSelectPosition = position
                            onRefresh()
                        })
                ).show()

        //将用于提示
//        XPopup.get(mActivity)
//                .asCustom(CircleBottomPopup(this, OnSelectListener { position, text ->
//
//                })
//                ).show()
    }
}