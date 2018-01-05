package com.zxcx.zhizhe.ui.my.message.system

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface SystemMessageContract {

    interface View : GetView<List<SystemMessageBean>>{
        fun getCardSuccess(cardId: Int)
        fun getCardNoFound()
    }

    interface Presenter : IGetPresenter<List<SystemMessageBean>>{
        fun getCardSuccess(cardId: Int)
        fun getCardNoFound()
    }
}

