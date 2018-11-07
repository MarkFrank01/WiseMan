package com.zxcx.zhizhe.ui.card.cardList

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import kotlinx.android.synthetic.main.fragment_card_list.*

/**
 * 首页-卡片-列表页面
 */

class CardListFragment : BaseFragment(), IGetPresenter<MutableList<CardCategoryBean>> {

	private var mAdapter: CardListViewPagerAdapter? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_card_list, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		tl_card_list.setupWithViewPager(vp_card_list)
		getCardCategory()
	}

	override fun getDataSuccess(list: MutableList<CardCategoryBean>) {
		mAdapter = fragmentManager?.let { CardListViewPagerAdapter(list, it) }
		vp_card_list.adapter = mAdapter
		tl_card_list.removeAllTabs()
		list.forEach {
			val tab = tl_card_list.newTab()
			tab.setCustomView(R.layout.tab_card_list)
			val textView = tab.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
			textView?.text = it.name
			tl_card_list.addTab(tab)
		}
	}

	override fun getDataFail(msg: String?) {
		toastFail(msg)
	}

	private fun getCardCategory() {
		mDisposable = AppClient.getAPIService().cardCategory
				.compose(BaseRxJava.handleArrayResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<MutableList<CardCategoryBean>>(this) {
					override fun onNext(t: MutableList<CardCategoryBean>) {
						getDataSuccess(t)
					}
				})
		addSubscription(mDisposable)
	}

	class CardListViewPagerAdapter(val list: MutableList<CardCategoryBean>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

		private val fragments: SparseArray<CardListItemFragment> = SparseArray(count)

		override fun getItem(position: Int): Fragment {
			return CardListItemFragment.newInstance(list[position].id)
		}

		override fun instantiateItem(container: ViewGroup, position: Int): Any {
			val fragment = super.instantiateItem(container, position) as CardListItemFragment
			fragments.put(position, fragment)
            LogCat.e("ChangeItem"+position)
            SharedPreferencesUtil.saveData(SVTSConstants.adTypePosition, position)

            return fragment
		}

		override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
			super.destroyItem(container, position, `object`)
			fragments.remove(position)
		}

		override fun getCount(): Int {
			return list.size
		}

		fun getPositionFragment(position: Int): CardListItemFragment? {
			return fragments[position]
		}

	}

	public fun onActivityReenter() {
		mAdapter?.getPositionFragment(vp_card_list.currentItem)?.onActivityReenter()
	}

	public fun getSharedView(names: MutableList<String>): MutableMap<String, View>? {
		return mAdapter?.getPositionFragment(vp_card_list.currentItem)?.getSharedView(names)
	}
}
