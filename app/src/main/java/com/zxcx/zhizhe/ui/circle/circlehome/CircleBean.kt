package com.zxcx.zhizhe.ui.circle.circlehome

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.zxcx.zhizhe.retrofit.RetrofitBean
import com.zxcx.zhizhe.ui.circle.bean.CircleIWaitContentBean
import com.zxcx.zhizhe.ui.circle.bean.CircleItemBean
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CircleBean(


) : RetrofitBean(), MultiItemEntity {

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
    val statusType:Int = -11

    @SerializedName("toBeAddedInfoVO")
    val toBeAddedInfoVO:CircleIWaitContentBean? =null

    //新标题
    var newTitle: String? = ""

    //唯一展示标题
    var showTitle:String = ""

    override fun getItemType(): Int {
        return CIRCLE_HOME_1
    }

    companion object {
        const val CIRCLE_HOME_0 = 0
        const val CIRCLE_HOME_1 = 2
        const val CIRCLE_HOME_2 = 2
        const val CIRCLE_HOME_3 = 3
    }

}