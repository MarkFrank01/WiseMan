package com.zxcx.zhizhe.ui.my.message

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.TextView
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.message.dynamic.DynamicMessageFragment
import com.zxcx.zhizhe.ui.my.message.system.SystemMessageFragment
import com.zxcx.zhizhe.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_follow_user.*
import kotlinx.android.synthetic.main.toolbar.*

class MessageActivity : BaseActivity() {
    private val titles = arrayOf("系统", "动态")

    private val systemMessageFragment = SystemMessageFragment()
    private val dynamicMessageFragment = DynamicMessageFragment()
    private var mCurrentFragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_user)
        ButterKnife.bind(this)
        initToolBar("消息")
        initView()
        initListener()
    }

    private fun initListener() {
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        for (i in titles.indices) {
            val tab = tl_follow_user.newTab()
            tab.setCustomView(R.layout.tab_note)
            val textView = tab.customView?.findViewById(R.id.tv_tab_note) as TextView
            textView.text = titles[i]
            tl_follow_user.addTab(tab)
            //            tab.setText(titles[i]);
        }
        tl_follow_user.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> switchFragment(systemMessageFragment)
                    1 -> switchFragment(dynamicMessageFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val para = tl_follow_user.layoutParams
        val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
        para.width = screenWidth * 2 / 3
        tl_follow_user.layoutParams = para
        tl_follow_user.getTabAt(0)?.select()
        switchFragment(systemMessageFragment)
    }

    private fun switchFragment(newFragment: Fragment) {

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (newFragment.isAdded) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_follow_user, newFragment).commitAllowingStateLoss()
        }
        mCurrentFragment = newFragment
    }
}
