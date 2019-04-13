package com.zxcx.zhizhe.ui.rank

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.callback.Callback
import com.uuch.adlibrary.AdConstant
import com.uuch.adlibrary.AdManager
import com.uuch.adlibrary.bean.AdInfo
import com.youth.banner.BannerConfig
import com.youth.banner.transformer.DepthPageTransformer
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.LoginEvent
import com.zxcx.zhizhe.event.LogoutEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.rank.moreRank.AllRankActivity
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.fragment_rank.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 首页-榜单页面
 */

class RankActivity : MvpActivity<RankPresenter>(), RankContract.View, BaseQuickAdapter.OnItemClickListener, Callback.OnReloadListener {


    private var mAdList: MutableList<ADBean> = mutableListOf()
    private val imageList: MutableList<String> = mutableListOf()
    private var mUserId: Int = 0
    private lateinit var mAdapter: RankAdapter

    var advList: ArrayList<AdInfo> = ArrayList()

    private var lastADTime: Long = 0
    private var lastADID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_rank)
        EventBus.getDefault().register(this)
        initRecyclerView()
        initView()
        onRefresh()

        lastADTime = SharedPreferencesUtil.getLong(SVTSConstants.homeRankLastOpenedTime, 0)
        lastADID = SharedPreferencesUtil.getInt(SVTSConstants.homeRankLastOpenedID, 0)


        onRefreshAD(lastADTime, lastADID.toLong())
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val root = inflater.inflate(R.layout.fragment_rank, container, false)
//        loadService = LoadSir.getDefault().register(root, this)
//        EventBus.getDefault().register(this)
//        return loadService.loadLayout
//    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initRecyclerView()
//        initView()
//        onRefresh()
//
//        lastADTime = SharedPreferencesUtil.getLong(SVTSConstants.homeRankLastOpenedTime, 0)
//        lastADID = SharedPreferencesUtil.getInt(SVTSConstants.homeRankLastOpenedID, 0)
//
//
//        onRefreshAD(lastADTime, lastADID.toLong())
//    }

    override fun createPresenter(): RankPresenter {
        return RankPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        //开始轮播
        banner_rank.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        //结束轮播
        banner_rank.stopAutoPlay()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onReload(v: View?) {
        onRefresh()
    }

    private fun onRefresh() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        if (mUserId != 0) {
            mPresenter.getMyRank()
        }
        mPresenter.getTopTenRank()
        mPresenter.getAD()
    }

    override fun getMyRankSuccess(bean: UserRankBean) {
//        loadService.showSuccess()

        if (bean.authenticationType != 0 && bean.authenticationType == 1) {
            iv_item_card_officials.visibility = View.VISIBLE
        }

        tv_item_rank_user_name.text = bean.name
        tv_item_rank_user_card.text = bean.cardNum.toString()
        tv_item_rank_user_fans.text = bean.fansNum.toString()
        tv_item_rank_user_like.text = bean.likeNum.toString()
        tv_item_rank_user_collect.text = bean.collectNum.toString()
        tv_item_rank_user_rank.text = bean.rankIndex.toString()
        tv_item_rank_user_level.text = getString(R.string.tv_level, bean.level)
        val imageUrl = ZhiZheUtils.getHDImageUrl(bean.imageUrl)
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_header, iv_item_rank_user)

        when {
            bean.rankChange > 0 -> {
                TextViewUtils.setTextLeftDrawable(mActivity, R.drawable.tv_rank_up, tv_my_rank_change)
                tv_my_rank_change.text = bean.rankChange.toString()
            }
            bean.rankChange == 0 -> {
                TextViewUtils.setTextLeftDrawable(mActivity, R.drawable.tv_rank_invariability, tv_my_rank_change)
                tv_my_rank_change.text = ""
            }
            else -> {
                TextViewUtils.setTextLeftDrawable(mActivity, R.drawable.tv_rank_down, tv_my_rank_change)
                tv_my_rank_change.text = Math.abs(bean.rankChange).toString()
            }
        }
    }

    override fun getDataSuccess(list: List<UserRankBean>) {
//        loadService.showSuccess()
        mAdapter.setNewData(list)
        initView()
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
//        loadService.showSuccess()
        if (list.size > 0) {
            mAdList = list
            imageList.clear()
            mAdList.forEach {
                imageList.add(it.titleImage)
            }
            fl_banner_rank.visibility = View.VISIBLE
        } else {
            fl_banner_rank.visibility = View.GONE
        }
        banner_rank.setImages(imageList)
        banner_rank.start()
    }

    override fun getDialogADSuccess(list: MutableList<ADBean>) {

        var title = ""
        var url = ""
        var id1 = 0

        if (list.size > 0) {
            mAdList = list
            mAdList.forEach {
                addImageData(it.titleImage)
                title = it.description
                url = it.behavior
                id1 = it.id
            }

            SharedPreferencesUtil.saveData(SVTSConstants.homeRankLastOpenedTime,System.currentTimeMillis())
            SharedPreferencesUtil.saveData(SVTSConstants.homeRankLastOpenedID,id1)

            showImageDialog(title, url)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LoginEvent) {
        refreshView()
        onRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LogoutEvent) {
        refreshView()
    }

    private fun gotoMoreRank() {
        val intent = Intent(mActivity, AllRankActivity::class.java)
        startActivity(intent)
    }

    override fun startLogin() {
        ZhiZheUtils.logout()
        toastShow(R.string.login_timeout)
        startActivity(Intent(mActivity, LoginActivity::class.java))
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as UserRankBean
        val intent = Intent(mActivity, OtherUserActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    override fun setListener() {
        tv_rank_more_rank.setOnClickListener { gotoMoreRank() }
        tv_rank_need_login.setOnClickListener { mActivity.startActivity(LoginActivity::class.java) {} }
    }

    private fun initRecyclerView() {
        mAdapter = RankAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        rv_rank.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_rank.adapter = mAdapter
//        val footer = LayoutInflater.from(mActivity).inflate(R.layout.layout_footer_rank, null)
//        mAdapter.addFooterView(footer)
    }

    private fun initView() {
        refreshView()

        banner_rank.setImageLoader(GlideBannerImageLoader())
        banner_rank.setIndicatorGravity(BannerConfig.RIGHT)
        banner_rank.setOnBannerListener {
            val adUrl = mAdList[it].behavior
            val adTitle = mAdList[it].description
//            val adImage = mAdList[it].titleImage
            val adImage = mAdList[it].shareImage
            val shareDescription = mAdList[it].shareDescription
            mActivity.startActivity(WebViewActivity::class.java) {
                it.putExtra("isAD", true)
                it.putExtra("title", adTitle)
                it.putExtra("url", adUrl)
                it.putExtra("imageUrl", adImage)
                it.putExtra("shareDescription",shareDescription)
            }
        }
        banner_rank.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
				val newPosition = position % 3
				val ad = mAdList[newPosition]
				when (ad.styleType) {
					0 -> {
						iv_ad_label.setImageResource(R.drawable.iv_ad_label_0)
					}
					1 -> {
						iv_ad_label.setImageResource(R.drawable.iv_ad_label_1)
					}
					2 -> {
						iv_ad_label.setImageResource(R.drawable.iv_ad_label_2)
					}
				}
            }

        })
    }

    private fun refreshView() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        if (mUserId == 0) {
            ImageLoader.load(mActivity, "", R.drawable.default_header, iv_item_rank_user)
            tv_rank_need_login.visibility = View.VISIBLE
            tv_my_rank_change.visibility = View.GONE
        } else {
            tv_rank_need_login.visibility = View.GONE
            tv_my_rank_change.visibility = View.VISIBLE
        }
    }

//    private fun showFirstDialog() {
//        val adInfo = AdInfo()
//        adInfo.activityImg = "https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png"
//        advList.add(adInfo)
//    }

    private fun addImageData(url: String) {
        val adInfo = AdInfo()
        adInfo.activityImg = url
        advList.add(adInfo)
    }

    private fun showImageDialog(title: String, url: String) {
        val adManager = AdManager(this, advList)
        adManager.setOverScreen(true)
                .setPageTransformer(DepthPageTransformer())
                .setOnImageClickListener { _, _ ->
                    val intent = Intent(this, WebViewActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("url", url)
                    startActivity(intent)
                }
        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }

    private fun onRefreshAD(lastADTime: Long, lastADID: Long) {
        mPresenter.getDialog(lastADTime, lastADID)
    }
}