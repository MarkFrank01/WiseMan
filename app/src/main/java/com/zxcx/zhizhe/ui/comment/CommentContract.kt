package com.zxcx.zhizhe.ui.comment

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter

interface CommentContract {

	interface View : GetPostView<MutableList<CommentBean>, CommentBean> {
		fun likeSuccess()
		fun unlikeSuccess()
	}

	interface Presenter : IGetPostPresenter<MutableList<CommentBean>, CommentBean> {
		fun likeSuccess()
		fun unlikeSuccess()
	}
}

