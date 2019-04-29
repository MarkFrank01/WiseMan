package com.zxcx.zhizhe.ui.circle.circledetaile

import com.google.gson.annotations.SerializedName

/**
 * @author : MarkFrank01
 * @Created on 2019/2/22
 * @Description :
 */
class CircleDetailItemBean(
        @SerializedName("avatar")var avater :String,
        @SerializedName("name")var name:String,
        @SerializedName("id") var id: Int = 0
)