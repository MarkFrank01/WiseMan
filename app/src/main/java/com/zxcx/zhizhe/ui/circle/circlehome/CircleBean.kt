package com.zxcx.zhizhe.ui.circle.circlehome

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.retrofit.RetrofitBean
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
    var mCircleType:Int = 0

    //广告
     var adList: MutableList<ADBean> = ArrayList()

    //分类类别
     var classifyList: MutableList<ClassifyBean> = ArrayList()

    override fun getItemType(): Int {
        return CIRCLE_HOME_1
    }

    companion object {
        const val CIRCLE_HOME_1 = 1
        const val CIRCLE_HOME_2 = 2
        const val CIRCLE_HOME_3 = 3
    }

}