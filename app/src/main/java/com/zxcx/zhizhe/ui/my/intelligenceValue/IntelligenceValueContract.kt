package com.zxcx.zhizhe.ui.my.intelligenceValue

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface IntelligenceValueContract {

    interface View : GetView<IntelligenceValueBean>

    interface Presenter : IGetPresenter<IntelligenceValueBean>
}

