package com.zxcx.zhizhe.ui.classify.subject

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.search.result.subject.SubjectBean

interface SubjectContract {

    interface View : GetView<List<SubjectBean>>

    interface Presenter : IGetPresenter<List<SubjectBean>>
}

