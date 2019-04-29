package com.zxcx.zhizhe.ui.circle.circlehome

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.bean.CircleIWaitContentBean
import com.zxcx.zhizhe.ui.circle.bean.CircleItemBean
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
@SuppressLint("ParcelCreator")
class CircleBean(


) : RetrofitBean(), MultiItemEntity,Parcelable {

    //标记
    @Deprecated("暂时不用")
    var mCircleType: Int = 0

    //广告
    @Deprecated("暂时不用")
    var adList: MutableList<ADBean> = ArrayList()

    //分类类别
    @Deprecated("暂时不用")
    var classifyList: MutableList<ClassifyBean> = ArrayList()

    //id
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("title")
    var title: String = ""

    @SerializedName("hasJoin")
    var hasJoin: Boolean = false

    @SerializedName("sign")
    var sign: String = ""

    @SerializedName("titleImage")
    var titleImage: String = ""

    @SerializedName("classifytitle")
    var classifytitle: String = ""

    @SerializedName("classifyId")
    var classifyId:Int = 0

    @SerializedName("createrName")
    var createrName: String = ""

    @SerializedName("joinUserCount")
    var joinUserCount: Int = 0

    @SerializedName("qaCount")
    var qaCount: Int = 0

    @SerializedName("likeCount")
    var likeCount: Int = 0

    @SerializedName("price")
    var price: String = ""

    @SerializedName("classifyVO")
    val classifyVO: CircleItemBean? = null

    @SerializedName("memberList")
    val memberList:MutableList<CircleUserBean> = ArrayList()

    @SerializedName("creater")
    val creater:CircleUserBean? = null

    @SerializedName("statusType")
    var statusType:Int = -11

    @SerializedName("toBeAddedInfoVO")
    val toBeAddedInfoVO:CircleIWaitContentBean? =null

    @SerializedName("articleCount")
    var articleCount:Int = 0

    @SerializedName("overallRating")
    var overallRating:Int = 0

    //新标题
    var newTitle: String? = ""

    //唯一展示标题
    var showTitle:String = ""

    //推荐的文章
    var partialArticleList:MutableList<CardBean> = ArrayList()

    //
    @SerializedName("owner")
    var owner:Boolean = false

    //限免的类型
    @SerializedName("limitedTimeType")
    var limitedTimeType:Int = 0

    @SerializedName("circleActiveDistanceTime")
    var circleActiveDistanceTime:String = ""

    @SerializedName("modifiedTime")
    var modifiedTime:Long = 0

    @SerializedName("unpassReason")
    var unpassReason:String = ""

    @SerializedName("memberExpirationTime")
    var memberExpirationTime:String = ""

    //圈子消失的时间，待提交的时间
    @SerializedName("circleExpiredDistanceTime")
    var circleExpiredDistanceTime:String = ""

    //会员到期的时间
    @SerializedName("endServiceDistanceTime")
    var endServiceDistanceTime:String = ""

    var ItemTP = 2

    constructor(parcel: Parcel) : this() {
        mCircleType = parcel.readInt()
        id = parcel.readInt()
        title = parcel.readString()
        hasJoin = parcel.readByte() != 0.toByte()
        sign = parcel.readString()
        titleImage = parcel.readString()
        classifytitle = parcel.readString()
        classifyId = parcel.readInt()
        createrName = parcel.readString()
        joinUserCount = parcel.readInt()
        qaCount = parcel.readInt()
        likeCount = parcel.readInt()
        price = parcel.readString()
        statusType = parcel.readInt()
        articleCount = parcel.readInt()
        overallRating = parcel.readInt()
        newTitle = parcel.readString()
        showTitle = parcel.readString()
        owner = parcel.readByte() != 0.toByte()
        limitedTimeType = parcel.readInt()
        circleActiveDistanceTime = parcel.readString()
        modifiedTime = parcel.readLong()
        unpassReason = parcel.readString()
        memberExpirationTime = parcel.readString()
        circleExpiredDistanceTime = parcel.readString()
        endServiceDistanceTime = parcel.readString()
        ItemTP = parcel.readInt()
    }

    override fun getItemType(): Int {
        return ItemTP
    }

    companion object {
        const val CIRCLE_HOME_0 = 0
        const val CIRCLE_HOME_1 = 2
        const val CIRCLE_HOME_2 = 2
        const val CIRCLE_HOME_3 = 3
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mCircleType)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeByte(if (hasJoin) 1 else 0)
        parcel.writeString(sign)
        parcel.writeString(titleImage)
        parcel.writeString(classifytitle)
        parcel.writeInt(classifyId)
        parcel.writeString(createrName)
        parcel.writeInt(joinUserCount)
        parcel.writeInt(qaCount)
        parcel.writeInt(likeCount)
        parcel.writeString(price)
        parcel.writeInt(statusType)
        parcel.writeInt(articleCount)
        parcel.writeInt(overallRating)
        parcel.writeString(newTitle)
        parcel.writeString(showTitle)
        parcel.writeByte(if (owner) 1 else 0)
        parcel.writeInt(limitedTimeType)
        parcel.writeString(circleActiveDistanceTime)
        parcel.writeLong(modifiedTime)
        parcel.writeString(unpassReason)
        parcel.writeString(memberExpirationTime)
        parcel.writeString(circleExpiredDistanceTime)
        parcel.writeString(endServiceDistanceTime)
        parcel.writeInt(ItemTP)
    }

    override fun describeContents() = 0

}