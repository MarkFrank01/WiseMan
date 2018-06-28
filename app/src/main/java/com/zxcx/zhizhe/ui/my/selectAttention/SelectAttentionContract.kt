package com.zxcx.zhizhe.ui.my.selectAttention

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.classify.ClassifyBean

interface SelectAttentionContract {

	interface View : NullGetPostView<MutableList<ClassifyBean>>

	interface Presenter : INullGetPostPresenter<MutableList<ClassifyBean>>
}

