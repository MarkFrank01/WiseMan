package com.zxcx.zhizhe.ui.my.creation.passed

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

data class CreationBean(
        @SerializedName("id") var id: Int?,
        @SerializedName("articleType") var cardType: Int?,
        @SerializedName("collectingCount") var collectNum: Int?,
        @SerializedName("pv") var readNum: Int?,
        @SerializedName("collectionName") var cardBagName: String?,
        @SerializedName("titleImage") var imageUrl: String?,
        @SerializedName("title") var name: String?,
        @SerializedName("passTime") var date: Date?,
        @SerializedName("authorName") var author: String?
) : RetrofitBaen()

