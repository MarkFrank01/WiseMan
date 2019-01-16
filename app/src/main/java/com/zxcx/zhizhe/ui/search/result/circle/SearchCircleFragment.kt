package com.zxcx.zhizhe.ui.search.result.circle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SearchCircleFragment :MvpFragment<SearchCirclePresenter>(),SearchCircleContract.View{

    override fun createPresenter(): SearchCirclePresenter {
        return SearchCirclePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }


}