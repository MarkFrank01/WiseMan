package com.zxcx.zhizhe.ui.article.attention

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import com.zxcx.zhizhe.ui.card.hot.CardBean

class SubjectBean(
		@SerializedName("id") var id: Int = 0,
		@SerializedName("title") var name: String? = null,
		@SerializedName("classifyTitle") var categoryName: String? = null,
		@SerializedName("collectionTitle") var labelName: String? = null,
		@SerializedName("articleList") var cardList: List<CardBean>? = null
) : RetrofitBaen() {
}

