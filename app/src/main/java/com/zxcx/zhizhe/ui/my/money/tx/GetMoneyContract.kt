package com.zxcx.zhizhe.ui.my.money.tx

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.my.money.MoneyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/3
 * @Description :
 */
class GetMoneyContract {

    interface View: NullGetPostView<MoneyBean> {
        fun applyForWithdrawalSuccess()
    }

    interface Presenter:INullGetPostPresenter<MoneyBean>{
        fun applyForWithdrawalSuccess()
    }
}