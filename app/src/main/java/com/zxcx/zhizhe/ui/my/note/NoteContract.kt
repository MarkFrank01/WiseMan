package com.zxcx.zhizhe.ui.my.note

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView

interface NoteContract {

	interface View : NullGetPostView<List<NoteBean>>

	interface Presenter : INullGetPostPresenter<List<NoteBean>>
}

