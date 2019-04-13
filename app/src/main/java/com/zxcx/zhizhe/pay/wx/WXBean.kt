package com.zxcx.zhizhe.pay.wx

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/25
 * @Description :
 */
class WXBean (
        @SerializedName("appid") var appid:String,
        @SerializedName("partnerid") var partnerid:String,
        @SerializedName("packageType") var packageType:String,
        @SerializedName("nonceStr")var nonceStr:String,
        @SerializedName("timestamp")var timestamp:String,
        @SerializedName("prepayId") var prepayId :String,
        @SerializedName("sign") var sign:String

):RetrofitBean()