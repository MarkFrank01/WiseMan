package com.zxcx.zhizhe.ui.card.hot

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.youth.banner.BannerConfig
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AddCardDetailsListEvent
import com.zxcx.zhizhe.event.HomeClickRefreshEvent
import com.zxcx.zhizhe.event.UpdateCardListEvent
import com.zxcx.zhizhe.event.UpdateCardListPositionEvent
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback
import com.zxcx.zhizhe.loadCallback.HomeNetworkErrorCallback
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.my.daily.DailyActivity
import com.zxcx.zhizhe.ui.my.invite.InviteBean
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.DefaultRefreshHeader
import com.zxcx.zhizhe.widget.centerpopup.InviteCenterPopup
import kotlinx.android.synthetic.main.fragment_hot.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 首页-卡片-推荐页面
 */

class HotCardFragment : RefreshMvpFragment<HotCardPresenter>(), HotCardContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {



    private lateinit var mAdapter: HotCardAdapter
    private var mPage = 0
    private var mHidden = true
    private var mLastDate = Date()
    private var mCurrentPosition = 0


    private var mAdList: MutableList<ADBean> = mutableListOf()
    private var imageList: MutableList<String> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_hot, container, false)
        mRefreshLayout = root.findViewById(R.id.refresh_layout)
        val loadSir = LoadSir.Builder()
                .addCallback(HomeLoadingCallback())
                .addCallback(LoginTimeoutCallback())
                .addCallback(HomeNetworkErrorCallback())
                .setDefaultCallback(HomeLoadingCallback::class.java)
                .build()
        loadService = loadSir.register(root) { v ->
            loadService.showCallback(HomeLoadingCallback::class.java)
            getHotCard()
        }
        return loadService.loadLayout
    }

    override fun createPresenter(): HotCardPresenter {
        return HotCardPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initRecyclerView()
        getHotCard()
        mHidden = false
        initADView()
        onRefreshAD()

        //正常流程
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0){
            if (Utils.getIsFirstTan()){
                showWindow()
                Utils.setIsFirstTan()
            }
        }

        //测试弹窗
//        showWindow()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.setUserVisibleHint(hidden)
        mHidden = hidden
    }

    override fun onPause() {
        super.onPause()
        rv_hot_card.itemAnimator = null
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun clearLeaks() {
        loadService = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HomeClickRefreshEvent) {
        if (!mHidden) {
            rv_hot_card.scrollToPosition(0)
            mPage = 0
            getHotCard()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UpdateCardListPositionEvent) {
        if (this::class.java.name == event.sourceName) {
            if (event.currentPosition == mAdapter.data.size - 1) {
                getHotCard()
            }
            mCurrentPosition = event.currentPosition
            rv_hot_card.scrollToPosition(event.currentPosition)
            fl_banner_card.visibility = View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UpdateCardListEvent) {
        mAdapter.data[event.currentPosition] = event.cardBean
        mAdapter.notifyItemChanged(event.currentPosition)
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        getHotCard()
        fl_banner_card.visibility = View.VISIBLE
    }

    private fun onRefreshAD() {
        mPresenter.getAD()
    }


    override fun onLoadMoreRequested() {
        getHotCard()
    }

    override fun inputInvitationCodeSuccess(bean: InviteBean) {
        toastShow("填写完成")
    }

    override fun errormsg(msg: String) {
        LogCat.e("????"+msg)
//        toastShow(msg)
        showWindow_check(msg)
    }

    override fun getDataSuccess(list: MutableList<CardBean>) {
//        if (mPage == 0&&list.size>=4) {
//            list[4].cardType = CardBean.Article_TOUTIAO
//        }
        loadService.showSuccess()
        if (mPage == 0) {
            (mRefreshLayout.refreshHeader as DefaultRefreshHeader).setSuccess(true)
            mRefreshLayout.finishRefresh()
            mAdapter.setNewData(list)
            rv_hot_card.scrollToPosition(0)
        } else {
            mAdapter.addData(list)
            val event = AddCardDetailsListEvent(this::class.java.name, list as ArrayList<CardBean>)
            EventBus.getDefault().post(event)
        }
        mPage++
        if (list.isEmpty()) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        loadService.showSuccess()
        if (list.size > 0) {
            mAdList = list
            imageList.clear()
            mAdList.forEach {
                imageList.add(it.titleImage)
            }
            fl_banner_card.visibility = View.VISIBLE
        } else {
            fl_banner_card.visibility = View.GONE
        }
        banner_card.setImages(imageList)
        banner_card.start()
    }

    override fun toastFail(msg: String) {
        super.toastFail(msg)
        mAdapter.loadMoreFail()
        if (mPage == 0) {
            mRefreshLayout.finishRefresh()
            loadService.showCallback(HomeNetworkErrorCallback::class.java)
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = HotCardAdapter(arrayListOf())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_hot_card)
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this
        rv_hot_card.layoutManager = layoutManager
        rv_hot_card.adapter = mAdapter
    }

    private fun initADView() {
        banner_card.setImageLoader(GlideBannerImageLoader())
        banner_card.setIndicatorGravity(BannerConfig.RIGHT)
        banner_card.setOnBannerListener {
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
                it.putExtra("shareDescription", shareDescription)
            }
        }

        banner_card.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val newPosition = position % 3
                val ad = mAdList[newPosition]
                if (mAdList.size>0) {

                    if (iv_ad_label_card!=null) {

                        when (ad.styleType) {

                            0 -> {
                                iv_ad_label_card.setImageResource(R.drawable.iv_ad_label_0)
                            }
                            1 -> {
                                iv_ad_label_card.setImageResource(R.drawable.iv_ad_label_1)
                            }
                            2 -> {
                                iv_ad_label_card.setImageResource(R.drawable.iv_ad_label_2)
                            }
                        }
                    }
                }
            }

        })
    }

    private fun getHotCard() {
        mPresenter.getHotCard(mLastDate.time.toString(), mPage)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

//        if (position == 4) {
//            val mIntent = Intent(mActivity, DailyActivity::class.java)
//            mActivity.startActivity(mIntent)
//        }
//
            val bean = adapter.data[position] as CardBean
            val cardImg = view.findViewById<ImageView>(R.id.iv_item_card_icon)
            val cardTitle = view.findViewById<TextView>(R.id.tv_item_card_title)
            val cardCategory = view.findViewById<TextView>(R.id.tv_item_card_category)
            val cardLabel = view.findViewById<TextView>(R.id.tv_item_card_label)
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                    Pair.create(cardImg, cardImg.transitionName),
                    Pair.create(cardTitle, cardTitle.transitionName),
                    Pair.create(cardCategory, cardCategory.transitionName),
                    Pair.create(cardLabel, cardLabel.transitionName)).toBundle()
            val intent = Intent(mActivity, CardDetailsActivity::class.java)
            val pushAdapter = mAdapter.data as ArrayList
            intent.putExtra("list", mAdapter.data as ArrayList)
            intent.putExtra("list", pushAdapter)
            intent.putExtra("currentPosition", position)
            intent.putExtra("sourceName", this::class.java.name)
            mActivity.startActivity(intent, bundle)
            mCurrentPosition = position
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        var content: String
            when (view.id) {
                R.id.position_4 -> {
                    val bean = adapter.data[position] as CardBean
                    val cardImg = view.findViewById<ImageView>(R.id.iv_item_card_icon)
                    val cardTitle = view.findViewById<TextView>(R.id.tv_item_card_title)
                    val cardCategory = view.findViewById<TextView>(R.id.tv_item_card_category)
                    val cardLabel = view.findViewById<TextView>(R.id.tv_item_card_label)
                    val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                            Pair.create(cardImg, cardImg.transitionName),
                            Pair.create(cardTitle, cardTitle.transitionName),
                            Pair.create(cardCategory, cardCategory.transitionName),
                            Pair.create(cardLabel, cardLabel.transitionName)).toBundle()
                    val intent = Intent(mActivity, CardDetailsActivity::class.java)
                    val pushAdapter = mAdapter.data as ArrayList
                    intent.putExtra("list", mAdapter.data as ArrayList)
                    intent.putExtra("list", pushAdapter)
                    intent.putExtra("currentPosition", position)
                    intent.putExtra("sourceName", this::class.java.name)
                    mActivity.startActivity(intent, bundle)
                    mCurrentPosition = position
                }

                R.id.iv_toutiao -> {
                    val mIntent = Intent(mActivity, DailyActivity::class.java)
                    mActivity.startActivity(mIntent)
                }
            }
    }

    fun onActivityReenter() {
        rv_hot_card.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                rv_hot_card.viewTreeObserver.removeOnPreDrawListener(this)
                rv_hot_card.requestLayout()
                mActivity.startPostponedEnterTransition()
                return true
            }
        })
    }

    fun getSharedView(names: MutableList<String>): MutableMap<String, View> {
        val sharedElements = mutableMapOf<String, View>()
        val cardImg = mAdapter.getViewByPosition(mCurrentPosition, R.id.iv_item_card_icon)
        val cardTitle = mAdapter.getViewByPosition(mCurrentPosition, R.id.tv_item_card_title)
        val cardCategory = mAdapter.getViewByPosition(mCurrentPosition, R.id.tv_item_card_category)
        val cardLabel = mAdapter.getViewByPosition(mCurrentPosition, R.id.tv_item_card_label)
        cardImg?.let { sharedElements[cardImg.transitionName] = it }
        cardTitle?.let { sharedElements[cardTitle.transitionName] = it }
        cardCategory?.let { sharedElements[cardCategory.transitionName] = it }
        cardLabel?.let { sharedElements[cardLabel.transitionName] = it }
        return sharedElements
    }

    private fun showWindow() {
        XPopup.Builder(mActivity)
                .asCustom(InviteCenterPopup(mActivity,
                        OnSelectListener { position, text ->
                            if (position == 2) {
//                                LogCat.e("text is $text")
//                                toastShow("进行校验")
                                mPresenter.inputInvitationCode(text)
                            }
                        })
                ).show()

    }

    private fun showWindow_check(hint:String) {
        XPopup.Builder(mActivity)
                .asCustom(InviteCenterPopup(mActivity,hint,
                        OnSelectListener { position, text ->
                            if (position == 2) {
                                mPresenter.inputInvitationCode(text)
                            }
                        })
                ).show()

    }
}