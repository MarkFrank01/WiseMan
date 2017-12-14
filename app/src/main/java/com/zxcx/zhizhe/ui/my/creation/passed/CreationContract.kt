package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface CreationContract {

    interface View : GetView<List<CreationBean>>

    interface Presenter : IGetPresenter<List<CreationBean>>
}

