package com.zxcx.zhizhe.ui.home.rank.moreRank

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.home.rank.UserRankBean

interface RankContract {

    interface View : GetView<List<UserRankBean>>

    interface Presenter : IGetPresenter<List<UserRankBean>>
}

