package com.zxcx.zhizhe.ui.my.message.system

import com.alibaba.fastjson.annotation.JSONField
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

const val message_card_pass = 101
const val message_card_reject = 102
const val message_apply_pass = 103
const val message_apply_reject = 104
const val message_rank = 105
const val message_recommend = 106

data class SystemMessageBean(
		@JSONField(name = "content") var content: String?, //string
		@JSONField(name = "intelligenceValue") var intelligenceValue: Int?, //0
		@JSONField(name = "messageType") var messageType: Int?, //0
		@JSONField(name = "relatedCardId") var relatedCardId: Int?, //0
		@JSONField(name = "remaskContent") var remaskContent: String?, //string
		@JSONField(name = "time") var time: Date?, //2017-12-20T03:28:06.284Z
		@JSONField(name = "titleColor") var titleColor: String?, //string
		@JSONField(name = "title") var title: String? //string
): RetrofitBaen()

