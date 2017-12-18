package com.zxcx.zhizhe.ui.otherUser

import com.alibaba.fastjson.annotation.JSONField
import com.zxcx.zhizhe.retrofit.RetrofitBaen

/**
 * Created by anm on 2017/12/18.
 */

data class OtherUserInfoBean(
        @JSONField(name = "avatar") var imageUrl: String?, //string
        @JSONField(name = "hasArticle") var hasCard: Boolean?, //0
        @JSONField(name = "id") var id: Int?, //0
        @JSONField(name = "intelligenceValue") var readNum: Int?, //0
        @JSONField(name = "name") var name: String?, //string
        @JSONField(name = "sign") var signture: String? //0
): RetrofitBaen()
