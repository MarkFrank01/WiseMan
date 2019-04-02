package com.zxcx.zhizhe.ui.my.invite.input

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.my.invite.InviteBean

/**
 * @author : MarkFrank01
 * @Created on 2019/4/2
 * @Description :
 */
interface InputInviteContract {
    interface View:GetView<InviteBean>{
        fun inputInvitationCodeSuccess(bean:InviteBean)
        fun errormsg(msg:String)
    }

    interface Presenter:IGetPresenter<InviteBean>{
        fun inputInvitationCodeSuccess(bean:InviteBean)
        fun errormsg(msg:String)
    }
}