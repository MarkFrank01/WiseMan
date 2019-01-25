package com.zxcx.zhizhe.ui.circle.circlemanlist

import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleManListContract{

    interface View:IGetPostPresenter<MutableList<CircleBean>,CircleBean>{
        fun getCircleMemberByCircleIdSuccess(bean:MutableList<CircleBean>)
        fun mFollowUserSuccess(bean: SearchUserBean)
        fun unFollowUserSuccess(bean: SearchUserBean)
    }

    interface Presnter:IGetPostPresenter<MutableList<CircleBean>,CircleBean>{
        fun getCircleMemberByCircleIdSuccess(bean:MutableList<CircleBean>)
        fun mFollowUserSuccess(bean: SearchUserBean)
        fun unFollowUserSuccess(bean: SearchUserBean)
    }

}