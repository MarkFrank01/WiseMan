package com.zxcx.zhizhe.ui.article

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
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
import com.zxcx.zhizhe.ui.article.attention.AttentionArticleFragment
import com.zxcx.zhizhe.ui.card.cardList.CardCategoryBean
import com.zxcx.zhizhe.ui.search.search.HotSearchBean
import com.zxcx.zhizhe.ui.search.search.SearchActivity
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.fragment_home_article.*

class HomeArticleFragment : BaseFragment(), IGetPresenter<MutableList<CardCategoryBean>> {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_home_article, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		tl_card_list.setupWithViewPager(vp_article_list)
		getArticleCategory()
	}

	override fun onResume() {
		super.onResume()
		getSearchDefaultKeyword()
	}

	override fun setListener() {
		super.setListener()
		tv_home_article_search.setOnClickListener {
			mActivity.startActivity(SearchActivity::class.java) {}
		}
	}

	override fun getDataSuccess(list: MutableList<CardCategoryBean>) {
		vp_article_list.adapter = fragmentManager?.let { CardListViewPagerAdapter(list, it) }
		tl_card_list.removeAllTabs()
		list.forEach {
			val tab = tl_card_list.newTab()
			tab.setCustomView(R.layout.tab_card_list)
			val textView = tab.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
			textView?.text = it.name
			tl_card_list.addTab(tab)
		}
	}

	fun getSearchDefaultKeywordSuccess(bean: HotSearchBean) {
		tv_home_article_search.text = bean.conent
	}

	override fun getDataFail(msg: String?) {
		toastFail(msg)
	}

	private fun getArticleCategory() {
		mDisposable = AppClient.getAPIService().articleCategory
				.compose(BaseRxJava.handleArrayResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<MutableList<CardCategoryBean>>(this) {
					override fun onNext(t: MutableList<CardCategoryBean>) {
						getDataSuccess(t)
					}
				})
		addSubscription(mDisposable)
	}

	private fun getSearchDefaultKeyword() {
		mDisposable = AppClient.getAPIService().searchDefaultKeyword
				.compose(BaseRxJava.handleResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : BaseSubscriber<HotSearchBean>(this) {
					override fun onNext(t: HotSearchBean) {
						getSearchDefaultKeywordSuccess(t)
					}
				})
		addSubscription(mDisposable)
	}

	class CardListViewPagerAdapter(val list: MutableList<CardCategoryBean>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

		private val articleFragment = AttentionArticleFragment()

		override fun getItem(position: Int): Fragment {
			return if (list[position].id == -1) {
				articleFragment
			} else {
				ArticleListItemFragment.newInstance(list[position].id)
			}
		}

		override fun getCount(): Int {
			return list.size
		}
	}
}
