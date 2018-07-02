package com.zxcx.zhizhe.ui.article.subject

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface SubjectArticleContract {

	interface View : GetView<List<CardBean>>

	interface Presenter : IGetPresenter<List<CardBean>>
}

