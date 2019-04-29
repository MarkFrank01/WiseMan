package com.zxcx.zhizhe.ui.my

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.my.invite.InviteBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
interface MyFragmentContract {

    interface View : GetView<MyTabBean> {
        fun getADSuccess(list: MutableList<ADBean>)
        fun getInvitationInfoSuccess(bean: InviteBean)
        fun ewmError(msg:String)
    }

    interface Presenter : IGetPresenter<MutableList<MyTabBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
        fun getInvitationInfoSuccess(bean: InviteBean)
        fun ewmError(msg:String)
    }
}