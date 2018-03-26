package com.zxcx.zhizhe.ui.my.readCards

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView

interface ReadCardsContract {

    interface View : NullGetPostView<List<ReadCardsBean>>

    interface Presenter : INullGetPostPresenter<List<ReadCardsBean>>
}

