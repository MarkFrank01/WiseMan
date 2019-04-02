package com.zxcx.zhizhe.ui.my.money.editcount

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.my.invite.InviteBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/2
 * @Description :
 */
class EditMyCountContract {

    interface View : NullGetPostView<InviteBean>{
        fun bindingAlipaySuccess()
    }

    interface Presenter:INullGetPostPresenter<InviteBean>{
        fun bindingAlipaySuccess()
    }

}