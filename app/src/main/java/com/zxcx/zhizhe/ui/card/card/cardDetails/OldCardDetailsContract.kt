package com.zxcx.zhizhe.ui.card.card.cardDetails

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface OldCardDetailsContract {

    interface View : GetView<OldCardDetailsBean>

    interface Presenter : IGetPresenter<OldCardDetailsBean>
}

