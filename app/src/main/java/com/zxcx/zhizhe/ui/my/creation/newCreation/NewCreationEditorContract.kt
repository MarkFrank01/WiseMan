package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.INullPostPresenter
import com.zxcx.zhizhe.mvpBase.NullPostView

interface NewCreationEditorContract {

    interface View : NullPostView{
        fun saveFreedomNoteSuccess()
    }

    interface Presenter : INullPostPresenter{
        fun saveFreedomNoteSuccess()
    }
}

