package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import com.alibaba.fastjson.annotation.JSONField
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

data class DynamicMessageListBean(
		@JSONField(name = "content") var content: String?, //string
		@JSONField(name = "messageType") var messageType: Int?, //0
		@JSONField(name = "relatedCardId") var relatedCardId: Int?, //0
		@JSONField(name = "relatedCardName") var relatedCardName: String?, //string
		@JSONField(name = "relatedUserAvatar") var relatedUserAvatar: String?, //string
		@JSONField(name = "relatedUserId") var relatedUserId: Int?, //0
		@JSONField(name = "relatedUserName") var relatedUserName: String?, //string
		@JSONField(name = "time") var date: Date? //2017-12-20T09:17:26.516Z

): RetrofitBaen(), MultiItemEntity {
    var time: String? = ""
    override fun getItemType(): Int {
        return dynamic_content
    }
}

