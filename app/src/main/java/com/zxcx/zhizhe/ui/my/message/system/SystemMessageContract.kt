package com.zxcx.zhizhe.ui.my.message.system

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface SystemMessageContract {

	interface View : GetView<List<SystemMessageBean>> {
		fun getCardSuccess(bean: CardBean)
		fun getCardNoFound()
	}

	interface Presenter : IGetPresenter<List<SystemMessageBean>> {
		fun getCardSuccess(bean: CardBean)
		fun getCardNoFound()
	}
}

