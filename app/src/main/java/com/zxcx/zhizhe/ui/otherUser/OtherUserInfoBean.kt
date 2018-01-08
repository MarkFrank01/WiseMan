package com.zxcx.zhizhe.ui.otherUser

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen

/**
 * Created by anm on 2017/12/18.
 */

data class OtherUserInfoBean(
        @SerializedName("avatar") var imageUrl: String?, //string
        @SerializedName("hasArticle") var hasCard: Boolean?, //0
        @SerializedName("id") var id: Int?, //0
        @SerializedName("intelligenceValue") var readNum: Int?, //0
        @SerializedName("name") var name: String?, //string
        @SerializedName("sign") var signture: String? //0
): RetrofitBaen()
