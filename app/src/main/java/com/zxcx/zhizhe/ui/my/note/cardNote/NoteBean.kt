package com.zxcx.zhizhe.ui.search.result.card

import com.alibaba.fastjson.annotation.JSONField
import com.zxcx.zhizhe.retrofit.RetrofitBaen

data class NoteBean(
        @JSONField(name = "id") var id: Int?,
        @JSONField(name = "title") var name: String?,
        @JSONField(name = "body") var content: String?
) : RetrofitBaen()

