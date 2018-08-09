package com.zxcx.zhizhe.ui.my.selectAttention

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import java.io.Serializable

/**
 * Created by anm on 2017/8/30.
 */

class ClassifyCardBean : RetrofitBean(), MultiItemEntity, Serializable {

	@SerializedName("interested")
	var isChecked: Boolean = false
	@SerializedName("id")
	var id: Int = 0
	@SerializedName("titleImage")
	var imageUrl: String? = null
	@SerializedName("title")
	var name: String? = null

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

	companion object {

		const val TYPE_CARD_BAG = 2
	}
}
