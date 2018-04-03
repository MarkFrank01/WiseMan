package com.zxcx.zhizhe.ui.my.creation.passed

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface CreationContract {

    interface View : GetView<List<CreationBean>>

    interface Presenter : IGetPresenter<List<CreationBean>>
}

