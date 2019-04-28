package com.zxcx.zhizhe.ui.my.invite

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
data class InviteBean(
        @SerializedName("aiId") var aiId:Int,
        @SerializedName("avatar") var avatar: String,
        @SerializedName("hasReceiveReward") var hasReceiveReward: Boolean,
        @SerializedName("invitedTime") var invitedTime: Long,
        @SerializedName("name") var name: String,
        @SerializedName("userId") var userId:Int,

        @SerializedName("alreadyInviteesTotal") var alreadyInviteesTotal:Int,
        @SerializedName("invitationCode") var invitationCode:String,
        @SerializedName("inviteesTotal") var inviteesTotal:Int,
        @SerializedName("uniqueCode") var uniqueCode:String,

        @SerializedName("hasLimitPeople") var hasLimitPeople:Int,//0无线 1有限
        @SerializedName("invitationType") var invitationType:Int, //0常规 1活动
        @SerializedName("description") var description:String

) : RetrofitBean()