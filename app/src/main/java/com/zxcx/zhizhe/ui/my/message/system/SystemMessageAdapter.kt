package com.zxcx.zhizhe.ui.my.message.system

import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.StringUtils
import com.zxcx.zhizhe.utils.TextViewUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by anm on 2017/12/20.
 */
class SystemMessageAdapter(data : List<SystemMessageBean>) : BaseQuickAdapter<SystemMessageBean, BaseViewHolder>(R.layout.item_system_message,data){
    val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA)
    var pattern = "(《.*?》)"
    var r = Pattern.compile(pattern)

    override fun convert(helper: BaseViewHolder, item: SystemMessageBean) {
        helper.addOnClickListener(R.id.tv_item_system_message_action)
        helper.setText(R.id.tv_item_system_message_title,item.title)
        helper.setText(R.id.tv_item_system_message_time,format.format(item.time))
        setCardContent(helper, item)
        helper.setGone(R.id.tv_item_system_message_reason,item.messageType == message_card_reject)
        helper.setGone(R.id.tv_item_system_message_reason_hint,item.messageType == message_card_reject)

        when(item.messageType){
            message_card_pass -> {
                helper.setText(R.id.tv_item_system_message_value,
                        mContext.getString(R.string.tv_item_message_value,item.intelligenceValue))
                helper.setGone(R.id.tv_item_system_message_value,true)
                helper.setText(R.id.tv_item_system_message_action,"查看卡片")
                helper.setTextColor(R.id.tv_item_system_message_title,ContextCompat.getColor(mContext,R.color.button_blue))
            }
            message_card_reject -> {
                helper.setText(R.id.tv_item_system_message_reason,item.remaskContent)
                helper.setGone(R.id.tv_item_system_message_value,false)
                helper.setText(R.id.tv_item_system_message_action,"查看卡片")
                helper.setTextColor(R.id.tv_item_system_message_title,ContextCompat.getColor(mContext,R.color.text_color_2))
            }
            message_apply_pass -> {
                helper.setGone(R.id.tv_item_system_message_value,false)
                helper.setText(R.id.tv_item_system_message_action,"马上创作")
                helper.setTextColor(R.id.tv_item_system_message_title,ContextCompat.getColor(mContext,R.color.button_blue))
            }
            message_apply_reject -> {
                helper.setGone(R.id.tv_item_system_message_value,false)
                helper.setText(R.id.tv_item_system_message_action,"申请创作")
                helper.setTextColor(R.id.tv_item_system_message_title,ContextCompat.getColor(mContext,R.color.button_blue))
            }
            message_rank -> {
                helper.setText(R.id.tv_item_system_message_value,
                        mContext.getString(R.string.tv_item_message_value,item.intelligenceValue))
                helper.setGone(R.id.tv_item_system_message_value,true)
                helper.setText(R.id.tv_item_system_message_action,"查看榜单")
                helper.setTextColor(R.id.tv_item_system_message_title,ContextCompat.getColor(mContext,R.color.button_blue))
                setRankContent(helper, item)
            }
            message_recommend -> {
                helper.setText(R.id.tv_item_system_message_value,
                        mContext.getString(R.string.tv_item_message_value,item.intelligenceValue))
                helper.setGone(R.id.tv_item_system_message_value,true)
                helper.setText(R.id.tv_item_system_message_action,"查看卡片")
                helper.setTextColor(R.id.tv_item_system_message_title,ContextCompat.getColor(mContext,R.color.button_blue))
            }
        }
    }

    private fun setCardContent(helper: BaseViewHolder, item: SystemMessageBean) {
        val tv = helper.getView<TextView>(R.id.tv_item_system_message_content)
        val m = r.matcher(item.content)
        if (m.find()) {
            val cardName = m.group(1)
            if (!StringUtils.isEmpty(cardName)) {
                TextViewUtils.setTextViewColorAndBold(tv, cardName, item.content)
            }
        }else {
            helper.setText(R.id.tv_item_system_message_content, item.content)
        }
    }

    private fun setRankContent(helper: BaseViewHolder, item: SystemMessageBean) {
        val startIndex = item.content?.indexOf("第")
        if (startIndex != null) {
            val rank = item.content?.substring(startIndex + 1, startIndex + 2)
            val tv = helper.getView<TextView>(R.id.tv_item_system_message_content)
            TextViewUtils.setTextViewColorAndBold(tv, rank, item.content)
        }
    }

}