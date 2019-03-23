package com.zxcx.zhizhe.ui.my.invite

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
interface MyInviteContract {
    interface View : GetView<MutableList<InviteBean>> {
        fun getInvitationHistorySuccess(list: MutableList<InviteBean>)
    }

    interface Presenter : IGetPresenter<MutableList<InviteBean>> {
        fun getInvitationHistorySuccess(list: MutableList<InviteBean>)
    }
}