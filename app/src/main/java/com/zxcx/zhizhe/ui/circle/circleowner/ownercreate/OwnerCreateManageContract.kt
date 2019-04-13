package com.zxcx.zhizhe.ui.circle.circleowner.ownercreate

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerCreateManageContract {

    interface View: GetView<MutableList<CardBean>>

    interface Presenter: IGetPresenter<MutableList<CardBean>>
}