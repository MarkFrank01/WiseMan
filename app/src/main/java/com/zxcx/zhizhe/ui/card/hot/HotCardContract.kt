package com.zxcx.zhizhe.ui.card.hot

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.my.invite.InviteBean
import com.zxcx.zhizhe.ui.welcome.ADBean

interface HotCardContract {

    interface View : GetView<MutableList<CardBean>> {
        fun inputInvitationCodeSuccess(bean: InviteBean)
        fun getADSuccess(list: MutableList<ADBean>)
        fun errormsg(msg:String)
    }

    interface Presenter : IGetPresenter<MutableList<CardBean>> {
        fun inputInvitationCodeSuccess(bean:InviteBean)
        fun getADSuccess(list: MutableList<ADBean>)
        fun errormsg(msg:String)
    }
}

