package com.zxcx.zhizhe.ui.my.note.cardNote

import com.alibaba.fastjson.annotation.JSONField
import com.zxcx.zhizhe.retrofit.RetrofitBaen

data class NoteBean(
        @JSONField(name = "id") var id: Int?,
        @JSONField(name = "title") var name: String?,
        @JSONField(name = "body") var content: String?
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

