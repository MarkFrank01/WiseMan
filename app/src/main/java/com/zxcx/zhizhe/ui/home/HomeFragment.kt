package com.zxcx.zhizhe.ui.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.HomeClickRefreshEvent
import com.zxcx.zhizhe.event.LogoutEvent
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.ui.home.attention.AttentionFragment
import com.zxcx.zhizhe.ui.home.hot.HotFragment
import com.zxcx.zhizhe.ui.home.rank.RankActivity
import com.zxcx.zhizhe.ui.my.creation.CreationAgreementDialog
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.ui.search.search.SearchActivity
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : BaseFragment() {

    private val mHotFragment = HotFragment()
    private val mAttentionFragment = AttentionFragment()
    private var mCurrentFragment = Fragment()

    private var llRankOffset: Float = 0f

    private val titles = arrayOf("推荐", "关注")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
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
            //            tab.setText(titles[i]);
        }
        tl_home.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        switchFragment(mHotFragment)
                        tv_home_type.text = "推荐"
                    }
                    1 -> {
                        switchFragment(mAttentionFragment)
                        tv_home_type.text = "关注"
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val para = tl_home.layoutParams
        val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
        para.width = screenWidth * 1 / 2
        tl_home.layoutParams = para

        tl_home.getTabAt(0)?.select()
        switchFragment(mHotFragment)

        initAppbarLayout()

        initView()
    }

    private fun initView() {
        tv_home_creation.setOnClickListener {
            if (checkLogin()) {
                when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
                    writer_status_writer -> {
                        //创作界面
                        mActivity.startActivity(CreationEditorActivity::class.java,{})
                    }
                    else -> {
                        val dialog = CreationAgreementDialog()
                        dialog.show(mActivity.fragmentManager, "")
                    }
                }
            }
        }
        tv_home_rank.setOnClickListener {
            mActivity.startActivity(RankActivity::class.java,{})
        }
        view_home_search.setOnClickListener {
            mActivity.startActivity(SearchActivity::class.java,{})
        }
        iv_home_search.setOnClickListener {
            mActivity.startActivity(SearchActivity::class.java,{})
        }
        iv_attention_add.setOnClickListener {
            mActivity.startActivity(SelectAttentionActivity::class.java,{})
        }
    }

    private fun initAppbarLayout() {
        app_bar_layout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (llRankOffset == 0f){
                llRankOffset = ll_home_rank.top.toFloat()
            }
            val total = appBarLayout.totalScrollRange * 1.0f
            //计算出滑动百分比
            val p = Math.abs(verticalOffset) / total
            tv_home_title.alpha = 1 - p
            view_home_search.alpha = 1 - p
            ll_home_rank.alpha = 1 - p
            tv_home_type.alpha = 1 - p

            if (Math.abs(verticalOffset) > llRankOffset){
                ll_home_rank.alpha = 0f
                ll_home_tab.visibility = View.VISIBLE
                ll_home_tab.alpha = if (p == 1f) p else p/2
                if (mCurrentFragment == mAttentionFragment){
                    iv_attention_add.visibility = View.VISIBLE
                }else{
                    iv_attention_add.visibility = View.GONE
                }
            }else{
                ll_home_tab.visibility = View.INVISIBLE
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
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HomeClickRefreshEvent) {
        app_bar_layout.setExpanded(true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LogoutEvent) {
        app_bar_layout.setExpanded(true)
    }
}
