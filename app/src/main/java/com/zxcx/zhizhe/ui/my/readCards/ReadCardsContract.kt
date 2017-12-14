package com.zxcx.zhizhe.ui.my.readCards

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface ReadCardsContract {

    interface View : GetView<List<ReadCardsBean>>

    interface Presenter : IGetPresenter<List<ReadCardsBean>>
}

