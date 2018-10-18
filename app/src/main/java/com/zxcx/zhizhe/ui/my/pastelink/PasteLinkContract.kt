package com.zxcx.zhizhe.ui.my.pastelink

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView

interface PasteLinkContract {
    interface View : NullGetPostView<List<PastLinkBean>>

    interface Presenter : INullGetPostPresenter<List<PastLinkBean>>
}