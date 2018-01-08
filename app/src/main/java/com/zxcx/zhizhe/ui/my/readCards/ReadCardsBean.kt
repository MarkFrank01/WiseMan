package com.zxcx.zhizhe.ui.my.readCards

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

class ReadCardsBean(
        @SerializedName("id") var id: Int?,
        @SerializedName("titleImage") var imageUrl: String?,
        @SerializedName("title") var name: String?,
        @SerializedName("passTime") var date: Date?,
        @SerializedName("authorName") var author: String?
) : RetrofitBaen()

