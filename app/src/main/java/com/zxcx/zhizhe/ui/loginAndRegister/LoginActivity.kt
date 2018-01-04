package com.zxcx.zhizhe.ui.loginAndRegister

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.TextView
import butterknife.ButterKnife
import com.gyf.barlibrary.ImmersionBar
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PhoneRegisteredEvent
import com.zxcx.zhizhe.mvpBase.BackHandlerHelper
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginFragment
import com.zxcx.zhizhe.ui.loginAndRegister.register.RegisterFragment
import com.zxcx.zhizhe.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoginActivity : BaseActivity() {

    private val titles = arrayOf("登录", "注册")

    private val loginFragment = LoginFragment()
    private val registerFragment = RegisterFragment()
    private var mCurrentFragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        EventBus.getDefault().register(this)
        initView()
        iv_login_close.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initStatusBar() {
        mImmersionBar = ImmersionBar.with(this).fitsSystemWindows(true)
        mImmersionBar.init()
    }

    override fun onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: PhoneRegisteredEvent) {
        //手机号已注册，跳转登录
        tl_login.getTabAt(0)?.select()
    }

    private fun initView() {
        for (i in titles.indices) {
            val tab = tl_login.newTab()
            tab.setCustomView(R.layout.tab_home)
            val textView = tab.customView?.findViewById(R.id.tv_tab_home) as TextView
            textView.text = titles[i]
            tl_login.addTab(tab)
            //            tab.setText(titles[i]);
        }
        tl_login.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> switchFragment(loginFragment)
                    1 -> switchFragment(registerFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val para = tl_login.layoutParams
        val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
        para.width = screenWidth * 2 / 3
        tl_login.layoutParams = para
        tl_login.getTabAt(0)?.select()
        switchFragment(loginFragment)
    }

    private fun switchFragment(newFragment: Fragment) {

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (newFragment.isAdded) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_login_content, newFragment).commitAllowingStateLoss()
        }
        mCurrentFragment = newFragment
    }
}
