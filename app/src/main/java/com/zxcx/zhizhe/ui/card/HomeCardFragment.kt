package com.zxcx.zhizhe.ui.card

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.uuch.adlibrary.AdConstant
import com.uuch.adlibrary.AdManager
import com.uuch.adlibrary.bean.AdInfo
import com.youth.banner.transformer.DepthPageTransformer
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.GotoCardListEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.card.attention.AttentionCardFragment
import com.zxcx.zhizhe.ui.card.cardList.CardListFragment
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.card.hot.HotCardFragment
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity
import com.zxcx.zhizhe.ui.search.search.SearchActivity
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.fragment_home_card.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 首页-卡片Fragment
 */

//class HomeCardFragment : BaseFragment() {
class HomeCardFragment : MvpFragment<HomeCardPresenter>(), HomeCardContract.View {


    private val mHotFragment = HotCardFragment()
    private val mAttentionFragment = AttentionCardFragment()
    private val mListFragment = CardListFragment()
    private var mCurrentFragment = Fragment()

    private var advList: ArrayList<AdInfo> = ArrayList()
    private var mAdList: MutableList<ADBean> = mutableListOf()


    private val titles = arrayOf("关注", "推荐", "列表")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        for (i in titles.indices) {
            val tab = tl_home.newTab()
            tab.setCustomView(R.layout.tab_home)
            val textView = tab.customView?.findViewById<TextView>(R.id.tv_tab_home)
            textView?.text = titles[i]
            tl_home.addTab(tab)
        }
        tl_home.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        switchFragment(mAttentionFragment)
                    }
                    1 -> {
                        switchFragment(mHotFragment)
                    }
                    2 -> {
                        switchFragment(mListFragment)
                    }
                }
                val textView = tab.customView?.findViewById(R.id.tv_tab_home) as TextView
                textView.paint.isFakeBoldText = true
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val textView = tab.customView?.findViewById(R.id.tv_tab_home) as TextView
                textView.paint.isFakeBoldText = false
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val para = tl_home.layoutParams
        val screenWidth = ScreenUtils.getDisplayWidth() //屏幕宽度
        para.width = screenWidth * 1 / 2
        tl_home.layoutParams = para

        tl_home.getTabAt(1)?.select()

        onRefreshAD()
//        showFirstDialog("")
//        val adManager = AdManager(activity,advList)
//        adManager.setOverScreen(true)
//                .setPageTransformer(DepthPageTransformer())
//                .setOnImageClickListener { view, advInfo ->
//                    toastShow("get AD")
//                }
//
//        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }

    override fun setListener() {
        super.setListener()
        iv_home_search.setOnClickListener {
            mActivity.startActivity(SearchActivity::class.java, {})
        }
        iv_home_interests.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(SelectAttentionActivity::class.java, {})
            }
        }
    }

    private fun switchFragment(newFragment: Fragment) {

        val fm = childFragmentManager
        val transaction = fm.beginTransaction()

        if (newFragment.isAdded) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_home, newFragment).commitAllowingStateLoss()
        }
        mCurrentFragment = newFragment
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    public fun onActivityReenter() {
        when (mCurrentFragment) {
            mHotFragment -> mHotFragment.onActivityReenter()
            mAttentionFragment -> mAttentionFragment.onActivityReenter()
            mListFragment -> mListFragment.onActivityReenter()
        }
    }

    fun getSharedView(names: MutableList<String>): MutableMap<String, View>? {
        return when (mCurrentFragment) {
            mHotFragment -> mHotFragment.getSharedView(names)
            mAttentionFragment -> mAttentionFragment.getSharedView(names)
            mListFragment -> mListFragment.getSharedView(names)
            else -> mListFragment.getSharedView(names)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: GotoCardListEvent) {
        //去往卡片列表页
        tl_home.getTabAt(2)?.select()
    }

    private fun addImageData(url: String) {
        val adInfo = AdInfo()
        adInfo.activityImg = url
        advList.add(adInfo)
    }

    private fun showImageDialog(title: String, url: String) {
        val adManager = AdManager(activity, advList)
        adManager.setOverScreen(true)
                .setPageTransformer(DepthPageTransformer())
                .setOnImageClickListener { _, _ ->
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("url",url)
                    startActivity(intent)
                }
        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }


    override fun createPresenter(): HomeCardPresenter {
        return HomeCardPresenter(this)
    }

    override fun getADSuccess(list: MutableList<ADBean>) {

        var title = ""
        var url = ""

        if (list.size > 0) {
            mAdList = list
            mAdList.forEach {
                addImageData(it.titleImage)
                title = it.description
                url = it.behavior
            }
            showImageDialog(title, url)
        }

    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

    private fun onRefreshAD() {
        mPresenter.getAD()
    }
}
