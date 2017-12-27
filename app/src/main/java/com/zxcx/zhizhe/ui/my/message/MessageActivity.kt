package com.zxcx.zhizhe.ui.my.message

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.my.RedPointBean
import com.zxcx.zhizhe.ui.my.message.dynamic.DynamicMessageFragment
import com.zxcx.zhizhe.ui.my.message.system.SystemMessageFragment
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.toolbar.*

class MessageActivity : BaseActivity() , IGetPresenter<RedPointBean> {
    private val titles = arrayOf("系统", "动态")

    private val systemMessageFragment = SystemMessageFragment()
    private val dynamicMessageFragment = DynamicMessageFragment()
    private var mCurrentFragment = Fragment()

    private var hasDynamicMessage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        ButterKnife.bind(this)
        initToolBar("消息")
        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        getRedPointStatus()
    }

    private fun initListener() {
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        for (i in titles.indices) {
            val tab = tl_message.newTab()
            tab.setCustomView(R.layout.tab_message)
            val textView = tab.customView?.findViewById(R.id.tv_tab_message) as TextView
            textView.text = titles[i]
            tl_message.addTab(tab)
            //            tab.setText(titles[i]);
        }
        tl_message.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

        val para = tl_message.layoutParams
        val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
        para.width = screenWidth * 2 / 3
        tl_message.layoutParams = para
        tl_message.getTabAt(0)?.select()
        switchFragment(systemMessageFragment)

        hasDynamicMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasDynamicMessage, false)
        refreshRedPoint()
    }

    private fun switchFragment(newFragment: Fragment) {

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (newFragment.isAdded) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_message, newFragment).commitAllowingStateLoss()
        }
        mCurrentFragment = newFragment
    }

    private fun getRedPointStatus() {
        mDisposable = AppClient.getAPIService().redPointStatus
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<RedPointBean>(this) {
                    override fun onNext(bean: RedPointBean) {
                        getDataSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    override fun getDataSuccess(bean: RedPointBean) {
        hasDynamicMessage = bean.hasDynamicMessage
        SharedPreferencesUtil.saveData(SVTSConstants.hasDynamicMessage, hasDynamicMessage)
        refreshRedPoint()
    }

    override fun getDataFail(msg: String) {
    }

    private fun refreshRedPoint() {
        val tab = tl_message.getTabAt(1)?.customView
        val redPoint = tab?.findViewById<ImageView>(R.id.iv_tab_red_point)
        redPoint?.visibility = if (hasDynamicMessage) View.VISIBLE else View.GONE
    }
}
