package com.zxcx.zhizhe.ui.search.result.user

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

class SearchUserBean(
		@SerializedName("avatar") var imageUrl: String?, //string
		@SerializedName("createArticleCount") var cardNum: Int = 0, //0
		@SerializedName("followerCount") var fansNum: Int = 0, //0
		@SerializedName("id") var id: Int = 0, //0
		@SerializedName("accountLevel") var level: Int = 0, //0
		@SerializedName("intelligenceValue") var intelligence: Int = 0, //0
		@SerializedName("likeArticleCount") var likeNum: Int = 0, //0
		@SerializedName("collectedArticleCount") var collectNum: Int = 0, //0
		@SerializedName("name") var name: String?,
		@SerializedName("follow") var isFollow: Boolean = false,
        @SerializedName("authorAuthenticationType") var authorAuthenticationType:Int = 0

) : RetrofitBean() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SearchUserBean

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id
	}
}

