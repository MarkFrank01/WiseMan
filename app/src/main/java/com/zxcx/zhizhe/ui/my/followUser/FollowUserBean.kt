package com.zxcx.zhizhe.ui.search.result.card

import com.alibaba.fastjson.annotation.JSONField
import com.zxcx.zhizhe.retrofit.RetrofitBaen

class FollowUserBean(
		@JSONField(name = "avatar") var imageUrl: String?, //string
		@JSONField(name = "createArticleCount") var cardNum: Int?, //0
		@JSONField(name = "followerCount") var fansNum: Int?, //0
		@JSONField(name = "id") var id: Int?, //0
		@JSONField(name = "targetUserId") var targetUserId: Int?, //0
		@JSONField(name = "intelligenceValue") var readNum: Int?, //0
		@JSONField(name = "name") var name: String?, //string
		@JSONField(name = "userAurhorRelationshipType") var followType: Int? //0为未关注，1为已关注，2为已相互关注
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

