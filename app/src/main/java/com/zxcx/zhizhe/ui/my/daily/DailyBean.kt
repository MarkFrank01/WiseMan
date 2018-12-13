package com.zxcx.zhizhe.ui.my.daily

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyBean(
        @SerializedName("title") var title: String = "",
        @SerializedName("articleVOArrayList") var articleVOArrayList: List<CardBean>? = null
) : RetrofitBean(){


}