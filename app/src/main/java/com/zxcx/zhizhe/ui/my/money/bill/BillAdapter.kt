package com.zxcx.zhizhe.ui.my.money.bill

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ZhiZheUtils

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillAdapter(data: List<BillBean>) : BaseQuickAdapter<BillBean, BaseViewHolder>(R.layout.item_bill_1) {

    override fun convert(helper: BaseViewHolder, item: BillBean) {
        val imageView = helper.getView<ImageView>(R.id.iv_user)
        val imageUrl = ZhiZheUtils.getHDImageUrl(item.targetUser?.avatar)
        ImageLoader.load(mContext, imageUrl, R.drawable.default_header, imageView)

        helper.setText(R.id.tv_name, item.targetUser?.name)

//        helper.setText(R.id.tv_desc, ZhiZheUtils.timeChange(item.createTime) + "加入" + item.relatedCircle?.title)
        helper.setText(R.id.tv_money, item.amount)

        when (item.billType) {
            -2 -> {
                helper.setText(R.id.tv_desc, ZhiZheUtils.timeChange(item.createTime) + "加入" + item.relatedCircle?.title)
            }

            -1 -> {
                helper.setText(R.id.tv_desc, ZhiZheUtils.timeChange(item.createTime) + "进圈扣款")
            }

            0 -> {
                helper.setText(R.id.tv_desc, ZhiZheUtils.timeChange(item.createTime) + "提现")
            }

            1 -> {
                helper.setText(R.id.tv_desc, ZhiZheUtils.timeChange(item.createTime) + "加入")
            }

            2->{
                helper.setText(R.id.tv_desc, ZhiZheUtils.timeChange(item.createTime) + "系统奖励")
            }
        }

        when (item.sourcesOfFunds) {
            0 -> {
                helper.setText(R.id.tv_type, "来源IOS内购")
            }

            1 -> {
                helper.setText(R.id.tv_type, "来源Android支付宝")
            }

            2 -> {
                helper.setText(R.id.tv_type, "来源Android微信")
            }
        }
    }

}