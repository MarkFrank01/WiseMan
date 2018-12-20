package com.zxcx.zhizhe.service.version_update.entity

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2018/12/20
 * @Description :
 */
class UpdateApk(
        @SerializedName("apkDownloadURL")var apkDownloadURL:String ="",
        @SerializedName("createTime")var createTime:String = "",
        @SerializedName("enableTime")var enableTime:String="",
        @SerializedName("id")var id:Int,
        @SerializedName("newFeature")var newFeature:String = "",
        @SerializedName("newFeatureImg")var newFeatureImg:String = "",
        @SerializedName("platformType")var platformType:Int,
        @SerializedName("updateType")var type:Int,
        @SerializedName("version")var version:String = ""
):RetrofitBean(){


}