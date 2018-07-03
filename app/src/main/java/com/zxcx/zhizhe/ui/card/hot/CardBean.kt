package com.zxcx.zhizhe.ui.card.hot

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import com.zxcx.zhizhe.ui.welcome.ADBean
import java.util.*

class CardBean(
		@SerializedName("id") var id: Int = 0,
		@SerializedName("relationshipKeyId") var realId: Int = 0,//仅限阅读使用
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
		@SerializedName("adUrl") var adUrl: String = "",
		@SerializedName("adVO") var ad: ADBean? = null
) : RetrofitBaen(), Parcelable {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as CardBean

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id ?: 0
	}

	constructor(source: Parcel) : this(
			source.readInt(),
			source.readInt(),
			source.readInt(),
			source.readInt(),
			source.readInt(),
			source.readInt(),
			source.readInt(),
			source.readString(),
			source.readString(),
			source.readSerializable() as Date?,
			source.readString(),
			source.readInt(),
			source.readInt(),
			source.readString(),
			source.readString(),
			source.readInt(),
			source.readString(),
			source.readString(),
			source.readString(),
			1 == source.readInt(),
			1 == source.readInt(),
			1 == source.readInt(),
			1 == source.readInt(),
			source.readString(),
			source.readParcelable<ADBean>(ADBean::class.java.classLoader)
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeInt(id)
		writeInt(realId)
		writeInt(cardType)
		writeInt(collectNum)
		writeInt(readNum)
		writeInt(likeNum)
		writeInt(commentNum)
		writeString(imageUrl)
		writeString(name)
		writeSerializable(date)
		writeString(authorName)
		writeInt(authorId)
		writeInt(cardBagId)
		writeString(categoryName)
		writeString(labelName)
		writeInt(labelId)
		writeString(subjectName)
		writeString(content)
		writeString(searchContent)
		writeInt((if (isLike) 1 else 0))
		writeInt((if (isUnLike) 1 else 0))
		writeInt((if (isCollect) 1 else 0))
		writeInt((if (isFollow) 1 else 0))
		writeString(adUrl)
		writeParcelable(ad, 0)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<CardBean> = object : Parcelable.Creator<CardBean> {
			override fun createFromParcel(source: Parcel): CardBean = CardBean(source)
			override fun newArray(size: Int): Array<CardBean?> = arrayOfNulls(size)
		}
	}
}

