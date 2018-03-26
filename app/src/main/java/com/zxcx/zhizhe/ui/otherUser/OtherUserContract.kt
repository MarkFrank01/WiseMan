package com.zxcx.zhizhe.ui.otherUser

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.home.hot.CardBean

interface OtherUserContract {

    interface View : GetView<OtherUserInfoBean>{
        fun getOtherUserCreationSuccess(list: MutableList<CardBean>)
    }

    interface Presenter : IGetPresenter<OtherUserInfoBean>{
        fun getOtherUserCreationSuccess(list: MutableList<CardBean>)
    }
}

