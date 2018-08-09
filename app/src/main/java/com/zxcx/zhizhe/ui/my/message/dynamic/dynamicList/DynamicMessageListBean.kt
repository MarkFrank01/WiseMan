package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import java.util.*

data class DynamicMessageListBean(
		@SerializedName("content") var content: String = "", //string
		@SerializedName("messageType") var messageType: Int = 0, // 消息类型 201关注 202点赞 203收藏 204评论
		@SerializedName("relatedCardId") var relatedCardId: Int = 0, //0
		@SerializedName("relatedArticleType") var relatedCardType: Int = 0, //文章类型 1卡片 2长文
		@SerializedName("relatedCardName") var relatedCardName: String = "", //string
		@SerializedName("relatedUserAvatar") var relatedUserAvatar: String = "", //string
		@SerializedName("relatedUserId") var relatedUserId: Int = 0, //0
		@SerializedName("relatedUserName") var relatedUserName: String = "", //string
		@SerializedName("time") var date: Date? //2017-12-20T09:17:26.516Z

) : RetrofitBean(), MultiItemEntity {
	var newTime: String? = ""
	override fun getItemType(): Int {
		return dynamic_content
	}
}

