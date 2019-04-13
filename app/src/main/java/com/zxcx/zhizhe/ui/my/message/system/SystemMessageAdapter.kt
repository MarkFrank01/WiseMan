package com.zxcx.zhizhe.ui.my.message.system

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.StringUtils
import com.zxcx.zhizhe.utils.TextViewUtils
import com.zxcx.zhizhe.utils.getColorForKotlin
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by anm on 2017/12/20.
 */
class SystemMessageAdapter(data: List<SystemMessageBean>) : BaseQuickAdapter<SystemMessageBean, BaseViewHolder>(R.layout.item_system_message, data) {
//class SystemMessageAdapter(data: List<SystemMessageBean>) : BaseMultiItemQuickAdapter<SystemMessageBean, BaseViewHolder>(data) {

    val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA)
    var pattern = "(《.*?》)"
    var r = Pattern.compile(pattern)

    //预留将会多类型item
//    init {
//        addItemType(SystemMessageBean)
//        R.layout.item_system_message
//    }

    override fun convert(helper: BaseViewHolder, item: SystemMessageBean) {
        helper.addOnClickListener(R.id.tv_item_system_message_action)
        helper.setText(R.id.tv_item_system_message_title, item.title)
        helper.setText(R.id.tv_item_system_message_time, format.format(item.time))
        setCardContent(helper, item)

        when (item.messageType) {
            message_card_pass -> {
                helper.setText(R.id.tv_item_system_message_value,
                        mContext.getString(R.string.tv_item_message_value, item.intelligenceValue))
                helper.setGone(R.id.tv_item_system_message_value, true)
                helper.setText(R.id.tv_item_system_message_action, "查看作品")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }
            message_card_reject -> {
                helper.setGone(R.id.tv_item_system_message_value, false)
                helper.setText(R.id.tv_item_system_message_action, "查看详情")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_3))
            }
            message_apply_pass -> {
                helper.setGone(R.id.tv_item_system_message_value, false)
                helper.setText(R.id.tv_item_system_message_action, "马上创作")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }
            message_apply_reject -> {
                helper.setGone(R.id.tv_item_system_message_value, false)
                helper.setText(R.id.tv_item_system_message_action, "申请创作")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }
            message_rank -> {
                helper.setText(R.id.tv_item_system_message_value,
                        mContext.getString(R.string.tv_item_message_value, item.intelligenceValue))
                helper.setGone(R.id.tv_item_system_message_value, true)
//                helper.setText(R.id.tv_item_system_message_action, "查看榜单")
                helper.setText(R.id.tv_item_system_message_action, "查看智力值")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
                setRankContent(helper, item)
            }
            message_recommend -> {
                helper.setText(R.id.tv_item_system_message_value,
                        mContext.getString(R.string.tv_item_message_value, item.intelligenceValue))
                helper.setGone(R.id.tv_item_system_message_value, true)
                helper.setText(R.id.tv_item_system_message_action, "查看详情")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }
            message_link_pass ->{
//                helper.setGone(R.id.tv_item_system_message_value,true)
                helper.getView<TextView>(R.id.tv_item_system_message_value).visibility = View.GONE
                helper.setText(R.id.tv_item_system_message_action, "查看作品")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))

            }
            message_link_unpass->{
//                helper.setGone(R.id.tv_item_system_message_value,true)
                helper.getView<TextView>(R.id.tv_item_system_message_value).visibility = View.GONE
                helper.setText(R.id.tv_item_system_message_action, "查看详情")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
//                helper.getView<TextView>(R.id.tv_item_system_message_content).setOnClickListener {
//                    val intent = Intent()
//                    intent.putExtra("title", "智者创作协议")
//                    intent.putExtra("url", mContext.getString(R.string.base_url) + mContext.getString(R.string.creation_agreement_dark_url))
//                    mContext.startActivity(intent)
//                }
                helper.addOnClickListener(R.id.tv_item_system_message_content)
            }

            //新
            MESSAGE_TYPE_SYSTEM_CIRCLE_PASS->{
                helper.getView<TextView>(R.id.tv_item_system_message_value).visibility = View.GONE
                helper.setText(R.id.tv_item_system_message_action, "查看圈子")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }

            MESSAGE_TYPE_SYSTEM_CIRCLE_UNPASS->{
                helper.getView<TextView>(R.id.tv_item_system_message_value).visibility = View.GONE
                helper.setText(R.id.tv_item_system_message_action, "查看详情")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }

            MESSAGE_TYPE_SYSTEM_CIRCLE_IS_CLOSED->{
                helper.getView<TextView>(R.id.tv_item_system_message_value).visibility = View.GONE
                helper.setText(R.id.tv_item_system_message_action, "查看详情")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }

            MESSAGE_TYPE_SYSTEM_CIRCLE_NOT_SUBMITTED->{
                helper.getView<TextView>(R.id.tv_item_system_message_value).visibility = View.GONE
                helper.setText(R.id.tv_item_system_message_action, "查看详情")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }

            MESSAGE_TYPE_SYSTEM_CIRCLE_EDITING_PASS->{
                helper.getView<TextView>(R.id.tv_item_system_message_value).visibility = View.GONE
                helper.setText(R.id.tv_item_system_message_action, "查看详情")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }

            MESSAGE_TYPE_SYSTEM_CIRCLE_EDITING_UNPASS->{
                helper.getView<TextView>(R.id.tv_item_system_message_value).visibility = View.GONE
                helper.setText(R.id.tv_item_system_message_action, "查看详情")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }

            MESSAGE_TYPE_SYSTEM_CIRCLE_HAS_WITHDRAW->{
                helper.getView<TextView>(R.id.tv_item_system_message_value).visibility = View.GONE
                helper.setText(R.id.tv_item_system_message_action, "查看详情")
                helper.setTextColor(R.id.tv_item_system_message_title, mContext.getColorForKotlin(R.color.text_color_1))
            }
        }
    }

    private fun setCardContent(helper: BaseViewHolder, item: SystemMessageBean) {
        val tv = helper.getView<TextView>(R.id.tv_item_system_message_content)
        val m = r.matcher(item.content)
        if (m.find()) {
            val cardName = m.group(1)
            if (!StringUtils.isEmpty(cardName)) {
                TextViewUtils.setTextViewColorBlue(tv, cardName, item.content)
            }
        } else {
            helper.setText(R.id.tv_item_system_message_content, item.content)
        }
    }

    private fun setRankContent(helper: BaseViewHolder, item: SystemMessageBean) {
        val startIndex = item.content?.indexOf("第")
        val endIndex = item.content?.indexOf("名")
        if (startIndex != null && endIndex != null) {
            val rank = item.content?.substring(startIndex, endIndex + 1)
            val tv = helper.getView<TextView>(R.id.tv_item_system_message_content)
            TextViewUtils.setTextViewColorBlue(tv, rank, item.content)
        }
    }

}