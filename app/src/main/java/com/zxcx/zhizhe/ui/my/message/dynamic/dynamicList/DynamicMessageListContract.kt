package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView

interface DynamicMessageListContract {

	interface View : NullGetPostView<List<DynamicMessageListBean>>

	interface Presenter : INullGetPostPresenter<List<DynamicMessageListBean>>
}

