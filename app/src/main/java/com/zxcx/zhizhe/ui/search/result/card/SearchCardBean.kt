package com.zxcx.zhizhe.ui.search.result.card

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

data class SearchCardBean(
        @SerializedName("id") var id: Int?,
        @SerializedName("collectingCount") var collectNum: Int?,
        @SerializedName("pv") var readNum: Int?,
        @SerializedName("titleImage") var imageUrl: String?,
        @SerializedName("title") var name: String?,
        @SerializedName("collectionName") var cardBagName: String?,
        @SerializedName("matchContent") var content: String?,
        @SerializedName("passTime") var date: Date?,
        @SerializedName("authorName") var author: String?
) : RetrofitBaen()

