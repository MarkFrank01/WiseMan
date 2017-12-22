package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.my.note.cardNote.NoteBean

interface CardNoteContract {

    interface View : GetView<List<NoteBean>>

    interface Presenter : IGetPresenter<List<NoteBean>>
}

