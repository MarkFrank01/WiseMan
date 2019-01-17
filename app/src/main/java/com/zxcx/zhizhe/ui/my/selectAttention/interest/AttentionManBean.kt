package com.zxcx.zhizhe.ui.my.selectAttention.interest

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import java.io.Serializable

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class AttentionManBean : RetrofitBean(), MultiItemEntity, Serializable {

    override fun getItemType(): Int {
        return TYPE1
    }

    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    var name: String? = null
    @SerializedName("latestCircleId")
    var latestCircleId: String? =null
    @SerializedName("latestcircleTitle")
    var latestcircleTitle: String? =null
    @SerializedName("avatar")
    var avatar: String? = null

    companion object {

        const val TYPE1 = 2
    }
}