package com.zxcx.zhizhe.ui.my.note.newNote

import com.zxcx.zhizhe.mvpBase.INullPostPresenter
import com.zxcx.zhizhe.mvpBase.NullPostView

interface NoteEditorContract {

    interface View : NullPostView{
        fun saveFreedomNoteSuccess()
    }

    interface Presenter : INullPostPresenter{
        fun saveFreedomNoteSuccess()
    }
}

