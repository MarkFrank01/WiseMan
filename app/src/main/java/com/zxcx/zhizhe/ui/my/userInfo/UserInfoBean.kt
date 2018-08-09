package com.zxcx.zhizhe.ui.my.userInfo

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

class UserInfoBean(
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
		@SerializedName("bandingQQ")
		val bandingQQ: Boolean = false,
		@SerializedName("bandingWeixin")
		val bandingWeixin: Boolean = false,
		@SerializedName("bandingWeibo")
		val bandingWeibo: Boolean = false) : RetrofitBean() {

	/**
	 * id : 8 birth : 2017-03-25 avatar : default name : zxst_82698 gender : 1 createTime :
	 * 1508142980000 bandingQQ : true bandingWeixin : false bandingWeibo : false
	 */


}

