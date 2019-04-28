package com.zxcx.zhizhe.ui.circle.circleowner.owneradd

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.GetBackNumAndDataEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circleowner.owneradd.addnext.OwnerAddNextActivity
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.getColorForKotlin
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_owner_create_manage.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/3/12
 * @Description :
 */
class OwnerAddActivity : BaseActivity() {
    private val titles = arrayOf("卡片", "文章")
    private val mOwnerAddCardFragment = OwnerAddCardFragment()
    private val mOwnerAddArticleFragment = OwnerAddArticleFragment()
    private var mCurrentFragment = Fragment()

    private var circleID = 0

    //检测卡片的数量
    private var mCardNum = 0
    //检查长文的数量
    private var mArcNum = 0

    //传递到下一个AC的card
    private var listcdCard: MutableList<CardBean> = ArrayList()

    //传递到下一个AC的Arc
    private var listcdArc:MutableList<CardBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_add)
        EventBus.getDefault().register(this)
        initData()
        initToolBar("作品审核")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "下一步"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))

        initView()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //避免恢复视图状态
    }

    override fun recreate() {
        val intent = Intent()
        intent.putExtra("isNight", true)
        setIntent(intent)
        try {//避免重启太快 恢复
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            for (fragment in supportFragmentManager.fragments) {
                fragmentTransaction.remove(fragment)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
        }

        super.recreate()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: GetBackNumAndDataEvent) {
        LogCat.e("event back " + event.contentList)
        if (event.type == 0) {
            mCardNum = event.contentList.size
//            mCardList = event.contentList
            listcdCard = event.cardBeanList
        } else if (event.type == 1) {
            mArcNum = event.contentList.size
//            mArcList = event.contentList
            listcdArc = event.cardBeanList
        }

        //之后调整为8和4
        if (mCardNum >= 8 && mArcNum >= 4) {
            tv_toolbar_right.isEnabled = true
            tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))
        }else{
            tv_toolbar_right.isEnabled = false
            tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))
        }
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            startActivity(OwnerAddNextActivity::class.java){
                it.putExtra("circleId",circleID)
                it.putExtra("listCard",listcdCard as ArrayList)
                it.putExtra("listArc",listcdArc as ArrayList)
            }
        }
    }

    private fun initData(){
        circleID = intent.getIntExtra("circleId", 0)
        SharedPreferencesUtil.saveData("mcircleId",circleID)
    }

    private fun initView() {
        show_first_tv.text = "在此页面选择符合圈子分类的8张卡片"

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
                        show_first_tv.text = "在此页面选择符合圈子分类的8张卡片"
                        switchFragment(mOwnerAddCardFragment)
                    }
                    1 -> {
                        switchFragment(mOwnerAddArticleFragment)
                        show_first_tv.text = "在此页面选择符合圈子分类的4篇文章"
                    }
                }
//                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
//                textView.paint.isFakeBoldText = true
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
//                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
//                textView.paint.isFakeBoldText = false
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
        switchFragment(mOwnerAddCardFragment)
        tl_circle.getTabAt(0)?.select()
//        val textView = tl_circle.getTabAt(0)?.customView?.findViewById(R.id.tv_tab_creation) as TextView
//        textView.paint.isFakeBoldText = true
    }

    private fun switchFragment(newFragment: Fragment) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (newFragment.isAdded) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_circle, newFragment).commitAllowingStateLoss()
        }
        mCurrentFragment = newFragment
    }
}