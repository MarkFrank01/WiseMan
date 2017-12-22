package com.zxcx.zhizhe.ui.my.note.freedomNote

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.my.note.cardNote.NoteBean

interface FreedomNoteContract {

    interface View : GetView<List<NoteBean>>

    interface Presenter : IGetPresenter<List<NoteBean>>
}

