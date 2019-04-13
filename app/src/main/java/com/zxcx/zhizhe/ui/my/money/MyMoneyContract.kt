package com.zxcx.zhizhe.ui.my.money

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
interface MyMoneyContract {

    interface View:NullGetPostView<MoneyBean>{
        fun getAccountDetailsSuccess(bean:MoneyBean)
        fun applyForWithdrawalSuccess()
    }

    interface Presenter:INullGetPostPresenter<MoneyBean>{
        fun getAccountDetailsSuccess(bean: MoneyBean)
        fun applyForWithdrawalSuccess()
    }
}