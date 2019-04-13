package com.zxcx.zhizhe.ui.card

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.service.version_update.entity.UpdateApk
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
interface HomeCardContract {

    interface View : GetView<MutableList<CardBean>> {
        fun getADSuccess(list: MutableList<ADBean>)

        fun getCheckUpdateSuccess(info: UpdateApk)
    }

    interface Presenter : IGetPresenter<MutableList<CardBean>> {
        fun getADSuccess(list: MutableList<ADBean>)

        fun getCheckUpdateApk(info:UpdateApk)
    }
}