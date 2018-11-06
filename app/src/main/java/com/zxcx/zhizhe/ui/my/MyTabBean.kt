package com.zxcx.zhizhe.ui.my

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * Created by anm on 2017/12/21.
 */

//写手类型 0普通用户 1审核不通过 2审核中用户 3写手用户
const val writer_status_user = 0
const val writer_status_reject = 1
const val writer_status_review = 2
const val writer_status_writer = 3

data class MyTabBean(
		@SerializedName("creationCount") var cardCreationCount: Int,
		@SerializedName("viewArticleCount") var cardViewCount: Int,
		@SerializedName("hasDynamicMessage") var hasDynamicMessage: Boolean,
		@SerializedName("hasSystemMessage") var hasSystemMessage: Boolean,
		@SerializedName("accountLevel") var level: Int,
		@SerializedName("followerCount") var fansCount: Int,
		@SerializedName("totalIntelligenceValue") var totalIntelligenceValue: Int,
		@SerializedName("writerStatus") var writerStatus: Int,
        @SerializedName("authenticationType") var actionType: Int//0
) : RetrofitBean()