package com.zxcx.zhizhe.ui.my.followUser

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter

interface FollowUserContract {

	interface View : GetPostView<MutableList<FollowUserBean>, FollowUserBean> {
		fun unFollowUserSuccess(bean: FollowUserBean)
		fun getEmptyFollowUserSuccess(list: MutableList<FollowUserBean>)
	}

	interface Presenter : IGetPostPresenter<MutableList<FollowUserBean>, FollowUserBean> {
		fun unFollowUserSuccess(bean: FollowUserBean)
		fun getEmptyFollowUserSuccess(list: MutableList<FollowUserBean>)
	}
}

