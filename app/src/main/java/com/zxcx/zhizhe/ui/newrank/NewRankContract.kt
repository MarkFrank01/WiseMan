package com.zxcx.zhizhe.ui.newrank

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
interface NewRankContract {

    interface View:GetView<List<UserRankBean>>{
        fun getMyRankSuccess(bean: UserRankBean)
        fun getTopTenRankSuccess(bean:List<UserRankBean>)
        //获取圈子顶部的广告
        fun getADSuccess(list: MutableList<ADBean>)
    }

    interface Presenter : IGetPresenter<List<UserRankBean>> {
        fun getMyRankSuccess(bean: UserRankBean)
        fun getTopTenRankSuccess(bean:List<UserRankBean>)
        //获取圈子顶部的广告
        fun getADSuccess(list: MutableList<ADBean>)
    }
}