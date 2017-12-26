package com.zxcx.zhizhe.ui.search.result.card

import android.widget.ProgressBar
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.intelligenceValue.MissionVO
import com.zxcx.zhizhe.utils.Constants

/**
 * Created by anm on 2017/12/1.
 */
class IntelligenceValueAdapter(data : List<MissionVO>) : BaseQuickAdapter<MissionVO, BaseViewHolder>(R.layout.item_intelligence_value,data){

    override fun convert(helper: BaseViewHolder, item: MissionVO) {
        val tv = helper.getView<TextView>(R.id.tv_item_intelligence_value_title)
        var textPaint = tv.paint
        textPaint.isFakeBoldText = true
        helper.setText(R.id.tv_item_intelligence_value_progress, mContext.getString(
                R.string.tv_item_intelligence_value_progress,item.reachedCount,item.limitCount))
        val progress = helper.getView<ProgressBar>(R.id.pb_item_intelligence_value)
        progress.progress = item.reachedCount!!*100/ item.limitCount!!
        when(item.missionType){
            Constants.MISSION_REGISTER -> {
                helper.setText(R.id.tv_item_intelligence_value_title,"注册智者")
                helper.setText(R.id.tv_item_intelligence_value_value,"奖励20智力值")
                helper.setVisible(R.id.tv_item_intelligence_value_content,false)
            }
            Constants.MISSION_READ -> {
                helper.setText(R.id.tv_item_intelligence_value_title,"注册智者")
                helper.setText(R.id.tv_item_intelligence_value_value,"奖励20智力值")
                helper.setVisible(R.id.tv_item_intelligence_value_content,true)
                helper.setText(R.id.tv_item_intelligence_value_content,"单张卡片阅读≥30s")
            }
            Constants.MISSION_CREATION -> {
                helper.setText(R.id.tv_item_intelligence_value_title,"注册智者")
                helper.setText(R.id.tv_item_intelligence_value_value,"奖励20智力值")
                helper.setVisible(R.id.tv_item_intelligence_value_content,true)
                helper.setText(R.id.tv_item_intelligence_value_content,"由审核通过评分决定")
            }
            Constants.MISSION_HAS_LIKE -> {
                helper.setText(R.id.tv_item_intelligence_value_title,"注册智者")
                helper.setText(R.id.tv_item_intelligence_value_value,"奖励20智力值")
                helper.setVisible(R.id.tv_item_intelligence_value_content,false)
            }
            Constants.MISSION_HAS_COLLECT -> {
                helper.setText(R.id.tv_item_intelligence_value_title,"注册智者")
                helper.setText(R.id.tv_item_intelligence_value_value,"奖励20智力值")
                helper.setVisible(R.id.tv_item_intelligence_value_content,false)
            }
            Constants.MISSION_HAS_FOLLOW -> {
                helper.setText(R.id.tv_item_intelligence_value_title,"注册智者")
                helper.setText(R.id.tv_item_intelligence_value_value,"奖励20智力值")
                helper.setVisible(R.id.tv_item_intelligence_value_content,false)
            }
            Constants.MISSION_HAS_HOT -> {
                helper.setText(R.id.tv_item_intelligence_value_title,"注册智者")
                helper.setText(R.id.tv_item_intelligence_value_value,"奖励20智力值")
                helper.setVisible(R.id.tv_item_intelligence_value_content,false)
            }
            Constants.MISSION_HAS_RANK -> {
                helper.setText(R.id.tv_item_intelligence_value_title,"注册智者")
                helper.setText(R.id.tv_item_intelligence_value_value,"奖励20智力值")
                helper.setVisible(R.id.tv_item_intelligence_value_content,true)
                helper.setText(R.id.tv_item_intelligence_value_content,"榜单前10名用户")
            }
        }

    }

}