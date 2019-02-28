package com.zxcx.zhizhe.ui.circle.circledetaile

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.barlibrary.ImmersionBar
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.circle.adapter.CircleDetaileAdapter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean
import com.zxcx.zhizhe.ui.circle.circlemanlist.CircleManListActivity
import com.zxcx.zhizhe.ui.circle.circlequestion.CircleQuestionActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.layout_circle_detail.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description : 圈子详情页
 */
class CircleDetaileActivity : RefreshMvpActivity<CircleDetailePresenter>(), CircleDetaileContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var circleID: Int = 0

   private lateinit var  creater: CircleUserBean

    private lateinit var mAdapter: CircleDetaileAdapter

    //话题的数据
    private var mHuaTiPage = 0
    private var mHuaTiPageSize = Constants.PAGE_SIZE
    private var mHuaTiOrder = 0

    //存放自己是否加入圈子
    private var hasJoinBoolean: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_circle_detail)
        initData()
        initRecycleView()
        initView()
        initToolBar()

        mRefreshLayout = refresh_layout

        mPresenter.getCircleBasicInfo(circleID)
//        mPresenter.getCircleQ|AByCircleId(mHuaTiOrder,circleID,mHuaTiPage,mHuaTiPageSize)
        onRefresh()
    }

    override fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar.keyboardEnable(true)
        mImmersionBar.init()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        onRefresh()
    }

    override fun createPresenter(): CircleDetailePresenter {
        return CircleDetailePresenter(this)
    }

    override fun getCircleBasicInfoSuccess(bean: CircleBean) {
//        detail_name.text  = bean.title
//        t1.text = ""+bean.likeCount
//        t2.text = ""+bean.qaCount
//        t3.text = ""+bean.joinUserCount
        ImageLoader.load(this, bean.titleImage, R.drawable.default_card, detail_src_img)
        ImageLoader.load(this, bean.creater?.avatar, R.drawable.default_card, detail_my_img)

        creater = bean.creater!!

        loadJoinUserImg(bean)

//        hasJoinBoolean = bean.hasJoin
//
//        LogCat.e("member ${bean.memberList.size}")
//        val memberList:MutableList<CircleUserBean> = bean.memberList

    }

    override fun getCircleMemberByCircleIdSuccess(bean: MutableList<CircleBean>) {
    }

    override fun getCircleQAByCircleIdSuccess(list: MutableList<CircleDetailBean>) {
        LogCat.e("getCircleQAByCircleIdSuccess~ "+list.size)

        val emptyView = EmptyView.getEmptyView(mActivity,"暂无内容",R.drawable.no_data)
        mAdapter.emptyView = emptyView

        mRefreshLayout.finishRefresh()
        if (mHuaTiPage == 0){
            LogCat.e("First")
            mAdapter.setNewData(list)
            mHuaTiPage++
            onRefresh()
        }else{
            LogCat.e("More")
            mAdapter.addData(list)
        }

        mAdapter.notifyDataSetChanged()

        mHuaTiPage++

        if (list.isEmpty()){
            mAdapter.loadMoreEnd(false)
        }else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun setListener() {
        //去圈子成员列表
        detail_to_man_list.setOnClickListener {
//            toastShow("to man list")
            mActivity.startActivity(CircleManListActivity::class.java){
                it.putExtra("circleID", circleID)
                it.putExtra("create",creater)
            }
        }

        //去发布提问
        ll_comment_input.setOnClickListener {
            mActivity.startActivity(CircleQuestionActivity::class.java){}
        }

        et_comment.setOnClickListener {
            mActivity.startActivity(CircleQuestionActivity::class.java) {}
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
        onRefresh()
    }

    private fun initData() {
        circleID = intent.getIntExtra("circleID", 0)
        LogCat.e("circleID $circleID")
    }

    private fun initRecycleView() {
        mAdapter = CircleDetaileAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_circle_detail)
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this

//        rv_circle_detail.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)

        rv_circle_detail.layoutManager = object :GridLayoutManager(this,1){
            override fun canScrollVertically() = false
        }
        rv_circle_detail.isNestedScrollingEnabled =false
        rv_circle_detail.setHasFixedSize(true)
        rv_circle_detail.isFocusable  = false

        rv_circle_detail.adapter = mAdapter
    }

    private fun initView(){

    }

    private fun loadJoinUserImg(bean: CircleBean){
        if (bean.memberList.size > 0) {
            if (bean.memberList[0].avatar.isNotEmpty() && bean.memberList[0].avatar != "") {
                detail_round_img1.visibility = View.VISIBLE
                ImageLoader.load(mActivity,bean.memberList[0].avatar,R.drawable.default_card,detail_round_img1)
            }
        }

        if (bean.memberList.size > 1) {
            if (bean.memberList[1].avatar.isNotEmpty() && bean.memberList[1].avatar != "") {
                detail_round_img2.visibility = View.VISIBLE
                ImageLoader.load(mActivity,bean.memberList[1].avatar,R.drawable.default_card,detail_round_img2)
            }
        }

        if (bean.memberList.size > 2) {
            if (bean.memberList[2].avatar.isNotEmpty() && bean.memberList[2].avatar != "") {
                detail_round_img3.visibility = View.VISIBLE
                ImageLoader.load(mActivity,bean.memberList[2].avatar,R.drawable.default_card,detail_round_img3)
            }
        }

        if (bean.memberList.size > 3) {
            if (bean.memberList[3].avatar.isNotEmpty() && bean.memberList[3].avatar != "") {
                detail_round_img4.visibility = View.VISIBLE
                ImageLoader.load(mActivity,bean.memberList[3].avatar,R.drawable.default_card,detail_round_img4)
            }
        }

        if (bean.memberList.size > 4) {
            if (bean.memberList[4].avatar.isNotEmpty() && bean.memberList[4].avatar != "") {
                detail_round_img5.visibility = View.VISIBLE
                ImageLoader.load(mActivity,bean.memberList[4].avatar,R.drawable.default_card,detail_round_img5)
            }
        }

        if (bean.memberList.size > 5) {
            if (bean.memberList[5].avatar.isNotEmpty() && bean.memberList[5].avatar != "") {
                detail_round_img6.visibility = View.VISIBLE
                ImageLoader.load(mActivity,bean.memberList[5].avatar,R.drawable.default_card,detail_round_img6)
            }
        }
    }

    fun onRefresh(){
       getCircleQAByCircleIdRefresh()
    }

    private fun getCircleQAByCircleIdRefresh(){
        mPresenter.getCircleQAByCircleId(mHuaTiOrder,circleID,mHuaTiPage,mHuaTiPageSize)
    }
}
