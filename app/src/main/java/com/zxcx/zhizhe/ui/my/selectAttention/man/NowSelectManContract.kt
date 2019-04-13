package com.zxcx.zhizhe.ui.my.selectAttention.man

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/28
 * @Description :
 */
class NowSelectManContract {
    interface View:NullGetPostView<MutableList<SearchUserBean>>{
        fun mFollowUserSuccess(bean: SearchUserBean)
        fun unFollowUserSuccess(bean: SearchUserBean)
    }

    interface Presenter:INullGetPostPresenter<MutableList<SearchUserBean>>{
        fun mFollowUserSuccess(bean: SearchUserBean)
        fun unFollowUserSuccess(bean: SearchUserBean)
    }
}