package com.zxcx.zhizhe.ui.newrank.morerank

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.rank.UserRankBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class MoreRankContract {

    interface View : GetPostView<List<UserRankBean>,UserRankBean> {
        fun followUserSuccess(bean:UserRankBean,position:Int)
        fun unFollowUserSuccess(bean: UserRankBean,position:Int)
        fun getMoreRankSuccess(list:List<UserRankBean>)
    }

    interface Presenter: IGetPostPresenter<List<UserRankBean>,UserRankBean> {
        fun followUserSuccess(bean:UserRankBean,position:Int)
        fun unFollowUserSuccess(bean: UserRankBean,position:Int)
        fun getMoreRankSuccess(list:List<UserRankBean>)
    }
}