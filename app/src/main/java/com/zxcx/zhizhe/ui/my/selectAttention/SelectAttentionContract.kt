package com.zxcx.zhizhe.ui.my.selectAttention

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView

interface SelectAttentionContract {

	interface View : NullGetPostView<MutableList<ClassifyBean>>

	interface Presenter : INullGetPostPresenter<MutableList<ClassifyBean>>
}

