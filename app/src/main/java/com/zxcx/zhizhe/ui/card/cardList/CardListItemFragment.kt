package com.zxcx.zhizhe.ui.card.cardList

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
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.youth.banner.BannerConfig
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AddCardDetailsListEvent
import com.zxcx.zhizhe.event.UpdateCardListEvent
import com.zxcx.zhizhe.event.UpdateCardListPositionEvent
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardAdapter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_card_list_item.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 首页-卡片-列表-列表内Fragment
 */

private const val ARG_ID = "categoryId"

//class CardListItemFragment : BaseFragment(), IGetPresenter<MutableList<CardBean>>,
//		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
//		OnRefreshListener {
class CardListItemFragment : MvpFragment<CardListitemPresenter>(), CardListitemContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
        OnRefreshListener {


    private lateinit var mAdapter: CardAdapter
    private var mPage = 0
    private var mCurrentPosition = 0

    private var mAdList: MutableList<ADBean> = mutableListOf()
    private var imageList: MutableList<String> = mutableListOf()

    private var ad_type_position = 0

    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
                CardListItemFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_ID, id)
                    }
                }
    }

    private var categoryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(ARG_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_list_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initView()
        refresh_layout.setOnRefreshListener(this)
        getCardListForCategory(categoryId, mPage)


        ad_type_position = SharedPreferencesUtil.getInt(SVTSConstants.adTypePosition, 0)
        LogCat.e("ADPosition" + ad_type_position)

        //之后根据类型修改
        onRefreshAD(ad_type_position)

    }

    override fun onStart() {
        super.onStart()
        //开始轮播
        banner_card.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        //结束轮播
        banner_card.stopAutoPlay()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            EventBus.getDefault().register(this)
        } else {
            EventBus.getDefault().unregister(this)
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = CardAdapter(arrayListOf())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_card_list_item)
        mAdapter.onItemClickListener = this
        rv_card_list_item.layoutManager = layoutManager
        rv_card_list_item.adapter = mAdapter
    }

    private fun initView() {
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
                it.putExtra("shareDescription",shareDescription)
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

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
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
        intent.putExtra("list", mAdapter.data as ArrayList)
        intent.putExtra("currentPosition", position)
        intent.putExtra("sourceName", this::class.java.name)
        mActivity.startActivity(intent, bundle)
        mCurrentPosition = position
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        getCardListForCategory(categoryId, mPage)
    }

    override fun onLoadMoreRequested() {
        getCardListForCategory(categoryId, mPage)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UpdateCardListPositionEvent) {
        if (this::class.java.name == event.sourceName) {
            if (event.currentPosition == mAdapter.data.size - 1) {
                getCardListForCategory(categoryId, mPage)
            }
            mCurrentPosition = event.currentPosition
            rv_card_list_item.scrollToPosition(event.currentPosition)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UpdateCardListEvent) {
        mAdapter.data[event.currentPosition] = event.cardBean
        mAdapter.notifyItemChanged(event.currentPosition)
    }

    override fun getDataSuccess(list: MutableList<CardBean>) {
        if (mPage == 0) {
            refresh_layout.finishRefresh()
            mAdapter.setNewData(list)
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

        initView()
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        if (list.isNotEmpty()) {
            mAdList = list
            imageList.clear()
            mAdList.forEach {
                imageList.add(it.titleImage)
            }
            fl_banner_card.visibility = View.VISIBLE

            banner_card.setImages(imageList)
            banner_card.start()
        } else {
            fl_banner_card.visibility = View.GONE
        }
//
//        if (ad_type_position != 0) {
//            onRefreshAD(ad_type_position)
//        } else {
//            onRefreshAD(0)
//        }
    }

    override fun createPresenter(): CardListitemPresenter {
        return CardListitemPresenter(this)
    }

    private fun getCardListForCategory(cardCategoryId: Int, page: Int) {
        mDisposable = AppClient.getAPIService().getCardListForCategory(cardCategoryId, page, Constants.PAGE_SIZE)
                .compose(BaseRxJava.handleArrayResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<MutableList<CardBean>>(mPresenter) {
                    override fun onNext(t: MutableList<CardBean>) {
                        getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    public fun onActivityReenter() {
        rv_card_list_item.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                rv_card_list_item.viewTreeObserver.removeOnPreDrawListener(this)
                rv_card_list_item.requestLayout()
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

    private fun onRefreshAD(id: Int) {
        mPresenter.getAD(id)
    }

    override fun closeAD() {
        fl_banner_card.visibility = View.GONE
    }
}
