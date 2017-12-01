package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface SearchCardContract {

    interface View : GetView<List<SearchCardBean>>

    interface Presenter : IGetPresenter<List<SearchCardBean>>
}

