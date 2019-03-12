package com.zxcx.zhizhe.ui.circle.circleowner.ownercreatenext

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AANotingEvent
import com.zxcx.zhizhe.event.GetNextArcEvent
import com.zxcx.zhizhe.event.GetNextCardEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.activity_owner_next.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerCreateNextActivity : BaseActivity() {

    private val titles = arrayOf("卡片", "文章")
    private val mNextCardFragment = OwnerCreateNextCardFragment()
    private val mNextArticleFragment = OwnerCreateNextArticleFragment()
    private var mCurrentFragment = Fragment()

    private var classifyId = 0

    //检测卡片的数量
    private var mCardNum = 0
    //检查长文的数量
    private var mArcNum = 0

    //要提交的Card
    private var listcdCard = arrayListOf<CardBean>()

    //要提交的Arc
    private var listcdArc = arrayListOf<CardBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_next)
        EventBus.getDefault().register(this)
        //读取上个AC传递过来的值
        initData()
        initToolBar("设置阅读权限")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "提交"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))


        initView()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AANotingEvent) {

    }


    private fun initData() {
        listcdCard = intent.getParcelableArrayListExtra<CardBean>("listCard")
        listcdArc = intent.getParcelableArrayListExtra<CardBean>("listArc")
        LogCat.e("Next card" + listcdCard.size)
        LogCat.e("Next arc" + listcdArc.size)
//        EventBus.getDefault().post(GetNextCardEvent(0,listcdCard))
//        EventBus.getDefault().post(GetNextArcEvent(0,listcdArc))
    }

    private fun initView() {
        for (i in titles.indices) {
            val tab = tl_circle.newTab()
            tab.setCustomView(R.layout.tab_creation)
            val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
            textView.text = titles[i]
            tl_circle.addTab(tab)
        }
        tl_circle.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        show_first_tv.text = "在此页面选择4张卡片在圈外公开阅读"
                        switchFragment(mNextCardFragment)
                        EventBus.getDefault().post(GetNextCardEvent(0, listcdCard))
                    }
                    1 -> {
                        switchFragment(mNextArticleFragment)
                        show_first_tv.text = "在此页面选择2篇文章在圈外公开阅读"
                        EventBus.getDefault().post(GetNextArcEvent(0, listcdArc))

                    }
                }
                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
                textView.paint.isFakeBoldText = true
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
                textView.paint.isFakeBoldText = false
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
        switchFragment(mNextCardFragment)
        tl_circle.getTabAt(0)?.select()

        val textView = tl_circle.getTabAt(0)?.customView?.findViewById(R.id.tv_tab_creation) as TextView
        textView.paint.isFakeBoldText = true

    }

    private fun switchFragment(newFragment: Fragment) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (mCurrentFragment == newFragment){
            if (newFragment == mNextCardFragment){
                EventBus.getDefault().post(GetNextCardEvent(0, listcdCard))
            }else if (newFragment == mNextArticleFragment){
                EventBus.getDefault().post(GetNextArcEvent(0, listcdArc))
            }
        }else {
            if (newFragment.isAdded) {
                //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
                transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
            } else {
                transaction.hide(mCurrentFragment).add(R.id.fl_circle, newFragment).commitAllowingStateLoss()
            }
            mCurrentFragment = newFragment
        }
    }
}