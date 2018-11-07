package com.zxcx.zhizhe.ui.rank

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

data class UserRankBean(
		@SerializedName("avatar") var imageUrl: String?, //string
		@SerializedName("createArticleCount") var cardNum: Int = 0, //0
		@SerializedName("followerCount") var fansNum: Int = 0, //0
		@SerializedName("id") var id: Int = 0, //0
		@SerializedName("rankIndexFloat") var rankChange: Int = 0, //0
		@SerializedName("accountLevel") var level: Int = 0, //0
		@SerializedName("intelligenceValue") var intelligence: Int = 0, //0
		@SerializedName("likeArticleCount") var likeNum: Int = 0, //0
		@SerializedName("collectedArticleCount") var collectNum: Int = 0, //0
		@SerializedName("name") var name: String?, //string
		@SerializedName("rankIndex") var rankIndex: Int = 0, //0
        @SerializedName("authenticationType")var authenticationType:Int = 0
) : RetrofitBean()

