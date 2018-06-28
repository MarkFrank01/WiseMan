package com.zxcx.zhizhe.ui.my.likeCards

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

class MyCardsBean(
		@SerializedName("id") var id: Int = 0,
		@SerializedName("type") var cardType: Int = 0,
		@SerializedName("collectingCount") var collectNum: Int = 0,
		@SerializedName("pv") var readNum: Int = 0,
		@SerializedName("titleImage") var imageUrl: String = "",
		@SerializedName("title") var name: String = "",
		@SerializedName("passTime") var date: Date = Date(),
		@SerializedName("authorName") var author: String = "",
		@SerializedName("collectionName") var cardBagName: String = "",
		@SerializedName("topicName") var subjectName: String? = null
) : RetrofitBaen() {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as MyCardsBean

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id
	}
}

