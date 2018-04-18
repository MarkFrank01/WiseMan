package com.zxcx.zhizhe.ui.my.readCards

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

class ReadCardsBean(
        @SerializedName("id") var id: Int = 0,
        @SerializedName("relationshipKeyId") var realId: Int = 0,
        @SerializedName("articleType") var cardType: Int = 0,
        @SerializedName("collectingCount") var collectNum: Int = 0,
        @SerializedName("pv") var readNum: Int = 0,
        @SerializedName("titleImage") var imageUrl: String = "",
        @SerializedName("title") var name: String = "",
        @SerializedName("passTime") var date: Date = Date(),
        @SerializedName("authorName") var author: String = "",
        @SerializedName("collectionId") var cardBagId: Int = 0,
        @SerializedName("collectionName") var cardBagName: String = ""
) : RetrofitBaen() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReadCardsBean

        if (realId != other.realId) return false

        return true
    }

    override fun hashCode(): Int {
        return realId
    }
}

