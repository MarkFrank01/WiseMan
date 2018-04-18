package com.zxcx.zhizhe.ui.home.hot

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import com.zxcx.zhizhe.ui.my.likeCards.MyCardsBean
import java.util.*

class CardBean(
        @SerializedName("id") var id: Int = 0,
        @SerializedName("articleType") var cardType: Int = 0,
        @SerializedName("collectingCount") var collectNum: Int = 0,
        @SerializedName("pv") var readNum: Int = 0,
        @SerializedName("titleImage") var imageUrl: String? = null,
        @SerializedName("title") var name: String? = null,
        @SerializedName("passTime") var date: Date? = null,
        @SerializedName("authorName") var author: String? = null,
        @SerializedName("collectionId") var cardBagId: Int = 0,
        @SerializedName("collectionName") var cardBagName: String? = null
) : RetrofitBaen() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MyCardsBean

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}

