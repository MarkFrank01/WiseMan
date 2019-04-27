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
    @SerializedName("usersVO")var usersVO:CircleDetailItemBean,
    @SerializedName("qaImageEntityList")var qaImageEntityList:List<String>,
    @SerializedName("modifiedTime")var modifiedTime:String,
    @SerializedName("commentCount")var commentCount:Int,
    @SerializedName("likeCount")var likeCount:Int,
    @SerializedName("createTime")var createTime:String,
    @SerializedName("pv")var pv:Int,
    @SerializedName("circleFix") var circleFix:Boolean,
    @SerializedName("distanceTime") var distanceTime:String,

    var isOwner:Boolean

):RetrofitBean(),MultiItemEntity {
    override fun getItemType(): Int {
        return 1
    }
}