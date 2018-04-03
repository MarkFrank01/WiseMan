package com.zxcx.zhizhe.ui.my.creation

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.TextView
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.SaveFreedomNoteSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.creation.passed.CreationDraftsFragment
import com.zxcx.zhizhe.ui.my.creation.passed.CreationInReviewFragment
import com.zxcx.zhizhe.ui.my.creation.passed.CreationPassedFragment
import com.zxcx.zhizhe.ui.my.creation.passed.CreationRejectFragment
import com.zxcx.zhizhe.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_creation.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by anm on 2017/12/14.
 */
class CreationActivity : BaseActivity() {
    private val titles = arrayOf("已通过", "待审核", "未通过", "草稿箱")

    private val passedFragment = CreationPassedFragment()
    private val reviewFragment = CreationInReviewFragment()
    private val rejectFragment = CreationRejectFragment()
    private val draftsFragment = CreationDraftsFragment()
    private var mCurrentFragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation)
        EventBus.getDefault().register(this)
        ButterKnife.bind(this)

        initView()
        initListener()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun initListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
        /*iv_toolbar_creation.setOnClickListener {
            //进入新建卡片流程，修改标题题图页
            startActivity(Intent(mActivity, NewCreationTitleActivity::class.java))
        }*/
    }

    private fun initView() {
        for (i in titles.indices) {
            val tab = tl_creation.newTab()
            tab.setCustomView(R.layout.tab_creation)
            val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
            textView.text = titles[i]
            tl_creation.addTab(tab)
            //            tab.setText(titles[i]);
        }
        tl_creation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> switchFragment(passedFragment)
                    1 -> switchFragment(reviewFragment)
                    2 -> switchFragment(rejectFragment)
                    3 -> switchFragment(draftsFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val para = tl_creation.layoutParams
        val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
        para.width = screenWidth * 4 / 5
        tl_creation.layoutParams = para
        tl_creation.getTabAt(0)?.select()
        switchFragment(passedFragment)
    }

    private fun switchFragment(newFragment: Fragment) {

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (newFragment.isAdded) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_creation, newFragment).commitAllowingStateLoss()
        }
        mCurrentFragment = newFragment
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SaveFreedomNoteSuccessEvent) {
        finish()
    }
}