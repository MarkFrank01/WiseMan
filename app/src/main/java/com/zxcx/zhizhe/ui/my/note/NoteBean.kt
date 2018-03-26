package com.zxcx.zhizhe.ui.my.note

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*

data class NoteBean(
        @SerializedName("id") var id: Int = 0,
        @SerializedName("noteType") var noteType: Int = 0,
        @SerializedName("title") var name: String?,
        @SerializedName("modifyTime") var date: Date = Date(),
        @SerializedName("body") var content: String?
) : RetrofitBaen(){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteBean

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}

