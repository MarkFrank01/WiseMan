package com.zxcx.zhizhe.ui.my.money.bill

import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillBean {
    @SerializedName("amount")
    var amount: String = ""
    //账单类型 -2进圈扣款 -1提现到账 0申请提现 1他人进入自己的圈子 2系统奖励 ,
    @SerializedName("billType")
    var billType: Int = -10
    @SerializedName("createTime")
    var createTime: Long = 0
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("relatedCircle")
    var relatedCircle: CircleBean? = null
    @SerializedName("relatedUser")
    var relatedUser:CircleUserBean? = null
    @SerializedName("remask")
    var remask:String = ""
    @SerializedName("sourcesOfFunds")
    var sourcesOfFunds:Int = 0
    @SerializedName("targetUser")
    var targetUser:CircleUserBean? = null
}