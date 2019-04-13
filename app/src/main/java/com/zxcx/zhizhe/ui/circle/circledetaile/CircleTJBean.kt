package com.zxcx.zhizhe.ui.circle.circledetaile

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/8
 * @Description :
 */
class CircleTJBean(

) : RetrofitBean() {
    @SerializedName("title")
    var title:String = ""

    @SerializedName("styleType")
    val styleType:Int = 0

}