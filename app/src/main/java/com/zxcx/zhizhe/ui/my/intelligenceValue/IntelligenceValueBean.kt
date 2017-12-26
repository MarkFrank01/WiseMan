package com.zxcx.zhizhe.ui.my.intelligenceValue

import com.alibaba.fastjson.annotation.JSONField
import com.zxcx.zhizhe.retrofit.RetrofitBaen


data class IntelligenceValueBean(
        @JSONField(name = "missionVOList") var missionVOList: List<MissionVO?>?,
        @JSONField(name = "todayAchieveMissionCount") var todayAchieveMissionCount: Int?, //0
        @JSONField(name = "todayAllMissionCount") var todayAllMissionCount: Int?, //0
        @JSONField(name = "todayValue") var todayValue: Int?, //0
        @JSONField(name = "totalValue") var totalValue: Int? //0
):RetrofitBaen()

data class MissionVO(
		@JSONField(name = "limitCount") var limitCount: Int?, //0
		@JSONField(name = "missionType") var missionType: Int?, //0
		@JSONField(name = "reachedCount") var reachedCount: Int? //0
):RetrofitBaen()
