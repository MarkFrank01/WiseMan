package com.zxcx.zhizhe.ui.my.selectAttention.interest

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean
import java.io.Serializable

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class InterestRecommendBean(
        @SerializedName("collectionList")
        var collectionList: List<ClassifyCardBean>? = null,
        @SerializedName("usersList")
        var usersList: List<AttentionManBean>? = null

) : RetrofitBean(), MultiItemEntity, Serializable {


    override fun getItemType(): Int {
        return 0
    }


}