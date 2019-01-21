package com.zxcx.zhizhe.ui.circle.circlehome

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CircleBean(
        //id
        @SerializedName("id")var id:Int = 0,

        //广告
        @SerializedName("adVO")var ad:ADBean? = null
):RetrofitBean(),MultiItemEntity{

    override fun getItemType(): Int {
        return CIRCLE_HOME_1
    }

    companion object {
        const val CIRCLE_HOME_1 = 1
        const val CIRCLE_HOME_2 = 2
        const val CIRCLE_HOME_3 = 3
    }

}