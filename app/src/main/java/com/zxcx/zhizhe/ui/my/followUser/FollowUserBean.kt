package com.zxcx.zhizhe.ui.search.result.card

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen

class FollowUserBean(
        @SerializedName("avatar") var imageUrl: String?, //string
        @SerializedName("createArticleCount") var cardNum: Int?, //0
        @SerializedName("followerCount") var fansNum: Int?, //0
        @SerializedName("id") var id: Int?, //0
        @SerializedName("targetUserId") var targetUserId: Int?, //0
        @SerializedName("intelligenceValue") var readNum: Int?, //0
        @SerializedName("name") var name: String?, //string
        @SerializedName("userAurhorRelationshipType") var followType: Int? //0为未关注，1为已关注，2为已相互关注
):RetrofitBaen(){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FollowUserBean

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}

