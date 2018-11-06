package com.zxcx.zhizhe.ui.otherUser

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * Created by anm on 2017/12/18.
 */

data class OtherUserInfoBean(
        @SerializedName("avatar") var imageUrl: String?, //string
        @SerializedName("hasArticle") var hasCard: Boolean?, //0
        @SerializedName("id") var id: Int?, //0
        @SerializedName("intelligenceValue") var intelligence: Int?, //0
        @SerializedName("authorCreateArticleCount") var creationNum: Int?, //0
        @SerializedName("authorFollowerCount") var fansNum: Int?, //0
        @SerializedName("name") var name: String?, //string
        @SerializedName("accountLevel") var level: Int,
        @SerializedName("follow") var isFollow: Boolean = false,
        @SerializedName("sign") var signature: String?,
        @SerializedName("authenticationType") var actionType: Int?//0
) : RetrofitBean()
