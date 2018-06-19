package com.zxcx.zhizhe.ui.classify.subject

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface SubjectCardContract {

    interface View : GetView<List<CardBean>>

    interface Presenter : IGetPresenter<List<CardBean>>
}

