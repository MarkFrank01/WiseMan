package com.zxcx.zhizhe.ui.my.money.bill

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import kotlinx.android.synthetic.main.activity_bill.*

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillActivity : BaseActivity() {

    private val titles = arrayOf("账单", "提现")
    private var goto: Int = 0 //0账单 1加入
    private val mBillFirstFragment = BillFristFragment()
    private val mBillSecondFragment = BillSecondFragment()
    private var mCurrentFragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        initToolBar("账单明细")

        initView()
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
                        switchFragment(mBillFirstFragment)
                    }
                    1 -> {
                        switchFragment(mBillSecondFragment)
                    }
                }
                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
//                textView.paint.isFakeBoldText = true
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
//                textView.paint.isFakeBoldText = false
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
        switchFragment(mBillFirstFragment)
        tl_circle.getTabAt(0)?.select()
        val textView = tl_circle.getTabAt(0)?.customView?.findViewById(R.id.tv_tab_creation) as TextView
//        textView.paint.isFakeBoldText = true
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