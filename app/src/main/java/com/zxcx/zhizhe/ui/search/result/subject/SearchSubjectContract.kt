package com.zxcx.zhizhe.ui.search.result.subject

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface SearchSubjectContract {

    interface View : GetView<List<SubjectBean>>

    interface Presenter : IGetPresenter<List<SubjectBean>>
}

