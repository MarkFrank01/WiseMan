package com.zxcx.zhizhe.ui.circle.circlehome

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleUserBean(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("avatar") val avatar: String = "",
        @SerializedName("name") val name: String = "",
        @SerializedName("sign") val signature: String = "",
        @SerializedName("accountLevel") var level: Int = 0, //0
        @SerializedName("createArticleCount") var cardNum: Int = 0, //0
        @SerializedName("followerCount") var fansNum: Int = 0, //0
        @SerializedName("likeArticleCount") var likeNum: Int = 0, //0
        @SerializedName("collectedArticleCount") var collectNum: Int = 0, //0

        @SerializedName("followType") var followType:Int = 0,
        @SerializedName("authorCreateArticleCount")var authorCreateArticleCount:Int =0,
        @SerializedName("authorFollowerCount") var authorFollowerCount:Int =0,
        @SerializedName("likedUsersCount") var likedUsersCount:Int = 0


//        @SerializedName("phoneNum")
//        val phoneNum: String = "",
//        @SerializedName("gender")
//        val gender: Int = 0,
//        @SerializedName("createTime")
//        val createTime: Long = 0,
//        @SerializedName("hasInterest")
//        val hasAttention: Boolean = false,
//        @SerializedName("bandingQQ")
//        val bandingQQ: Boolean = false,
//        @SerializedName("bandingWeixin")
//        val bandingWeixin: Boolean = false,
//        @SerializedName("bandingWeibo")
//        val bandingWeibo: Boolean = false
) : RetrofitBean(), Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()
            ) {
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(avatar)
        writeString(name)
        writeString(signature)
        writeInt(level)
        writeInt(cardNum)
        writeInt(fansNum)
        writeInt(likeNum)
        writeInt(collectNum)
        writeInt(followType)
        writeInt(authorCreateArticleCount)
        writeInt(authorFollowerCount)
        writeInt(likedUsersCount)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CircleUserBean> {
        override fun createFromParcel(parcel: Parcel): CircleUserBean {
            return CircleUserBean(parcel)
        }

        override fun newArray(size: Int): Array<CircleUserBean?> {
            return arrayOfNulls(size)
        }
    }


}