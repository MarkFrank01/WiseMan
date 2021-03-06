package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean

class CircleCommentBean(
//		@SerializedName("articleAuthor") var articleAuthor: Boolean = false,//文章作者
//		@SerializedName("authorAvatar") var userImageUrl: String = "",//头像
//		@SerializedName("authorId") var userId: Int = 0,
//		@SerializedName("authorName") var userName: String = "",
//		@SerializedName("childCommentVOList") var childCommentList: ArrayList<CircleChildCommentBean> = arrayListOf(),
//		@SerializedName("content") var content: String = "",
//		@SerializedName("hasLike") var hasLike: Boolean = false,
//		@SerializedName("id") var id: Int = 0,
//		@SerializedName("likeCount") var likeCount: Int = 0,
//        @SerializedName("authorAuthenticationType") var authorAuthenticationType:Int = 0,
//        @SerializedName("distanceTime") var distanceTime:String = "" ,//时间

///////////////////////////////////////////////////
        @SerializedName("authorVO") var authorVO:UserInfoBean? = null,
        @SerializedName("childCommentCount")var childCommentCount:Int = 0,
        @SerializedName("childQaCommentVOList")var childQaCommentVOList:ArrayList<CircleChildCommentBean> = arrayListOf(),
        @SerializedName("createTime")var createTime:String,
        @SerializedName("description")var description:String,
        @SerializedName("qacImageList")var qacImageList:ArrayList<String>,
        @SerializedName("statusType")var statusType:Int = 0,
        @SerializedName("id") var id: Int = 0,

        @SerializedName("likeCount") var likeCount:Int = 0,
        @SerializedName("distanceTime")var  distanceTime:String,
        @SerializedName("like") var like:Boolean


) : AbstractExpandableItem<CircleChildCommentBean>(), MultiItemEntity {

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
		childQaCommentVOList.forEach {
			addSubItem(it)
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as CircleCommentBean

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id
	}

}