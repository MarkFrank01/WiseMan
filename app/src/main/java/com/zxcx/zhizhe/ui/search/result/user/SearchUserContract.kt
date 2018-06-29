package com.zxcx.zhizhe.ui.search.result.user

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter

interface SearchUserContract {

	interface View : GetPostView<List<SearchUserBean>, SearchUserBean> {
		fun unFollowUserSuccess(bean: SearchUserBean)
	}

	interface Presenter : IGetPostPresenter<List<SearchUserBean>, SearchUserBean> {
		fun unFollowUserSuccess(bean: SearchUserBean)
	}
}

