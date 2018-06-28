package com.zxcx.zhizhe.ui.my.intelligenceValue

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBaen
import java.util.*


data class IntelligenceValueBean(
		@SerializedName("missionVOList") var missionVOList: ArrayList<MissionVO?>?,
		@SerializedName("todayAchieveMissionCount") var todayAchieveMissionCount: Int?, //0
		@SerializedName("todayAllMissionCount") var todayAllMissionCount: Int?, //0
		@SerializedName("todayValue") var todayValue: Int?, //0
		@SerializedName("totalValue") var totalValue: Int? //0
) : RetrofitBaen()

data class MissionVO(
		@SerializedName("limitCount") var limitCount: Int?, //0
		@SerializedName("missionType") var missionType: Int?, //0
		@SerializedName("reachedCount") var reachedCount: Int? //0
) : RetrofitBaen()
