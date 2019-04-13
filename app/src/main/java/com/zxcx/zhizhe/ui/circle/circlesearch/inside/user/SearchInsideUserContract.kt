package com.zxcx.zhizhe.ui.circle.circlesearch.inside.user

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideUserContract {

    interface View : GetPostView<List<SearchUserBean>, SearchUserBean> {
        fun unFollowUserSuccess(bean: SearchUserBean)
    }

    interface Presenter : IGetPostPresenter<List<SearchUserBean>, SearchUserBean> {
        fun unFollowUserSuccess(bean: SearchUserBean)
    }
}