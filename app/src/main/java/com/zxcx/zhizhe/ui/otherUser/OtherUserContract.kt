package com.zxcx.zhizhe.ui.otherUser

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.card.hot.CardBean

interface OtherUserContract {

	interface View : NullGetPostView<OtherUserInfoBean> {
		fun getOtherUserCreationSuccess(list: MutableList<CardBean>)
	}

	interface Presenter : INullGetPostPresenter<OtherUserInfoBean> {
		fun getOtherUserCreationSuccess(list: MutableList<CardBean>)
	}
}

