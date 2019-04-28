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
        fun getActivityInvitationHistorySuccess(list: MutableList<InviteBean>)
        fun getInvitationHistorySuccess(list: MutableList<InviteBean>)
        fun getInvitationInfoSuccess(bean: InviteBean)
        fun receiveInvitationCodeReward(bean: InviteBean)
    }

    interface Presenter : IGetPresenter<MutableList<InviteBean>> {
        fun getActivityInvitationHistorySuccess(list: MutableList<InviteBean>)
        fun getInvitationHistorySuccess(list: MutableList<InviteBean>)
        fun getInvitationInfoSuccess(bean:InviteBean)
        fun receiveInvitationCodeReward(bean: InviteBean)
    }
}