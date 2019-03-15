package com.zxcx.zhizhe.ui.circle.circleowner.owneradd.addnext

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/15
 * @Description :
 */
class BalanceBean(
        @SerializedName("articleCount") var articleCount:Int = 0,
        @SerializedName("articleMoreThanOne") var articleMoreThanOne:Boolean,
        @SerializedName("cardCount") var cardCount:Int =0,
        @SerializedName("cardMoreThanOne") var cardMoreThanOne:Boolean
):RetrofitBean(){

}