package com.zxcx.zhizhe.ui.home.rank

import com.alibaba.fastjson.annotation.JSONField
import com.zxcx.zhizhe.retrofit.RetrofitBaen

data class UserRankBean(
        @JSONField(name = "avatar") var imageUrl: String?, //string
        @JSONField(name = "createArticleCount") var cardNum: Int?, //0
        @JSONField(name = "followerCount") var fansNum: Int?, //0
        @JSONField(name = "id") var id: Int?, //0
        @JSONField(name = "intelligenceValue") var readNum: Int?, //0
        @JSONField(name = "name") var name: String?, //string
        @JSONField(name = "rankIndex") var rankIndex: Int? //0
): RetrofitBaen()

