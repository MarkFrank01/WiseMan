package com.zxcx.zhizhe.ui.search.result.card

import com.alibaba.fastjson.annotation.JSONField
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

data class FreedomNoteBean(
        @JSONField(name = "id") var id: Int?,
        @JSONField(name = "title") var name: String?,
        @JSONField(name = "content") var content: String?,
        @JSONField(name = "passTime") var date: Date?
) : RetrofitBaen()

