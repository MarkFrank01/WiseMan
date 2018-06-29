package com.zxcx.zhizhe.ui.my.followUser

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

interface FollowUserContract {

	interface View : GetPostView<MutableList<SearchUserBean>, SearchUserBean> {
		fun unFollowUserSuccess(bean: SearchUserBean)
		fun getEmptyFollowUserSuccess(list: MutableList<SearchUserBean>)
	}

	interface Presenter : IGetPostPresenter<MutableList<SearchUserBean>, SearchUserBean> {
		fun unFollowUserSuccess(bean: SearchUserBean)
		fun getEmptyFollowUserSuccess(list: MutableList<SearchUserBean>)
	}
}

