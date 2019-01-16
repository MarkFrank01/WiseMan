package com.zxcx.zhizhe.ui.search.result.label

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class SearchLabelBean(
        @SerializedName("title") var title: String?,
        @SerializedName("titleImage") var titleImage: String?
):RetrofitBean(){

}