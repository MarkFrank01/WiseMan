package com.zxcx.zhizhe.ui.my.selectAttention

import android.os.Parcel
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * Created by anm on 2017/8/30.
 */

class ClassifyCardBean() : RetrofitBean(), MultiItemEntity, Parcelable {

	@SerializedName("interested")
	var isChecked: Boolean = false
	@SerializedName("id")
	var id: Int = 0
	@SerializedName("titleImage")
	var imageUrl: String? = null
	@SerializedName("title")
	var name: String? = null
    @SerializedName("follow")
    var follow:Boolean = false

    constructor(parcel: Parcel) : this() {
        isChecked = parcel.readByte() != 0.toByte()
        id = parcel.readInt()
        imageUrl = parcel.readString()
        name = parcel.readString()
        follow = parcel.readByte() != 0.toByte()
    }

    override fun getItemType(): Int {
		return TYPE_CARD_BAG
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as ClassifyCardBean

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id
	}


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isChecked) 1 else 0)
        parcel.writeInt(id)
        parcel.writeString(imageUrl)
        parcel.writeString(name)
        parcel.writeByte(if (follow) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        const val TYPE_CARD_BAG = 2

        @JvmField
        val CREATOR: Parcelable.Creator<ClassifyCardBean> = object : Parcelable.Creator<ClassifyCardBean> {
            override fun createFromParcel(source: Parcel): ClassifyCardBean = ClassifyCardBean(source)
            override fun newArray(size: Int): Array<ClassifyCardBean?> = arrayOfNulls(size)
        }
    }
}
