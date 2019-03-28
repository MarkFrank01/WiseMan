package com.zxcx.zhizhe.ui.my.message.system

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import java.util.*
//文章通过审核
const val message_card_pass = 101
//文章审核不通过
const val message_card_reject = 102
const val message_apply_pass = 103
const val message_apply_reject = 104
const val message_rank = 105
const val message_recommend = 106

const val message_link_pass = 107
const val message_link_unpass = 108

//圈子通过
const val MESSAGE_TYPE_SYSTEM_CIRCLE_PASS = 109
//圈子不通过
const val MESSAGE_TYPE_SYSTEM_CIRCLE_UNPASS = 110
//圈子被关闭
const val MESSAGE_TYPE_SYSTEM_CIRCLE_IS_CLOSED = 111
//圈子未提交
const val MESSAGE_TYPE_SYSTEM_CIRCLE_NOT_SUBMITTED = 112
//圈子编辑通过
const val MESSAGE_TYPE_SYSTEM_CIRCLE_EDITING_PASS = 113
//圈子编辑不通过
const val MESSAGE_TYPE_SYSTEM_CIRCLE_EDITING_UNPASS = 114
//圈子已提现
const val MESSAGE_TYPE_SYSTEM_CIRCLE_HAS_WITHDRAW = 115

const val message_follow = 201
const val message_like = 202
const val message_collect = 203
const val message_comment = 204


//日常推送消息
//直接唤起APP首页
const val MESSAGE_TYPE_DAILY_FOR_INDEX = 401
//直接唤起APP中的H5
const val MESSAGE_TYPE_DAILY_FOR_H5 = 402
//直接唤起APP中的文章
const val MESSAGE_TYPE_DAILY_FOR_ARTICLE = 403
//直接唤起APP中的卡片
const val MESSAGE_TYPE_DAILY_FOR_CARD = 404

data class SystemMessageBean(
        @SerializedName("content") var content: String?, //string
        @SerializedName("intelligenceValue") var intelligenceValue: Int?, //0
        @SerializedName("messageType") var messageType: Int?, //0
        @SerializedName("relatedCardId") var relatedCardId: Int?, //0
        @SerializedName("remaskContent") var remaskContent: String?, //string
        @SerializedName("time") var time: Date?, //2017-12-20T03:28:06.284Z
        @SerializedName("titleColor") var titleColor: String?, //string
        @SerializedName("title") var title: String?, //string
        @SerializedName("relatedCircleId") var relatedCircleId:Int

) : RetrofitBean(), MultiItemEntity {

    override fun getItemType(): Int {
        return this.messageType!!
    }

    companion object {
    }
}

