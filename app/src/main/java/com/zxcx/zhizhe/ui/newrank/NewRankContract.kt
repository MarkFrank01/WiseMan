package com.zxcx.zhizhe.ui.newrank

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.rank.UserRankBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
interface NewRankContract {

    interface View:GetView<List<UserRankBean>>{
        fun getMyRankSuccess(bean: UserRankBean)
        fun getTopTenRankSuccess(bean:List<UserRankBean>)
    }

    interface Presenter : IGetPresenter<List<UserRankBean>> {
        fun getMyRankSuccess(bean: UserRankBean)
        fun getTopTenRankSuccess(bean:List<UserRankBean>)
    }
}