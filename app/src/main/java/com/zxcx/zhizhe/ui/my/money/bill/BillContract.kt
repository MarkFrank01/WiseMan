package com.zxcx.zhizhe.ui.my.money.bill

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class BillContract {
    interface View:GetView<MutableList<BillBean>>

    interface Presenter : IGetPresenter<MutableList<BillBean>>
}