package com.zxcx.zhizhe.ui.circle.circledetaile

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.barlibrary.ImmersionBar
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean
import com.zxcx.zhizhe.ui.circle.circlemanlist.CircleManListActivity
import com.zxcx.zhizhe.ui.circle.circlequestion.CircleQuestionActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.layout_circle_detail.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleDetaileActivity : RefreshMvpActivity<CircleDetailePresenter>(), CircleDetaileContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var circleID: Int = 0

   private lateinit var  creater: CircleUserBean

    //话题的数据
    private var mHuaTiPage = 0
    private var mHuaTiPageSize = Constants.PAGE_SIZE

    //存放自己是否加入圈子
    private var hasJoinBoolean: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_circle_detail)
        initData()
        initRecycleView()
        initView()

        mPresenter.getCircleBasicInfo(circleID)
        mPresenter.getCircleQAByCircleId(0,circleID,mHuaTiPage,mHuaTiPageSize)
    }

    override fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar.keyboardEnable(true)
        mImmersionBar.init()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
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

    override fun getCircleQAByCircleIdSuccess(bean: MutableList<CircleBean>) {
        LogCat.e("getCircleQAByCircleIdSuccess"+bean.size)
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
    }

    private fun initData() {
        circleID = intent.getIntExtra("circleID", 0)
        LogCat.e("circleID $circleID")
    }

    private fun initRecycleView() {

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
}
