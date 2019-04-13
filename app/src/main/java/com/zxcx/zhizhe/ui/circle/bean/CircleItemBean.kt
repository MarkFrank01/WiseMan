package com.zxcx.zhizhe.ui.circle.bean

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/23
 * @Description :
 */
class CircleItemBean(

):RetrofitBean(){
    //id
    @SerializedName("id")
    var id:Int = 0

    @SerializedName("title")
    var title:String = ""

    @SerializedName("hasMore")
    var hasMore:String = ""

}