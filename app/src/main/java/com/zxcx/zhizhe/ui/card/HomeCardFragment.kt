package com.zxcx.zhizhe.ui.card

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.ui.card.attention.AttentionCardFragment
import com.zxcx.zhizhe.ui.card.cardList.CardListFragment
import com.zxcx.zhizhe.ui.card.hot.HotCardFragment
import com.zxcx.zhizhe.ui.search.search.SearchActivity
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.fragment_home_card.*

class HomeCardFragment : BaseFragment() {

	private val mHotFragment = HotCardFragment()
	private val mAttentionFragment = AttentionCardFragment()
	private val mListFragment = CardListFragment()
	private var mCurrentFragment = Fragment()

	private val titles = arrayOf("关注", "推荐", "列表")

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_home_card, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		for (i in titles.indices) {
			val tab = tl_home.newTab()
			tab.setCustomView(R.layout.tab_home)
			val textView = tab.customView?.findViewById<TextView>(R.id.tv_tab_home)
			textView?.text = titles[i]
			tl_home.addTab(tab)
		}
		tl_home.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
			override fun onTabSelected(tab: TabLayout.Tab) {
				when (tab.position) {
					0 -> {
						switchFragment(mAttentionFragment)
					}
					1 -> {
						switchFragment(mHotFragment)
					}
					2 -> {
						switchFragment(mListFragment)
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

		val para = tl_home.layoutParams
		val screenWidth = ScreenUtils.getDisplayWidth() //屏幕宽度
		para.width = screenWidth * 1 / 2
		tl_home.layoutParams = para

		tl_home.getTabAt(1)?.select()
	}

	override fun setListener() {
		super.setListener()
		iv_home_search.setOnClickListener {
			mActivity.startActivity(SearchActivity::class.java, {})
		}
	}

	private fun switchFragment(newFragment: Fragment) {

		val fm = childFragmentManager
		val transaction = fm.beginTransaction()

		if (newFragment.isAdded) {
			//.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
			transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
		} else {
			transaction.hide(mCurrentFragment).add(R.id.fl_home, newFragment).commitAllowingStateLoss()
		}
		mCurrentFragment = newFragment
	}

	override fun onDestroyView() {
		super.onDestroyView()
	}

	public fun onActivityReenter() {
		mHotFragment.onActivityReenter()
	}
}
