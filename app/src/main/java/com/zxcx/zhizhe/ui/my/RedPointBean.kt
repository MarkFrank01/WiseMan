package com.zxcx.zhizhe.ui.my

import com.alibaba.fastjson.annotation.JSONField

/**
 * Created by anm on 2017/12/21.
 */

//写手类型 0普通用户 1审核不通过 2审核中用户 3写手用户
const val writer_status_user = 0
const val writer_status_reject = 1
const val writer_status_review = 2
const val writer_status_writer = 3

data class RedPointBean(
		@JSONField(name = "hasDynamicMessage") var hasDynamicMessage: Boolean?, //true
		@JSONField(name = "hasSystemMessage") var hasSystemMessage: Boolean?, //true
		@JSONField(name = "writerStatus") var writerStatus: Int? //0
)
