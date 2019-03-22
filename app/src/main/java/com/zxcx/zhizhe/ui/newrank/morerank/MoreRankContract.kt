package com.zxcx.zhizhe.ui.newrank.morerank

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class MoreRankContract {

    interface View :GetView<List<UserRankBean>>{
        fun followUserSuccess(bean:SearchUserBean)
        fun unFollowUserSuccess(bean: SearchUserBean)
        fun getMoreRankSuccess(list:List<UserRankBean>)
    }

    interface Presenter:IGetPresenter<List<UserRankBean>>{
        fun followUserSuccess(bean:SearchUserBean)
        fun unFollowUserSuccess(bean: SearchUserBean)
        fun getMoreRankSuccess(list:List<UserRankBean>)
    }
}