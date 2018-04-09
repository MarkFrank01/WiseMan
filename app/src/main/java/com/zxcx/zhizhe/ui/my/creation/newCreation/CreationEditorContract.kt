package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.INullPostPresenter
import com.zxcx.zhizhe.mvpBase.NullPostView

interface CreationEditorContract {

    interface View : NullPostView{
        fun saveDraftSuccess()
    }

    interface Presenter : INullPostPresenter{
        fun saveDraftSuccess()
    }
}

