package com.zxcx.zhizhe.ui.circle.allmycircle

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import kotlinx.android.synthetic.main.activity_all_mycircle.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class AllMyCircleActivity : BaseActivity() {
    private val titles = arrayOf("创建", "加入")
    private var goto: Int = 0 //0创建 1加入
    private val mMyCreateFragment = AllMyCreateFragment()
    private val mMyJoinFragment = AllMyJoinFragment()
    private var mCurrentFragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_mycircle)
        initToolBar("我的圈子")

        initView()
    }

    private fun initView(){
        for (i in titles.indices) {
            val tab = tl_circle.newTab()
            tab.setCustomView(R.layout.tab_creation)
            val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
            textView.text = titles[i]
            tl_circle.addTab(tab)
            //            tab.setText(titles[i]);
        }
        tl_circle.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {


            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0->{
                        switchFragment(mMyCreateFragment)
                    }
                    1->{
                        switchFragment(mMyJoinFragment)
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
        switchFragment(mMyCreateFragment)
//        goto = intent.getIntExtra("goto",0)
        tl_circle.getTabAt(goto)?.select()
        val textView = tl_circle.getTabAt(goto)?.customView?.findViewById(R.id.tv_tab_creation) as TextView
        textView.paint.isFakeBoldText = true
    }


    private fun switchFragment(newFragment:Fragment){
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