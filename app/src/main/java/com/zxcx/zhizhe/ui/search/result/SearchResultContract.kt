package com.zxcx.zhizhe.ui.search.result

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface SearchResultContract {

    interface View : GetView<MutableList<SearchResultBean>>

    interface Presenter : IGetPresenter<MutableList<SearchResultBean>>
}

