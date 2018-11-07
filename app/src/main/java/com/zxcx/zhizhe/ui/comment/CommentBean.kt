package com.zxcx.zhizhe.ui.comment

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName

class CommentBean(
		@SerializedName("articleAuthor") var articleAuthor: Boolean = false,//文章作者
		@SerializedName("authorAvatar") var userImageUrl: String = "",//头像
		@SerializedName("authorId") var userId: Int = 0,
		@SerializedName("authorName") var userName: String = "",
		@SerializedName("childCommentVOList") var childCommentList: ArrayList<ChildCommentBean> = arrayListOf(),
		@SerializedName("content") var content: String = "",
		@SerializedName("hasLike") var hasLike: Boolean = false,
		@SerializedName("id") var id: Int = 0,
		@SerializedName("likeCount") var likeCount: Int = 0,
        @SerializedName("authorAuthenticationType") var authorAuthenticationType:Int = 0
) : AbstractExpandableItem<ChildCommentBean>(), MultiItemEntity {

	companion object {
		const val TYPE_LEVEL_0 = 0
	}

	override fun getLevel(): Int {
		return 0
	}

	override fun getItemType(): Int {
		return TYPE_LEVEL_0
	}

	public fun addAll() {
		childCommentList.forEach {
			addSubItem(it)
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as CommentBean

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id
	}

	fun toChildCommentBean(): ChildCommentBean {
		var bean = ChildCommentBean()
		bean.articleAuthor = articleAuthor
		bean.userImageUrl = userImageUrl
		bean.userId = userId
		bean.userName = userName
		bean.content = content
		bean.hasLike = hasLike
		bean.id = id
		bean.likeCount = likeCount
        bean.authorAuthenticationType = authorAuthenticationType
		return bean
	}
}