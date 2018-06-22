package com.zxcx.zhizhe.ui.card.cardList

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen

class CardCategoryBean(
        @SerializedName("id") var id: Int = 0,
        @SerializedName("title") var name: String = ""
) : RetrofitBaen() {
}