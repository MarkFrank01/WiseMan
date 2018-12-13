package com.zxcx.zhizhe.ui.my.message.system

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import java.util.*

const val message_card_pass = 101
const val message_card_reject = 102
const val message_apply_pass = 103
const val message_apply_reject = 104
const val message_rank = 105
const val message_recommend = 106

const val message_link_pass = 107
const val message_link_unpass = 108

const val message_follow = 201
const val message_like = 202
const val message_collect = 203
const val message_comment = 204

data class SystemMessageBean(
        @SerializedName("content") var content: String?, //string
        @SerializedName("intelligenceValue") var intelligenceValue: Int?, //0
        @SerializedName("messageType") var messageType: Int?, //0
        @SerializedName("relatedCardId") var relatedCardId: Int?, //0
        @SerializedName("remaskContent") var remaskContent: String?, //string
        @SerializedName("time") var time: Date?, //2017-12-20T03:28:06.284Z
        @SerializedName("titleColor") var titleColor: String?, //string
        @SerializedName("title") var title: String?, //string
        @SerializedName("remask") var url: String?
) : RetrofitBean(), MultiItemEntity {

    override fun getItemType(): Int {
        return this.messageType!!
    }

    companion object {
    }
}

