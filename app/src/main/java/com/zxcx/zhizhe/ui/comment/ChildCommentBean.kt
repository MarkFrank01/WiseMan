package com.zxcx.zhizhe.ui.comment

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName

class ChildCommentBean(
		@SerializedName("articleAuthor") var articleAuthor: Boolean = false,//文章作者
		@SerializedName("authorAvatar") var userImageUrl: String = "",//头像
		@SerializedName("authorId") var userId: Int = 0,
		@SerializedName("authorName") var userName: String = "",
		@SerializedName("content") var content: String = "",
		@SerializedName("hasLike") var hasLike: Boolean = false,
		@SerializedName("id") var id: Int = 0,
		@SerializedName("likeCount") var likeCount: Int = 0,
        @SerializedName("authorAuthenticationType")var authorAuthenticationType:Int = 0
) : MultiItemEntity {

	companion object {
		const val TYPE_LEVEL_1 = 1
	}

	override fun getItemType(): Int {
		return TYPE_LEVEL_1
	}
}