package com.zxcx.zhizhe.ui.circle.circlehome

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleUserBean(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("birth")
        val birth: String = "",
        @SerializedName("avatar")
        val avatar: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("sign")
        val signature: String = "",
        @SerializedName("phoneNum")
        val phoneNum: String = "",
        @SerializedName("gender")
        val gender: Int = 0,
        @SerializedName("createTime")
        val createTime: Long = 0,
        @SerializedName("hasInterest")
        val hasAttention: Boolean = false,
        @SerializedName("bandingQQ")
        val bandingQQ: Boolean = false,
        @SerializedName("bandingWeixin")
        val bandingWeixin: Boolean = false,
        @SerializedName("bandingWeibo")
        val bandingWeibo: Boolean = false) : RetrofitBean() {
}