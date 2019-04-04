package com.zxcx.zhizhe.ui.my.money.bill

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillDescAdapter(data:List<BillBean>):BaseQuickAdapter<BillBean,BaseViewHolder>(R.layout.item_bill_2) {

    override fun convert(helper: BaseViewHolder, item: BillBean) {

        helper.setText(R.id.tv_name, item.targetUser?.name)
        helper.setText(R.id.tv_desc, ZhiZheUtils.timeChange(item.createTime) + "加入")
        helper.setText(R.id.tv_money, item.amount)
    }
}