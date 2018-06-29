package com.zxcx.zhizhe.ui.article.articleDetails

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import com.zxcx.zhizhe.ui.welcome.ADBean
import java.util.*

class ArticleDetailsBean(
		@SerializedName("id") var id: Int = 0,
		@SerializedName("type") var cardType: Int = 0, //1卡片，2长文
		@SerializedName("collectingCount") var collectNum: Int = 0,
		@SerializedName("pv") var readNum: Int = 0,
		@SerializedName("likedUsersCount") var likeNum: Int = 0,
		@SerializedName("commentCount") var commentNum: Int = 0,
		@SerializedName("titleImage") var imageUrl: String? = null,
		@SerializedName("title") var name: String? = null,
		@SerializedName("passTime") var date: Date? = null,
		@SerializedName("authorName") var authorName: String? = null,
		@SerializedName("authorId") var authorId: Int = 0,
		@SerializedName("classifyId") var cardBagId: Int = 0,
		@SerializedName("classifyTitle") var categoryName: String? = null,
		@SerializedName("collectionTitle") var labelName: String? = null,
		@SerializedName("collectionId") var labelId: Int = 0,
		@SerializedName("topicName") var subjectName: String? = null,
		@SerializedName("content") var content: String = "",
		@SerializedName("matchContent") var searchContent: String = "",
		@SerializedName("like") var isLike: Boolean = false,
		@SerializedName("disagree") var isUnLike: Boolean = false,
		@SerializedName("collect") var isCollect: Boolean = false,
		@SerializedName("follow") var isFollow: Boolean = false,
		@SerializedName("adVO") val ad: ADBean? = null
) : RetrofitBaen() {


}

