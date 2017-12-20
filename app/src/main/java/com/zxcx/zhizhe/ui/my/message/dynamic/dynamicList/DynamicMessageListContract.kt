package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface DynamicMessageListContract {

    interface View : GetView<List<DynamicMessageListBean>>

    interface Presenter : IGetPresenter<List<DynamicMessageListBean>>
}

