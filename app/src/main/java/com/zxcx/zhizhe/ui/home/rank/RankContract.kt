package com.zxcx.zhizhe.ui.home.rank

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface RankContract {

    interface View : GetView<List<UserRankBean>>{
        fun getMyRankSuccess(bean: UserRankBean)
    }

    interface Presenter : IGetPresenter<List<UserRankBean>>{
        fun getMyRankSuccess(bean: UserRankBean)
    }
}

