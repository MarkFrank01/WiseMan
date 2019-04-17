package com.zxcx.zhizhe.ui.card

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.uuch.adlibrary.AdConstant
import com.uuch.adlibrary.AdManager
import com.uuch.adlibrary.bean.AdInfo
import com.youth.banner.transformer.DepthPageTransformer
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.GotoCardListEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.service.DownloadService
import com.zxcx.zhizhe.service.version_update.entity.UpdateApk
import com.zxcx.zhizhe.service.version_update.update_utils.AppUtils
import com.zxcx.zhizhe.ui.card.attention.AttentionCardFragment
import com.zxcx.zhizhe.ui.card.cardList.CardListFragment
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.card.hot.HotCardFragment
import com.zxcx.zhizhe.ui.search.search.SearchActivity
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.centerpopup.InviteCenterPopup
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
    private var updateImageList: ArrayList<AdInfo> = ArrayList()
    private var mAdList: MutableList<ADBean> = mutableListOf()

    private var lastADTime: Long = 0
    private var lastADID: Int = 0

    //            private val titles = arrayOf("关注", "推荐", "列表")
    private val titles = arrayOf("推荐", "列表")

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
//                    0 -> {
//                        switchFragment(mAttentionFragment)
//                    }
//                    1 -> {
//                        switchFragment(mHotFragment)
//                    }
//                    2 -> {
//                        switchFragment(mListFragment)
//                    }
                    0 -> {
                        switchFragment(mHotFragment)
                    }

                    1 -> {
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

//        tl_home.getTabAt(2)?.select()
//        tl_home.getTabAt(0)?.select()
//        tl_home.getTabAt(1)?.select()

        tl_home.getTabAt(1)?.select()
        tl_home.getTabAt(0)?.select()

        lastADTime = SharedPreferencesUtil.getLong(SVTSConstants.homeCardLastOpenedTime, 0)
        lastADID = SharedPreferencesUtil.getInt(SVTSConstants.homeCardLastOpenedID, 0)



        onRefreshAD(lastADTime, lastADID.toLong())
        mPresenter.getCheckUpdateApk(1)
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
                //以前
//                mActivity.startActivity(SelectAttentionActivity::class.java, {})
                //现在
//                mActivity.startActivity(SelectInterestActivity::class.java,{})

                //emmmmmmmmmmmmmmmmm 真正的现在正式使用
//                mActivity.startActivity(NowSelectActivity::class.java) {}

                //方便测试
                //微信吊起
//                mActivity.startActivity(WXEntryActivity::class.java,{})
                //支付宝吊起
//                mActivity.startActivity(ZFBEntryActivity::class.java,{})
                //测试奇怪的弹出窗口
                showWindow()
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
        tl_home.getTabAt(1)?.select()
    }


    override fun createPresenter(): HomeCardPresenter {
        return HomeCardPresenter(this)
    }

    override fun getADSuccess(list: MutableList<ADBean>) {

        var title = ""
        var url = ""
        var id1: Int = 0
        var time = ""

        if (list.size > 0) {
            mAdList = list
            mAdList.forEach {
                addAdImageData(it.titleImage)
                title = it.description
                url = it.behavior
                id1 = it.id
            }

            SharedPreferencesUtil.saveData(SVTSConstants.homeCardLastOpenedTime, System.currentTimeMillis())
            SharedPreferencesUtil.saveData(SVTSConstants.homeCardLastOpenedID, id1)

            showImageDialog(title, url)
        }

    }

    override fun getCheckUpdateSuccess(info: UpdateApk) {
        val apkCode = info.version
        val needUpdate = info.type
        val url = info.apkDownloadURL
        val versionCode = AppUtils.getVersionName(mActivity)

        if (versionCode.toString() != apkCode) {
            LogCat.e("需要更新")
            addUpdateApkImageData(info.newFeatureImg)
            if (needUpdate == 0) {
                showUpdateDialog(url)
            } else if (needUpdate == 1) {
                showUpdateDialog(url, needUpdate)
            }
        } else {
            LogCat.e("最新")
        }
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

    private fun onRefreshAD(lastADTime: Long, lastADID: Long) {
        mPresenter.getAD(lastADTime, lastADID)
    }


    private fun addUpdateApkImageData(url: String) {
        val adInfo = AdInfo()
        adInfo.activityImg = url
        updateImageList.add(adInfo)
    }

    private fun showUpdateDialog(url: String) {

        val updateManager = AdManager(activity, updateImageList)
        updateManager.setOverScreen(true)
                .setPageTransformer(DepthPageTransformer())
                .setOnImageClickListener { view, advInfo ->
                    goToDownload(mActivity, url)
                }
                .setWidthPerHeight(0.83f)
        updateManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }

    private fun showUpdateDialog(url: String, type: Int) {

        val updateManager = AdManager(activity, updateImageList)
        updateManager.setOverScreen(true)
                .setPageTransformer(DepthPageTransformer())
                .setOnImageClickListener { view, advInfo ->
                    goToDownload(mActivity, url)
                }
                .setDialogCloseable(false)
        updateManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }

    private fun addAdImageData(url: String) {
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
                    intent.putExtra("url", url)
                    startActivity(intent)
                }
        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }

    private fun goToDownload(context: Context, downLoadUrl: String) {
        val intent = Intent(context.applicationContext, DownloadService::class.java)
        intent.putExtra("downloadUrl", downLoadUrl)
        context.startService(intent)
    }


    //此处测试弹出窗口
    private fun showWindow() {
        XPopup.get(mActivity)
                .asCustom(InviteCenterPopup(mActivity,
                        OnSelectListener { position, text ->
                            if (position == 2) {
                                LogCat.e("text is $text" )
                                toastShow("进行校验")
                            }
                        })
                ).show()
    }
}
