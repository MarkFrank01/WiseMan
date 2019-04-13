package com.zxcx.zhizhe.ui.card.cardDetails

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface CardDetailsContract {

	interface View : GetPostView<CardBean, CardBean> {
		fun followSuccess(bean: CardBean)
	}

	interface Presenter : IGetPostPresenter<CardBean, CardBean> {
		fun followSuccess(bean: CardBean)
        fun deleteSuccess()
	}
}

