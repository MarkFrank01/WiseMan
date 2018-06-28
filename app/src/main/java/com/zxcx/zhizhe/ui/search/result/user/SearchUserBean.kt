package com.zxcx.zhizhe.ui.search.result.user

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen

data class SearchUserBean(
		@SerializedName("avatar") var imageUrl: String?, //string
		@SerializedName("createArticleCount") var cardNum: Int?, //0
		@SerializedName("followerCount") var fansNum: Int?, //0
		@SerializedName("id") var id: Int?, //0
		@SerializedName("intelligenceValue") var readNum: Int?, //0
		@SerializedName("name") var name: String?, //string
		@SerializedName("rankIndex") var rankIndex: Int? //0
) : RetrofitBaen()

