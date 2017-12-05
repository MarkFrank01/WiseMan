package com.zxcx.zhizhe.ui.home.rank

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface RankContract {

    interface View : GetView<RankBean>

    interface Presenter : IGetPresenter<RankBean>
}

