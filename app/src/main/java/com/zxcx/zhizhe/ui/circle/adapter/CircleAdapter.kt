package com.zxcx.zhizhe.ui.circle.adapter

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleHomeOnClickListener

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CircleAdapter(data:List<CircleBean>?, private val mListener: CircleHomeOnClickListener):
        BaseMultiItemQuickAdapter<CircleBean,BaseViewHolder>(data),BaseQuickAdapter.OnItemClickListener{

    var layoutManager: LinearLayoutManager

    init {
        addItemType(CircleBean.CIRCLE_HOME_1, R.layout.item_circle_ad)
        addItemType(CircleBean.CIRCLE_HOME_2,R.layout.item_circle_list)
//        addItemType(CircleBean.CIRCLE_HOME_3,R.layout.item_circle_more)
        layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
    }

    override fun convert(helper: BaseViewHolder, item: CircleBean) {
        when(helper.itemViewType){
            CircleBean.CIRCLE_HOME_1 -> initCircleAD(helper,item)
            CircleBean.CIRCLE_HOME_2 -> initCircleList(helper,item)
        }
    }

    //初始化广告
    private fun initCircleAD(helper: BaseViewHolder,bean: CircleBean){

    }

    //初始化列表
    private fun initCircleList(helper: BaseViewHolder,bean: CircleBean){

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

}