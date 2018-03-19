package com.zxcx.zhizhe.ui.home.rank

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.welcome.ADBean

interface RankContract {

    interface View : GetView<List<UserRankBean>>{
        fun getMyRankSuccess(bean: UserRankBean)
        fun getADSuccess(list: MutableList<ADBean>)
    }

    interface Presenter : IGetPresenter<List<UserRankBean>>{
        fun getMyRankSuccess(bean: UserRankBean)
        fun getADSuccess(list: MutableList<ADBean>)
    }
}

