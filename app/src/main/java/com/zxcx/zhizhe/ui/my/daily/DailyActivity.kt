package com.zxcx.zhizhe.ui.my.daily

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.card.hot.DailyAdapter
import kotlinx.android.synthetic.main.activity_daily.*

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyActivity:MvpActivity<DailyPresenter>(),DailyContract.View, BaseQuickAdapter.OnItemClickListener{

    private lateinit var mAdapter : DailyAdapter
    private var mPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)
        initToolBar("实用头条")
        initView()
    }

    override fun createPresenter(): DailyPresenter {
        return DailyPresenter(this)
    }

    override fun getDataSuccess(bean: MutableList<CardBean>) {
        mAdapter.setNewData(bean)
    }

    private fun initView(){
        mAdapter = DailyAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        rv_daily_card.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_daily_card.adapter = mAdapter

        mPresenter.getDailyList(0)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        val cardImg = view.findViewById<ImageView>(R.id.iv_item_card_icon)
        val cardTitle = view.findViewById<TextView>(R.id.tv_item_card_title)
        val cardCategory = view.findViewById<TextView>(R.id.tv_item_card_category)
        val cardLabel = view.findViewById<TextView>(R.id.tv_item_card_label)
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                Pair.create(cardImg, cardImg.transitionName),
                Pair.create(cardTitle, cardTitle.transitionName),
                Pair.create(cardCategory, cardCategory.transitionName),
                Pair.create(cardLabel, cardLabel.transitionName)).toBundle()
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("list", mAdapter.data as java.util.ArrayList)
        intent.putExtra("currentPosition", position)
        intent.putExtra("sourceName", this::class.java.name)
        mActivity.startActivity(intent, bundle)
    }



    private fun getDailyCard(){
        mPresenter.getDailyList(mPage)
    }
}