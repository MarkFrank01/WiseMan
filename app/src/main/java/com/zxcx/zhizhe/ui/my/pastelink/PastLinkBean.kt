package com.zxcx.zhizhe.ui.my.pastelink

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

data class PastLinkBean(
        @SerializedName("id") var id:Int = 0,
        @SerializedName("link") var link: String?
):RetrofitBean(){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PastLinkBean

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}