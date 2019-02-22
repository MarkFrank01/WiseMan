package com.zxcx.zhizhe.ui.circle.circledetaile

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/2/22
 * @Description :
 */
class CircleDetailBean (
    @SerializedName("id") var id: Int = 0,
    @SerializedName("title")var title:String,
    @SerializedName("description")var description:String,
    @SerializedName("usersVO")var usersVO:CircleDetailItemBean

):RetrofitBean(),MultiItemEntity {
    override fun getItemType(): Int {
        return 1
    }
}