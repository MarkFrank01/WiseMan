package com.zxcx.zhizhe.ui.card.hot

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.daily.DailyBean

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyAdapter(data:MutableList<DailyBean>):
        BaseQuickAdapter<DailyBean, BaseViewHolder>(R.layout.item_daily_card,data){

    private lateinit var mItemAdapter : DailyItemAdapter

    override fun convert(helper: BaseViewHolder, item: DailyBean) {

        helper.setText(R.id.tv_daily_title,item.title)

        val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

        mItemAdapter = DailyItemAdapter(arrayListOf())

        val rv_item = helper.getView<RecyclerView>(R.id.item_daily_detail_card)
        rv_item.layoutManager = layoutManager
        rv_item.adapter = mItemAdapter
        mItemAdapter.setNewData(item.articleVOArrayList)
    }

}