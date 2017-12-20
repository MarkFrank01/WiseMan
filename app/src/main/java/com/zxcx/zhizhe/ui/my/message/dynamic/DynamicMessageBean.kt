package com.zxcx.zhizhe.ui.my.message.dynamic

import com.zxcx.zhizhe.retrofit.RetrofitBaen
import com.alibaba.fastjson.annotation.JSONField

data class DynamicMessageBean(
		@JSONField(name = "collectedUserStr") var collectedUserStr: String?, //string
		@JSONField(name = "followerUserStr") var followerUserStr: String?, //string
		@JSONField(name = "likeUserStr") var likeUserStr: String? //string
)

