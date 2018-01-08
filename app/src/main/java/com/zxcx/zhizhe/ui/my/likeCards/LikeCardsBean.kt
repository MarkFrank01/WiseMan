package com.zxcx.zhizhe.ui.my.likeCards

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

class LikeCardsBean(
        @SerializedName("id") var id: Int?,
        @SerializedName("titleImage") var imageUrl: String?,
        @SerializedName("title") var name: String?,
        @SerializedName("passTime") var date: Date?,
        @SerializedName("authorName") var author: String?
) : RetrofitBaen(){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LikeCardsBean

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}

