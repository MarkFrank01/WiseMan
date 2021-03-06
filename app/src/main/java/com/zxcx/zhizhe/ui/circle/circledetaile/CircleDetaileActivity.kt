package com.zxcx.zhizhe.ui.circle.circledetaile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gyf.barlibrary.ImmersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.pay.SelectPayActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardDetails.SingleCardDetailsActivity
import com.zxcx.zhizhe.ui.circle.adapter.CircleDetaileAdapter
import com.zxcx.zhizhe.ui.circle.circledetaile.recommend.CircleRecommendActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean
import com.zxcx.zhizhe.ui.circle.circlemanlist.CircleManListActivity
import com.zxcx.zhizhe.ui.circle.circlemore.CircleEditActivity
import com.zxcx.zhizhe.ui.circle.circlemore.CircleIntroductionActivity
import com.zxcx.zhizhe.ui.circle.circlemore.CirclePingFenActivity
import com.zxcx.zhizhe.ui.circle.circleowner.ownermanage.OwnerManageContentActivity
import com.zxcx.zhizhe.ui.circle.circlequestion.CircleQuestionActivity
import com.zxcx.zhizhe.ui.circle.circlequestiondetail.CircleQuestionDetailActivity
import com.zxcx.zhizhe.ui.circle.circlesearch.inside.CircleInsidePreActivity
import com.zxcx.zhizhe.ui.my.money.MoneyBean
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.BottomListPopup.CirclePopup
import com.zxcx.zhizhe.widget.BottomListPopup.HuatiManagePopup
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.DefaultRefreshHeader
import com.zxcx.zhizhe.widget.bottomdescpopup.CircleBottomPopup2
import com.zxcx.zhizhe.widget.bottomdescpopup.CircleJoinPopup
import com.zxcx.zhizhe.widget.bottominfopopup.BottomInfoPopup
import com.zxcx.zhizhe.widget.bottomsharepopup.CircleBottomSharePopup
import com.zxcx.zhizhe.widget.gridview.GridItemClickListener
import com.zxcx.zhizhe.widget.gridview_tj.ContentBean
import kotlinx.android.synthetic.main.layout_circle_detail.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description : 圈子详情页
 */
class CircleDetaileActivity : RefreshMvpActivity<CircleDetailePresenter>(), CircleDetaileContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {


    private var circleID: Int = 0

    private lateinit var creater: CircleUserBean

    private lateinit var mAdapter: CircleDetaileAdapter

    //话题的数据
    private var mHuaTiPage = 0
    private var mHuaTiPageSize = 5
    private var mHuaTiOrder = 0

    //存放自己是否加入圈子
    private var hasJoinBoolean: Boolean = false

    //存放自己是否是圈主
    private var mCircleImOwner: Boolean = false

    //存放简介
    private var mIntroduction: String = ""

    //存放图片地址
    private var mImageUrl: String = ""

    //存放分类的ID和名字
    private var classifyId = 0

    private var labelName = ""

    //存放限免的类型
    private var limitedTimeType = 0

    //排序话题时的标注
    private var mSelectPosition = 0


    //推荐文章的数据
    private var mClassifyData: MutableList<ContentBean> = mutableListOf()

    //存放一些支付信息所必须的数据
    var circlename: String = ""
    var circleprice: String = ""
    var circleendtime: Long = 0
    var circleyue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_circle_detail)
        mRefreshLayout = findViewById(R.id.refresh_layout)
        initData()
        initRecycleView()
        initView()

        mRefreshLayout = refresh_layout

        mPresenter.getCircleBasicInfo(circleID)
        mPresenter.getAccountDetails()
//        mPresenter.getCircleQ|AByCircleId(mHuaTiOrder,circleID,mHuaTiPage,mHuaTiPageSize)
//        onRefresh()
    }

    override fun onResume() {
        super.onResume()
        mHuaTiPage = 0
        onRefresh()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 1 && data != null) {
            onRefresh()
        }
    }

    override fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
        if (!Constants.IS_NIGHT) {
            mImmersionBar
                    .statusBarColor(R.color.translate)
                    .statusBarDarkFont(true, 0.2f)
                    .flymeOSStatusBarFontColor(R.color.text_color_1)
                    .keyboardEnable(true)
                    .fitsSystemWindows(false)
        } else {
            mImmersionBar
                    .statusBarColor(R.color.translate)
                    .flymeOSStatusBarFontColor(R.color.text_color_1)
                    .keyboardEnable(true)
                    .fitsSystemWindows(false)
        }
        mImmersionBar.init()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mHuaTiPage = 0
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

        //数据加载后才可点击
        iv_toolbar_right2.isEnabled = true
        iv_toolbar_right1.isEnabled = true

        ImageLoader.load(this, bean.titleImage, R.drawable.default_card, detail_src_img)
        ImageLoader.load(this, bean.creater?.avatar, R.drawable.default_card, detail_my_img)

        tv_show.text = bean.sign

        creater = bean.creater!!

        loadJoinUserImg(bean)
        loadTuiJianArticle(bean)

        toolbar_title_1.text = bean.title

        hasJoinBoolean = bean.hasJoin

        if (!bean.hasJoin) {
            bottom_bt.text = "立即加入 ￥" + bean.price
            bottom_bt.visibility = View.VISIBLE
            ll_join.visibility = View.VISIBLE
            ll_comment_input.isEnabled = false
            et_comment.isEnabled = false

//            iv_toolbar_right1.visibility = View.GONE
        } else {
            bottom_bt.visibility = View.GONE
            ll_join.visibility = View.GONE
            ll_comment_input.isEnabled = true
            et_comment.isEnabled = true

//            iv_toolbar_right1.visibility = View.VISIBLE
        }

        circlename = bean.title
        circleprice = bean.price
        circleendtime = bean.memberExpirationTime.toLong()
        mImageUrl = bean.titleImage
        labelName = bean.classifytitle
        classifyId = bean.classifyId
        limitedTimeType = bean.limitedTimeType

        //存储数据
        mIntroduction = bean.sign

        LogCat.e("检查是否是圈主" + bean.owner)
        //存放是否是圈主的数据
        mCircleImOwner = bean.owner

//        hasJoinBoolean = bean.hasJoin
//
//        LogCat.e("member ${bean.memberList.size}")
//        val memberList:MutableList<CircleUserBean> = bean.memberList

        //评分
        numScore(bean.overallRating)

        //最近一次的时间
        detail_time.text = "圈主活跃: " + bean.circleActiveDistanceTime

        detail_man_num.text = bean.joinUserCount.toString()

        detail_read_num.text = bean.articleCount.toString()

        detail_comment_num.text = bean.qaCount.toString()

    }

    override fun getCircleMemberByCircleIdSuccess(bean: MutableList<CircleBean>) {
    }

    override fun reportCircleSuccess() {
        toastShow("举报成功")
    }

    override fun setQAFixTopSuccess() {
        toastShow("设置成功")

    }

    override fun deleteQaSuccess() {
        toastShow("删除成功")
        mHuaTiPage = 0
        onRefresh()
    }

    override fun getCircleQAByCircleIdSuccess(list: MutableList<CircleDetailBean>) {
        LogCat.e("getCircleQAByCircleIdSuccess~ " + list.size)

//        val emptyView = EmptyView.getEmptyView2(mActivity, "暂无内容", R.drawable.no_data)
//        mAdapter.emptyView = emptyView

        mRefreshLayout.finishRefresh()
        if (mHuaTiPage == 0) {
            (mRefreshLayout.refreshHeader as DefaultRefreshHeader).setSuccess(true)
            mRefreshLayout.finishRefresh()
            mAdapter.setNewData(list)
            rv_circle_detail.scrollToPosition(0)

//            mHuaTiPage++
//            onRefresh()
        } else {
            mAdapter.addData(list)
        }

        mAdapter.notifyDataSetChanged()

        mHuaTiPage++

        if (list.isEmpty()) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    //付费加入接口
    override fun joinCircleByZzbForAndroidSuccess() {

    }

    override fun getAccountDetailsSuccess(bean: MoneyBean) {
        circleyue = bean.accountMoney
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun postSuccess() {
        toastShow("加入成功")
        finish()
    }

    override fun postFail(msg: String?) {
        toastShow(msg)
    }

    override fun setListener() {

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        //去圈子成员列表

        detail_to_man_list.setOnClickListener {
            //            toastShow("to man list")
            mActivity.startActivity(CircleManListActivity::class.java) {
                it.putExtra("circleID", circleID)
                it.putExtra("create", creater)
            }
        }

        //去发布提问
        ll_comment_input.setOnClickListener {
            mActivity.startActivity(CircleQuestionActivity::class.java) {
                it.putExtra("circleID", circleID)
            }
        }

        et_comment.setOnClickListener {
            var intent = Intent(this, CircleQuestionActivity::class.java)
            intent.putExtra("circleID", circleID)

            mActivity.startActivityForResult(intent, 1)

//            mActivity.startActivity(CircleQuestionActivity::class.java) {
//                it.putExtra("circleID", circleID)
//            }
        }

        //去搜索
        iv_toolbar_right1.setOnClickListener {
            mActivity.startActivity(CircleInsidePreActivity::class.java) {
                it.putExtra("circleId", circleID)
            }
        }

        iv_toolbar_right2.setOnClickListener {
            if (mCircleImOwner) {
                chooseMoreOwner()
            } else {
                chooseMore()
            }
        }

//        bottom_bt.setOnClickListener {
//            showjoinhit(circlename, circleprice, "", "")
//        }

        goto_jx.setOnClickListener {
            mActivity.startActivity(CircleRecommendActivity::class.java) {
                it.putExtra("circleID", circleID)
            }
        }

        change_order.setOnClickListener {
            //排序重新加载话题
            orderHuati()
        }

        //底部按钮交钱
        bottom_bt.setOnClickListener {
            //            mActivity.startActivity(WXEntryActivity::class.java){
//                it.putExtra("circleId",circleID)
//                it.putExtra("circleName",circlename)
//                it.putExtra("circlePrice",circleprice)
//            }

            //方便测试
//            if (checkLogin()) {
//                mActivity.startActivity(SelectPayActivity::class.java) {
//                    it.putExtra("circleId", circleID)
//                    it.putExtra("circleName", circlename)
//                    it.putExtra("circlePrice", circleprice)
//                }
//            }

            //现在
//            LogCat.e("circleName "+circlename+"circleprice "+circleprice+" circleenndTime"+circleendtime+" eyu "+circleyue)
            JoinCircle(circlename, "￥ " + circleprice + "($circleprice 智者币)", ZhiZheUtils.timeChange(circleendtime) + "到期", circleyue)

        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        LogCat.e("hasJoinBoolean" + hasJoinBoolean + "mCircleImOwner" + mCircleImOwner)

        if (hasJoinBoolean || mCircleImOwner) {
            when (view.id) {
                R.id.circle_detail_text, R.id.circle_detail_img, R.id.circle_detail_content -> {
                    val bean = adapter.data[position] as CircleDetailBean
//                toastShow("进入话题中" + bean.id)
                    mActivity.startActivity(CircleQuestionDetailActivity::class.java) {
                        it.putExtra("huatiId", bean.id)
                        it.putExtra("CircleId", circleID)
                    }
                }

                R.id.circle_detail_more -> {
                    if (mCircleImOwner) {
                        val bean = adapter.data[position] as CircleDetailBean
                        manageHTowner(bean.circleFix, bean.id, position)
                    }
                }
            }
        }
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

        rv_circle_detail.layoutManager = object : GridLayoutManager(this, 1) {
            override fun canScrollVertically() = false
        }
//        rv_circle_detail.isNestedScrollingEnabled = false
//        rv_circle_detail.setHasFixedSize(true)
//        rv_circle_detail.isFocusable = false

        rv_circle_detail.adapter = mAdapter
    }

    private fun initView() {
        detail_circle_appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                when (state) {
                    State.EXPANDED -> {
                        LogCat.e("展开状态")
                        detail_time.visibility = View.VISIBLE
                        detail_star.visibility = View.VISIBLE
                        detail_num.visibility = View.VISIBLE

                        iv_toolbar_back.setImageResource(R.drawable.circle_back_white)
                    }

                    State.COLLAPSED -> {
                        LogCat.e("折叠状态")
                        detail_time.visibility = View.GONE
                        detail_star.visibility = View.GONE
                        detail_num.visibility = View.GONE

                        iv_toolbar_back.setImageResource(R.drawable.common_back)
                    }
                    else -> {
                        //中间状态
                    }
                }
            }
        })

        //数据未加载时
        iv_toolbar_right2.isEnabled = false
        iv_toolbar_right1.isEnabled = false

    }

    //推荐即左右滑动的文章
    private fun loadTuiJianArticle(list: CircleBean) {
//        for (t in list.partialArticleList) {
//            LogCat.e("推荐的数据测试" + t.title)
//        }

        if (list.partialArticleList.size > 0) {
            list.partialArticleList.forEach {
                mClassifyData.add(ContentBean(it.name, it.cardType))
            }

            gv_circle_classify2.pageSize = 4
//            gv_circle_classify2.setGridItemClickListener { pos, position, str ->
//                val intent = Intent(mActivity, ArticleDetailsActivity::class.java)
//                intent.putExtra("cardBean", list.partialArticleList[position])
//                mActivity.startActivity(intent)
//            }
            gv_circle_classify2.setGridItemClickListener(object : GridItemClickListener {

                override fun click(pos: Int, position: Int, str: String?) {
                }

                override fun click_type(pos: Int, position: Int, str: String?, type: Int) {
                    if (type == 2) {
//                        val intent = Intent(mActivity, ArticleDetailsActivity::class.java)
//                        intent.putExtra("cardBean", list.partialArticleList[position])
//                        mActivity.startActivity(intent)

                        mActivity.startActivity(ArticleDetailsActivity::class.java){
                            it.putExtra("cardBean",list.partialArticleList[position])
                        }
                    }else if (type == 1){
                        mActivity.startActivity(SingleCardDetailsActivity::class.java) {
                            it.putExtra("cardBean", list.partialArticleList[position])
                        }
                    }
                }
            })
            gv_circle_classify2.init(mClassifyData)
        }
    }

    //人头像
    private fun loadJoinUserImg(bean: CircleBean) {
        if (bean.memberList.size > 0) {
            if (bean.memberList[0].avatar.isNotEmpty() && bean.memberList[0].avatar != "") {
                detail_round_img1.visibility = View.VISIBLE
                ImageLoader.load(mActivity, bean.memberList[0].avatar, R.drawable.default_card, detail_round_img1)
            }
        }

        if (bean.memberList.size > 1) {
            if (bean.memberList[1].avatar.isNotEmpty() && bean.memberList[1].avatar != "") {
                detail_round_img2.visibility = View.VISIBLE
                ImageLoader.load(mActivity, bean.memberList[1].avatar, R.drawable.default_card, detail_round_img2)
            }
        }

        if (bean.memberList.size > 2) {
            if (bean.memberList[2].avatar.isNotEmpty() && bean.memberList[2].avatar != "") {
                detail_round_img3.visibility = View.VISIBLE
                ImageLoader.load(mActivity, bean.memberList[2].avatar, R.drawable.default_card, detail_round_img3)
            }
        }

        if (bean.memberList.size > 3) {
            if (bean.memberList[3].avatar.isNotEmpty() && bean.memberList[3].avatar != "") {
                detail_round_img4.visibility = View.VISIBLE
                ImageLoader.load(mActivity, bean.memberList[3].avatar, R.drawable.default_card, detail_round_img4)
            }
        }

        if (bean.memberList.size > 4) {
            if (bean.memberList[4].avatar.isNotEmpty() && bean.memberList[4].avatar != "") {
                detail_round_img5.visibility = View.VISIBLE
                ImageLoader.load(mActivity, bean.memberList[4].avatar, R.drawable.default_card, detail_round_img5)
            }
        }

        if (bean.memberList.size > 5) {
            if (bean.memberList[5].avatar.isNotEmpty() && bean.memberList[5].avatar != "") {
                detail_round_img6.visibility = View.VISIBLE
                ImageLoader.load(mActivity, bean.memberList[5].avatar, R.drawable.default_card, detail_round_img6)
            }
        }
    }

    fun onRefresh() {
        getCircleQAByCircleIdRefresh()
    }

    private fun getCircleQAByCircleIdRefresh() {
        mPresenter.getCircleQAByCircleId(mHuaTiOrder, circleID, mHuaTiPage, mHuaTiPageSize)
    }

    //游客或者会员的情况
    private fun chooseMore() {
        XPopup.get(mActivity)
                .asCustom(CirclePopup(this, "更多", arrayOf("圈子介绍", "圈子评分", "会员续费", "分享圈子", "举报圈子"),
                        null, -1, OnSelectListener { position, text ->
                    when (position) {
                        0 -> {
                            startActivity(CircleIntroductionActivity::class.java) {
                                it.putExtra("info", mIntroduction)
                            }
                        }

                        1 -> {
                            startActivity(CirclePingFenActivity::class.java) {
                                it.putExtra("circleId", circleID)
                            }
                        }

                        3 -> {
                            showshare()
                        }
                        4 -> {
                            jubaoCircle()
                        }
                    }
                })).show()
    }

    //圈主的情况
    private fun chooseMoreOwner() {
        XPopup.get(mActivity)
                .asCustom(CirclePopup(this, "更多", arrayOf("内容管理", "编辑圈子", "圈子介绍", "分享圈子"),
                        null, -1, OnSelectListener { position, text ->
                    when (position) {
                        0 -> {
                            startActivity(OwnerManageContentActivity::class.java) {
                                it.putExtra("circleId", circleID)
                            }
                        }

                        1 -> {
                            onBackPressed()
                            startActivity(CircleEditActivity::class.java) {
                                it.putExtra("title", circlename)
                                it.putExtra("levelType", circleprice)
                                it.putExtra("sign", mIntroduction)
                                it.putExtra("mImageUrl", mImageUrl)
                                it.putExtra("labelName", labelName)
                                it.putExtra("classifyId", classifyId)
                                it.putExtra("limitedTimeType", limitedTimeType)

                                it.putExtra("circleId", circleID)
                            }
                        }

                        2 -> {
                            startActivity(CircleIntroductionActivity::class.java) {
                                it.putExtra("info", mIntroduction)
                            }
                        }

                        3 -> {
                            showshare()
                        }
                    }
                })).show()
    }

    //提醒你加入交钱
    private fun showjoinhit(circlename: String, circleprice: String, circleendtime: String, circleyue: String) {
        XPopup.get(mActivity)
                .asCustom(CircleBottomPopup2(this, OnSelectListener { position, text ->

                })
                ).show()
    }

    //分享
    //弹出分享四兄弟
    private fun showshare() {
        XPopup.get(mActivity)
                .asCustom(CircleBottomSharePopup(this,
                        OnSelectListener { position, text ->
                            when (position) {
                                1 -> {
                                    toastShow("微信")
                                }

                                2 -> {
                                    toastShow("朋友圈")
                                }

                                3 -> {
                                    toastShow("球球号")
                                }
                                4 -> {
                                    toastShow("微博")
                                }
                            }
                        })
                ).show()
    }

    //举报圈子
    private fun jubaoCircle() {
        XPopup.get(mActivity)
                .asCustom(CirclePopup(this, "举报类型", arrayOf("政治敏感", "垃圾广告", "恶意攻击", "色情低俗", "其它"), null, -1,
                        OnSelectListener { position, text ->
                            mPresenter.reportCircle(circleID, position)
                        })
                ).show()
    }

    //管理话题 分为人和管理
    //管理包括置顶操作和取消置顶的操作
    private fun manageHTowner(circleFix: Boolean, id: Int, weizhi: Int) {
        var type = -1
        type = if (circleFix) {
            2
        } else {
            3
        }

        XPopup.get(mActivity)
                .asCustom(HuatiManagePopup(this, type, circleFix, -1,
                        OnSelectListener { position, text ->
                            when (position) {
                                0 -> {
                                    rv_circle_detail.scrollToPosition(0)
                                    mAdapter.add(0, mAdapter.data[weizhi])
                                    mAdapter.remove(weizhi + 1)
                                    mPresenter.setQAFixTop(id, 1)
                                }
                                1 -> {
                                    deleteHuaTi(id)
                                }
                                2 -> {
                                    mPresenter.setQAFixTop(id, 0)
                                    rv_circle_detail.scrollToPosition(0)
                                    mHuaTiPage = 0
                                    onRefresh()
                                }

                            }
                        })
                ).show()
    }

    private fun manageHTman() {

    }

    //删除话题
    private fun deleteHuaTi(qaId: Int) {
        XPopup.get(mActivity)
                .asCustom(BottomInfoPopup(this, "该操作将删除此话题和所有关联回复，是否继续？", -1,
                        OnSelectListener { position, text ->
                            if (position == 2) {
                                mPresenter.deleteQa(qaId)
                            }
                        })
                ).show()
    }

    //排序话题
    private fun orderHuati() {
        XPopup.get(mActivity)
                .asCustom(CirclePopup(this, "排序类型", arrayOf("默认排序", "只看圈主", "只看我的", "最新话题"),
                        null, mHuaTiOrder,
                        OnSelectListener { position, text ->
                            mHuaTiOrder = position
                            mHuaTiPage = 0
                            onRefresh()
                        })
                ).show()
    }

    //加入圈子
    private fun JoinCircle(t1: String, t2: String, t3: String, t4: String) {

        if (checkLogin()) {
            if (t4.parseFloat() > 0) {
                XPopup.get(mActivity)
                        .asCustom(CircleJoinPopup(this, t1, t2, t3, t4, "立即加入", -1,
                                OnSelectListener { position, text ->
                                    mPresenter.joinCircleByZzbForAndroid(circleID)
                                })
                        ).show()
            } else {
                XPopup.get(mActivity)
                        .asCustom(CircleJoinPopup(this, t1, t2, t3, t4, "充值并兑换", -1,
                                OnSelectListener { position, text ->
                                    if (checkLogin()) {
                                        mActivity.startActivity(SelectPayActivity::class.java) {
                                            it.putExtra("circleId", circleID)
                                            it.putExtra("circleName", circlename)
                                            it.putExtra("circlePrice", circleprice)
                                        }
                                    }
                                })
                        ).show()
            }
        }
    }

    //评分显示
    private fun numScore(num: Int) {
        when (num) {
            0 -> {

            }

            1 -> {
                ImageLoader.load(this, R.drawable.star_on_white, star_1)
            }

            2 -> {
                ImageLoader.load(this, R.drawable.star_on_white, star_1)
                ImageLoader.load(this, R.drawable.star_on_white, star_2)
            }

            3 -> {
                ImageLoader.load(this, R.drawable.star_on_white, star_1)
                ImageLoader.load(this, R.drawable.star_on_white, star_2)
                ImageLoader.load(this, R.drawable.star_on_white, star_3)
            }

            4 -> {
                ImageLoader.load(this, R.drawable.star_on_white, star_1)
                ImageLoader.load(this, R.drawable.star_on_white, star_2)
                ImageLoader.load(this, R.drawable.star_on_white, star_3)
                ImageLoader.load(this, R.drawable.star_on_white, star_4)
            }
            5 -> {
                ImageLoader.load(this, R.drawable.star_on_white, star_1)
                ImageLoader.load(this, R.drawable.star_on_white, star_2)
                ImageLoader.load(this, R.drawable.star_on_white, star_3)
                ImageLoader.load(this, R.drawable.star_on_white, star_4)
                ImageLoader.load(this, R.drawable.star_on_white, star_5)
            }
        }
    }
}
