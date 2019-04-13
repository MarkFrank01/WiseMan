package com.zxcx.zhizhe.ui.circle.circlehome

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/2/26
 * @Description :
 */
class CircleSixPicBean(
        @SerializedName("image") var image:String
):RetrofitBean(),MultiItemEntity {
    override fun getItemType(): Int {
        return 1
    }
}