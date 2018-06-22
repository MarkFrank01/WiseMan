package com.zxcx.zhizhe.ui.card.cardList

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
import kotlinx.android.synthetic.main.fragment_card_list.*
import kotlinx.android.synthetic.main.fragment_home.*

class CardListFragment : BaseFragment() , IGetPresenter<MutableList<CardCategoryBean>>{


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
        getCardCategory()
    }

    override fun getDataSuccess(list: MutableList<CardCategoryBean>) {
        tl_card_list.removeAllTabs()
        list.forEach {
            val tab = tl_home.newTab()
            tab.setCustomView(R.layout.tab_card_list)
            val textView = tab.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
            textView?.text = it.name
            tl_home.addTab(tab)
        }
        vp_card_list.adapter = fragmentManager?.let { CardListViewPagerAdapter(list, it) }
        tl_card_list.setupWithViewPager(vp_card_list)
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
        override fun getItem(position: Int): Fragment {
            return CardListItemFragment.newInstance(list[position].id)
        }

        override fun getCount(): Int {
            return list.size
        }
    }
}
