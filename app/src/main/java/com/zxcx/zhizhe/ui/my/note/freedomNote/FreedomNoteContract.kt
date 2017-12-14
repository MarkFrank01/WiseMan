package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface FreedomNoteContract {

    interface View : GetView<List<FreedomNoteBean>>

    interface Presenter : IGetPresenter<List<FreedomNoteBean>>
}

