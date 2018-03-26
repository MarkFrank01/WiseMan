package com.zxcx.zhizhe.ui.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.GotoHomeRankEvent
import com.zxcx.zhizhe.event.HomeClickRefreshEvent
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.ui.home.attention.AttentionFragment
import com.zxcx.zhizhe.ui.home.hot.HotFragment
import com.zxcx.zhizhe.ui.home.rank.RankFragment
import com.zxcx.zhizhe.utils.ScreenUtils
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : BaseFragment() {

    private val mHotFragment = HotFragment()
    private val mAttentionFragment = AttentionFragment()
    private val mRankFragment = RankFragment()
    private var mCurrentFragment = Fragment()

    private var llRankOffset: Float = 0f

    private val titles = arrayOf("推荐", "关注")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
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
                    0 -> switchFragment(mHotFragment)
                    1 -> switchFragment(mAttentionFragment)
                    2 -> switchFragment(mRankFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val para = tl_home.layoutParams
        val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
        para.width = screenWidth * 2 / 3
        tl_home.layoutParams = para

        tl_home.getTabAt(0)?.select()
        switchFragment(mHotFragment)

        initAppbarLayout()
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
    fun onMessageEvent(event: GotoHomeRankEvent) {
        tl_home.getTabAt(2)?.select()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HomeClickRefreshEvent) {
        app_bar_layout.setExpanded(true)
    }
}
