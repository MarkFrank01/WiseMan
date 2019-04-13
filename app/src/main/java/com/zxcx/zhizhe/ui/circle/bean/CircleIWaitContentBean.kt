package com.zxcx.zhizhe.ui.circle.bean

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/23
 * @Description :
 */
class CircleIWaitContentBean(

):RetrofitBean(){
    //id
    @SerializedName("articleCount")
    var articleCount:String = ""

    @SerializedName("cardCount")
    var cardCount:String = ""

    @SerializedName("deadline")
    var deadline:String = ""

}