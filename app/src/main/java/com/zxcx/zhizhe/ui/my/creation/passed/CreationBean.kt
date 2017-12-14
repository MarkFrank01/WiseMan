package com.zxcx.zhizhe.ui.search.result.card

import com.alibaba.fastjson.annotation.JSONField
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

data class CreationBean(
        @JSONField(name = "id") var id: Int?,
        @JSONField(name = "titleImage") var imageUrl: String?,
        @JSONField(name = "title") var name: String?,
        @JSONField(name = "passTime") var date: Date?,
        @JSONField(name = "authorName") var author: String?
) : RetrofitBaen()
