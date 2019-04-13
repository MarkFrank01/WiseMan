package com.zxcx.zhizhe.ui.circle.circlesearch

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.search.result.article.SearchArticleFragment
import com.zxcx.zhizhe.ui.search.result.card.SearchCardFragment
import com.zxcx.zhizhe.ui.search.result.circle.SearchCircleFragment
import com.zxcx.zhizhe.ui.search.result.label.SearchLabelFragment
import com.zxcx.zhizhe.ui.search.result.user.SearchUserFragment
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_search_result_circle.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class CircleSearchActivity : BaseActivity() {

    private val cardFragment = SearchCardFragment()
    private val articleFragment = SearchArticleFragment()
    private val userFragment = SearchUserFragment()
    private var mCurrentFragment = Fragment()

    private var labelFragment = SearchLabelFragment()
    private var circleFragment = SearchCircleFragment()

    //	private val titles = arrayOf("卡片", "深读", "用户")
    private val titles = arrayOf("卡片", "文章", "标签", "圈子", "用户")
//    private val titles = arrayOf("卡片", "文章", "标签",  "用户")

    private var keyword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result_circle)
        initData()
        initView()
    }

    private fun initData() {
        keyword = intent.getStringExtra("keyword")
        tv_search_result_search.text = keyword
        cardFragment.mKeyword = keyword
        articleFragment.mKeyword = keyword
        userFragment.mKeyword = keyword
        labelFragment.mKeyword = keyword
        circleFragment.mKeyword = keyword
    }

    private fun initView() {
        for (i in titles.indices) {
            val tab = tl_search_result.newTab()
            tab.setCustomView(R.layout.tab_home)
            val textView = tab.customView?.findViewById<TextView>(R.id.tv_tab_home)
            textView?.text = titles[i]
            tl_search_result.addTab(tab)
        }
        tl_search_result.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            //			override fun onTabSelected(tab: TabLayout.Tab) {
//				when (tab.position) {
//					0 -> {
//						switchFragment(cardFragment)
//					}
//					1 -> {
//						switchFragment(articleFragment)
//					}
//					2 -> {
//						switchFragment(userFragment)
//					}
//				}
//				val textView = tab.customView?.findViewById(R.id.tv_tab_home) as TextView
//				textView.paint.isFakeBoldText = true
//			}
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        switchFragment(cardFragment)
                    }
                    1 -> {
                        switchFragment(articleFragment)
                    }
                    2 -> {
                        switchFragment(labelFragment)
                    }
                    3 -> {
                        switchFragment(circleFragment)
                    }
                    4 -> {
                        switchFragment(userFragment)
                    }
                }
                val textView = tab.customView?.findViewById(R.id.tv_tab_home) as TextView
                textView.paint.isFakeBoldText = true
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val textView = tab.customView?.findViewById(R.id.tv_tab_home) as TextView
                textView.paint.isFakeBoldText = false
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val para = tl_search_result.layoutParams
        val screenWidth = ScreenUtils.getDisplayWidth() //屏幕宽度
//		para.width = screenWidth * 1 / 2
        para.width = screenWidth * 9 / 10
        tl_search_result.layoutParams = para

        switchFragment(cardFragment)
        tl_search_result.getTabAt(0)?.select()
        val textView = tl_search_result.getTabAt(0)?.customView?.findViewById(R.id.tv_tab_home) as TextView
        textView.paint.isFakeBoldText = true
    }

    override fun setListener() {
        super.setListener()
        iv_search_result_search.setOnClickListener {
            mActivity.startActivity(CircleSearchPreActivity::class.java) {
                it.putExtra("keyword", keyword)
            }
            finish()
        }
        tv_search_result_search.setOnClickListener {
            mActivity.startActivity(CircleSearchPreActivity::class.java) {
                it.putExtra("keyword", keyword)
            }
            finish()
        }
        tv_search_result_cancel.setOnClickListener {
            onBackPressed()
        }
        app_bar_layout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
//            iv_search_result_search.visibility = if (verticalOffset < 0) View.VISIBLE else View.GONE
        }
    }

    private fun switchFragment(newFragment: Fragment) {

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (newFragment.isAdded) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_search_result, newFragment).commitAllowingStateLoss()
        }
        mCurrentFragment = newFragment
    }
}