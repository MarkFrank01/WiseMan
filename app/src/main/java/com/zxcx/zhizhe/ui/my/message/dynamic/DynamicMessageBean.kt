package com.zxcx.zhizhe.ui.my.message.dynamic

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

data class DynamicMessageBean(
		@SerializedName("collectedUserStr") var collectedUserStr: String?, //string
		@SerializedName("commentUserStr") var commentUserStr: String?, //string
		@SerializedName("followerUserStr") var followerUserStr: String?, //string
		@SerializedName("likeUserStr") var likeUserStr: String? //string
) : RetrofitBean()

