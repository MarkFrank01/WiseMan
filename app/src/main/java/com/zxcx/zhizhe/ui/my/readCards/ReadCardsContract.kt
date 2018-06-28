package com.zxcx.zhizhe.ui.my.readCards

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface ReadCardsContract {

	interface View : NullGetPostView<List<ReadCardsBean>> {
		fun getEmptyRecommendCardSuccess(bean: CardBean)
	}

	interface Presenter : INullGetPostPresenter<List<ReadCardsBean>> {
		fun getEmptyRecommendCardSuccess(bean: CardBean)
	}
}

