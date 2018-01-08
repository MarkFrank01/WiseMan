package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import com.google.gson.annotations.SerializedName
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

data class DynamicMessageListBean(
		@SerializedName("content") var content: String?, //string
		@SerializedName("messageType") var messageType: Int?, //0
		@SerializedName("relatedCardId") var relatedCardId: Int?, //0
		@SerializedName("relatedCardName") var relatedCardName: String?, //string
		@SerializedName("relatedUserAvatar") var relatedUserAvatar: String?, //string
		@SerializedName("relatedUserId") var relatedUserId: Int?, //0
		@SerializedName("relatedUserName") var relatedUserName: String?, //string
		@SerializedName("time") var date: Date? //2017-12-20T09:17:26.516Z

): RetrofitBaen(), MultiItemEntity {
    var time: String? = ""
    override fun getItemType(): Int {
        return dynamic_content
    }
}

