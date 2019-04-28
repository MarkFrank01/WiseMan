package com.zxcx.zhizhe.ui.circle.circleowner.owneradd.addnext

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.GetBackNumAndDataEvent2
import com.zxcx.zhizhe.event.GetNextArcEvent
import com.zxcx.zhizhe.event.GetNextCardEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.allmycircle.AllMyCircleActivity
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.getColorForKotlin
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_owner_next.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/3/15
 * @Description :
 */
class OwnerAddNextActivity : MvpActivity<OwnerAddNextPresenter>(), OwnerAddNextContract.View {

    private val titles = arrayOf("卡片", "文章")
    private val mNextCardFragment = OwnerAddNextCardFragment()
    private val mNextArcFragment = OwnerAddNextArcFragment()
    private var mCurrentFragment = Fragment()

    //圈子的ID
    private var circleId: Int = 0

    //检测卡片的数量
    private var mCardNum = 0
    //检查长文的数量
    private var mArcNum = 0

    //收集回传的锁定的Card
    private var mCardList: MutableList<Int> = ArrayList()

    //收集回传的锁定Arc
    private var mArcList: MutableList<Int> = ArrayList()

    //要提交的Card
    private var listcdCard = arrayListOf<CardBean>()

    //要提交的Arc
    private var listcdArc = arrayListOf<CardBean>()

    //将未锁定的聚合
    private var mAllUnLock: MutableList<Int> = ArrayList()

    //将锁定的聚合
    private var mAllLock: MutableList<Int> = ArrayList()

    ///////////////////////
    //检查接口回来的数据

    ///////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_add_next)
        EventBus.getDefault().register(this)
        initData()
        initToolBar("设置阅读权限")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "提交"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))

        initView()
        mPresenter.checkCircleArticleBalance(circleId)
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
    fun onMessageEvent(event: GetBackNumAndDataEvent2) {
        if (event.type == 0) {
            mCardNum = event.contentList.size
            mCardList = event.contentList
        } else if (event.type == 1) {
            mArcNum = event.contentList.size
            mArcList = event.contentList
        }

        //之后调整为8和4
        if (mCardNum >= 4 && mArcNum >= 2) {
            tv_toolbar_right.isEnabled = true
            tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))
        } else {
            tv_toolbar_right.isEnabled = false
            tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))
        }
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            mPresenter.setCircleArticle(circleId, mAllUnLock, mAllLock)
        }
    }

    override fun createPresenter(): OwnerAddNextPresenter {
        return OwnerAddNextPresenter(this)
    }

    override fun setArcSuccess(bean: MutableList<CardBean>) {
        toastShow("提交成功，等待审核")
        startActivity(AllMyCircleActivity::class.java) {}
        finish()
    }

    override fun checkCircleArticleBalanceSuccess(bean: BalanceBean) {
        LogCat.e("Balance card: " + bean.cardCount + " Arc" + bean.articleCount)
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

    private fun initData() {
        circleId = intent.getIntExtra("circleId", 0)

        listcdCard = intent.getParcelableArrayListExtra<CardBean>("listCard")
        listcdArc = intent.getParcelableArrayListExtra<CardBean>("listArc")
    }

    private fun initView() {

        tl_circle_next.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        show_first_tv.text = "在此页面选择" + listcdCard.size / 2  + "张卡片在圈外公开阅读"
                        switchFragment(mNextCardFragment)
                        EventBus.getDefault().post(GetNextCardEvent(0, listcdCard))
                    }
                    1 -> {
                        switchFragment(mNextArcFragment)
                        show_first_tv.text = "在此页面选择"+listcdArc.size/2+"篇文章在圈外公开阅读"
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



        for (i in titles.indices) {
            val tab = tl_circle_next.newTab()
            tab.setCustomView(R.layout.tab_creation)
            val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
            textView.text = titles[i]
            tl_circle_next.addTab(tab)
        }

        switchFragment(mNextCardFragment)
        tl_circle_next.getTabAt(0)?.select()
        EventBus.getDefault().post(GetNextCardEvent(0, listcdCard))


        val textView = tl_circle_next.getTabAt(0)?.customView?.findViewById(R.id.tv_tab_creation) as TextView
        textView.paint.isFakeBoldText = true

    }

    private fun switchFragment(newFragment: Fragment) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (mCurrentFragment == newFragment) {
            if (newFragment == mNextCardFragment) {
                EventBus.getDefault().post(GetNextCardEvent(0, listcdCard))
            } else if (newFragment == mNextArcFragment) {
                EventBus.getDefault().post(GetNextArcEvent(0, listcdArc))
            }
        } else {
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